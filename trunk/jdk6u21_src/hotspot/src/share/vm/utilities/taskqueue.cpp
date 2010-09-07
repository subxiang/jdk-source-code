/*
 * Copyright (c) 2001, 2009, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

# include "incls/_precompiled.incl"
# include "incls/_taskqueue.cpp.incl"

#ifdef TRACESPINNING
uint ParallelTaskTerminator::_total_yields = 0;
uint ParallelTaskTerminator::_total_spins = 0;
uint ParallelTaskTerminator::_total_peeks = 0;
#endif

bool TaskQueueSuper::peek() {
  return _bottom != _age.top();
}

int TaskQueueSetSuper::randomParkAndMiller(int *seed0) {
  const int a =      16807;
  const int m = 2147483647;
  const int q =     127773;  /* m div a */
  const int r =       2836;  /* m mod a */
  assert(sizeof(int) == 4, "I think this relies on that");
  int seed = *seed0;
  int hi   = seed / q;
  int lo   = seed % q;
  int test = a * lo - r * hi;
  if (test > 0)
    seed = test;
  else
    seed = test + m;
  *seed0 = seed;
  return seed;
}

ParallelTaskTerminator::
ParallelTaskTerminator(int n_threads, TaskQueueSetSuper* queue_set) :
  _n_threads(n_threads),
  _queue_set(queue_set),
  _offered_termination(0) {}

bool ParallelTaskTerminator::peek_in_queue_set() {
  return _queue_set->peek();
}

void ParallelTaskTerminator::yield() {
  assert(_offered_termination <= _n_threads, "Invariant");
  os::yield();
}

void ParallelTaskTerminator::sleep(uint millis) {
  assert(_offered_termination <= _n_threads, "Invariant");
  os::sleep(Thread::current(), millis, false);
}

bool
ParallelTaskTerminator::offer_termination(TerminatorTerminator* terminator) {
  assert(_offered_termination < _n_threads, "Invariant");
  Atomic::inc(&_offered_termination);

  uint yield_count = 0;
  // Number of hard spin loops done since last yield
  uint hard_spin_count = 0;
  // Number of iterations in the hard spin loop.
  uint hard_spin_limit = WorkStealingHardSpins;

  // If WorkStealingSpinToYieldRatio is 0, no hard spinning is done.
  // If it is greater than 0, then start with a small number
  // of spins and increase number with each turn at spinning until
  // the count of hard spins exceeds WorkStealingSpinToYieldRatio.
  // Then do a yield() call and start spinning afresh.
  if (WorkStealingSpinToYieldRatio > 0) {
    hard_spin_limit = WorkStealingHardSpins >> WorkStealingSpinToYieldRatio;
    hard_spin_limit = MAX2(hard_spin_limit, 1U);
  }
  // Remember the initial spin limit.
  uint hard_spin_start = hard_spin_limit;

  // Loop waiting for all threads to offer termination or
  // more work.
  while (true) {
    assert(_offered_termination <= _n_threads, "Invariant");
    // Are all threads offering termination?
    if (_offered_termination == _n_threads) {
      return true;
    } else {
      // Look for more work.
      // Periodically sleep() instead of yield() to give threads
      // waiting on the cores the chance to grab this code
      if (yield_count <= WorkStealingYieldsBeforeSleep) {
        // Do a yield or hardspin.  For purposes of deciding whether
        // to sleep, count this as a yield.
        yield_count++;

        // Periodically call yield() instead spinning
        // After WorkStealingSpinToYieldRatio spins, do a yield() call
        // and reset the counts and starting limit.
        if (hard_spin_count > WorkStealingSpinToYieldRatio) {
          yield();
          hard_spin_count = 0;
          hard_spin_limit = hard_spin_start;
#ifdef TRACESPINNING
          _total_yields++;
#endif
        } else {
          // Hard spin this time
          // Increase the hard spinning period but only up to a limit.
          hard_spin_limit = MIN2(2*hard_spin_limit,
                                 (uint) WorkStealingHardSpins);
          for (uint j = 0; j < hard_spin_limit; j++) {
            SpinPause();
          }
          hard_spin_count++;
#ifdef TRACESPINNING
          _total_spins++;
#endif
        }
      } else {
        if (PrintGCDetails && Verbose) {
         gclog_or_tty->print_cr("ParallelTaskTerminator::offer_termination() "
           "thread %d sleeps after %d yields",
           Thread::current(), yield_count);
        }
        yield_count = 0;
        // A sleep will cause this processor to seek work on another processor's
        // runqueue, if it has nothing else to run (as opposed to the yield
        // which may only move the thread to the end of the this processor's
        // runqueue).
        sleep(WorkStealingSleepMillis);
      }

#ifdef TRACESPINNING
      _total_peeks++;
#endif
      if (peek_in_queue_set() ||
          (terminator != NULL && terminator->should_exit_termination())) {
        Atomic::dec(&_offered_termination);
        assert(_offered_termination < _n_threads, "Invariant");
        return false;
      }
    }
  }
}

#ifdef TRACESPINNING
void ParallelTaskTerminator::print_termination_counts() {
  gclog_or_tty->print_cr("ParallelTaskTerminator Total yields: %lld  "
    "Total spins: %lld  Total peeks: %lld",
    total_yields(),
    total_spins(),
    total_peeks());
}
#endif

void ParallelTaskTerminator::reset_for_reuse() {
  if (_offered_termination != 0) {
    assert(_offered_termination == _n_threads,
           "Terminator may still be in use");
    _offered_termination = 0;
  }
}

void RegionTaskQueueWithOverflow::save(RegionTask t) {
  if (TraceRegionTasksQueuing && Verbose) {
    gclog_or_tty->print_cr("CTQ: save " PTR_FORMAT, t);
  }
  if(!_region_queue.push(t)) {
    _overflow_stack.push(t);
  }
}

// Note that using this method will retrieve all regions
// that have been saved but that it will always check
// the overflow stack.  It may be more efficient to
// check the stealable queue and the overflow stack
// separately.
bool RegionTaskQueueWithOverflow::retrieve(RegionTask& region_task) {
  bool result = retrieve_from_overflow(region_task);
  if (!result) {
    result = retrieve_from_stealable_queue(region_task);
  }
  if (TraceRegionTasksQueuing && Verbose && result) {
    gclog_or_tty->print_cr("  CTQ: retrieve " PTR_FORMAT, result);
  }
  return result;
}

bool RegionTaskQueueWithOverflow::retrieve_from_stealable_queue(
                                   RegionTask& region_task) {
  bool result = _region_queue.pop_local(region_task);
  if (TraceRegionTasksQueuing && Verbose) {
    gclog_or_tty->print_cr("CTQ: retrieve_stealable " PTR_FORMAT, region_task);
  }
  return result;
}

bool
RegionTaskQueueWithOverflow::retrieve_from_overflow(RegionTask& region_task) {
  bool result;
  if (!_overflow_stack.is_empty()) {
    region_task = _overflow_stack.pop();
    result = true;
  } else {
    region_task = (RegionTask) NULL;
    result = false;
  }
  if (TraceRegionTasksQueuing && Verbose) {
    gclog_or_tty->print_cr("CTQ: retrieve_stealable " PTR_FORMAT, region_task);
  }
  return result;
}
