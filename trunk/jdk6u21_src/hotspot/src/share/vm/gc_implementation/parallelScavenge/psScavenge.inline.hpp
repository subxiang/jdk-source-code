/*
 * Copyright (c) 2002, 2008, Oracle and/or its affiliates. All rights reserved.
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

inline void PSScavenge::save_to_space_top_before_gc() {
  ParallelScavengeHeap* heap = (ParallelScavengeHeap*)Universe::heap();
  _to_space_top_before_gc = heap->young_gen()->to_space()->top();
}

template <class T> inline bool PSScavenge::should_scavenge(T* p) {
  T heap_oop = oopDesc::load_heap_oop(p);
  if (oopDesc::is_null(heap_oop)) return false;
  oop obj = oopDesc::decode_heap_oop_not_null(heap_oop);
  return PSScavenge::is_obj_in_young((HeapWord*)obj);
}

template <class T>
inline bool PSScavenge::should_scavenge(T* p, MutableSpace* to_space) {
  if (should_scavenge(p)) {
    oop obj = oopDesc::load_decode_heap_oop_not_null(p);
    // Skip objects copied to to_space since the scavenge started.
    HeapWord* const addr = (HeapWord*)obj;
    return addr < to_space_top_before_gc() || addr >= to_space->end();
  }
  return false;
}

template <class T>
inline bool PSScavenge::should_scavenge(T* p, bool check_to_space) {
  if (check_to_space) {
    ParallelScavengeHeap* heap = (ParallelScavengeHeap*)Universe::heap();
    return should_scavenge(p, heap->young_gen()->to_space());
  }
  return should_scavenge(p);
}

// Attempt to "claim" oop at p via CAS, push the new obj if successful
// This version tests the oop* to make sure it is within the heap before
// attempting marking.
template <class T>
inline void PSScavenge::copy_and_push_safe_barrier(PSPromotionManager* pm,
                                                   T*                  p) {
  assert(should_scavenge(p, true), "revisiting object?");

  oop o = oopDesc::load_decode_heap_oop_not_null(p);
  oop new_obj = o->is_forwarded()
        ? o->forwardee()
        : pm->copy_to_survivor_space(o, pm->depth_first());
  oopDesc::encode_store_heap_oop_not_null(p, new_obj);

  // We cannot mark without test, as some code passes us pointers
  // that are outside the heap.
  if ((!PSScavenge::is_obj_in_young((HeapWord*)p)) &&
      Universe::heap()->is_in_reserved(p)) {
    if (PSScavenge::is_obj_in_young((HeapWord*)new_obj)) {
      card_table()->inline_write_ref_field_gc(p, new_obj);
    }
  }
}
