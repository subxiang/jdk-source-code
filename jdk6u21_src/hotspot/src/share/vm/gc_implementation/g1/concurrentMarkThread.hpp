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

// The Concurrent Mark GC Thread (could be several in the future).
// This is copied from the Concurrent Mark Sweep GC Thread
// Still under construction.

class ConcurrentMark;

class ConcurrentMarkThread: public ConcurrentGCThread {
  friend class VMStructs;

  double _vtime_start;  // Initial virtual time.
  double _vtime_accum;  // Accumulated virtual time.

  double _vtime_mark_accum;
  double _vtime_count_accum;

 public:
  virtual void run();

 private:
  ConcurrentMark*                  _cm;
  volatile bool                    _started;
  volatile bool                    _in_progress;

  void sleepBeforeNextCycle();

  static SurrogateLockerThread*         _slt;

 public:
  // Constructor
  ConcurrentMarkThread(ConcurrentMark* cm);

  static void makeSurrogateLockerThread(TRAPS);
  static SurrogateLockerThread* slt() { return _slt; }

  // Printing
  void print_on(outputStream* st) const;
  void print() const;

  // Total virtual time so far.
  double vtime_accum();
  // Marking virtual time so far
  double vtime_mark_accum();
  // Counting virtual time so far.
  double vtime_count_accum() { return _vtime_count_accum; }

  ConcurrentMark* cm()     { return _cm; }

  void set_started()       { _started = true;  }
  void clear_started()     { _started = false; }
  bool started()           { return _started;  }

  void set_in_progress()   { _in_progress = true;  }
  void clear_in_progress() { _in_progress = false; }
  bool in_progress()       { return _in_progress;  }

  // This flag returns true from the moment a marking cycle is
  // initiated (during the initial-mark pause when started() is set)
  // to the moment when the cycle completes (just after the next
  // marking bitmap has been cleared and in_progress() is
  // cleared). While this flag is true we will not start another cycle
  // so that cycles do not overlap. We cannot use just in_progress()
  // as the CM thread might take some time to wake up before noticing
  // that started() is set and set in_progress().
  bool during_cycle()      { return started() || in_progress(); }

  // Yield for GC
  void            yield();

  // shutdown
  void stop();
};
