/*
 * Copyright (c) 2000, 2007, Oracle and/or its affiliates. All rights reserved.
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

// Sets the default values for platform dependent flags used by the server compiler.
// (see c2_globals.hpp).  Alpha-sorted.

define_pd_global(bool, BackgroundCompilation,        true);
define_pd_global(bool, CICompileOSR,                 true);
define_pd_global(bool, InlineIntrinsics,             false);
define_pd_global(bool, PreferInterpreterNativeStubs, false);
define_pd_global(bool, ProfileTraps,                 true);
define_pd_global(bool, UseOnStackReplacement,        true);
#ifdef CC_INTERP
define_pd_global(bool, ProfileInterpreter,           false);
#else
define_pd_global(bool, ProfileInterpreter,           true);
#endif // CC_INTERP
define_pd_global(bool, TieredCompilation,            false);
#ifdef TIERED
define_pd_global(intx, CompileThreshold,             1000);
define_pd_global(intx, BackEdgeThreshold,            14000);
#else
define_pd_global(intx, CompileThreshold,             10000);
define_pd_global(intx, BackEdgeThreshold,            140000);
#endif // TIERED

define_pd_global(intx, Tier2CompileThreshold,        10000); // unused level
define_pd_global(intx, Tier3CompileThreshold,        10000);
define_pd_global(intx, Tier4CompileThreshold,        40000);

define_pd_global(intx, Tier2BackEdgeThreshold,       100000);
define_pd_global(intx, Tier3BackEdgeThreshold,       100000);
define_pd_global(intx, Tier4BackEdgeThreshold,       100000);

define_pd_global(intx, OnStackReplacePercentage,     140);
define_pd_global(intx, ConditionalMoveLimit,         4);
define_pd_global(intx, FLOATPRESSURE,                52);  // C2 on V9 gets to use all the float/double registers
define_pd_global(intx, FreqInlineSize,               175);
define_pd_global(intx, INTPRESSURE,                  48);  // large register set
define_pd_global(intx, InteriorEntryAlignment,       16);  // = CodeEntryAlignment
define_pd_global(intx, NewSizeThreadIncrease, ScaleForWordSize(4*K));
// The default setting 16/16 seems to work best.
// (For _228_jack 16/16 is 2% better than 4/4, 16/4, 32/32, 32/16, or 16/32.)
define_pd_global(intx, OptoLoopAlignment,            16);  // = 4*wordSize
define_pd_global(intx, RegisterCostAreaRatio,        12000);
define_pd_global(bool, UseTLAB,                      true);
define_pd_global(bool, ResizeTLAB,                   true);
define_pd_global(intx, LoopUnrollLimit,              60); // Design center runs on 1.3.1

// Peephole and CISC spilling both break the graph, and so makes the
// scheduler sick.
define_pd_global(bool, OptoPeephole,                 false);
define_pd_global(bool, UseCISCSpill,                 false);
define_pd_global(bool, OptoBundling,                 false);
define_pd_global(bool, OptoScheduling,               true);

#ifdef _LP64
// We need to make sure that all generated code is within
// 2 gigs of the libjvm.so runtime routines so we can use
// the faster "call" instruction rather than the expensive
// sequence of instructions to load a 64 bit pointer.
//
// InitialCodeCacheSize derived from specjbb2000 run.
define_pd_global(intx, InitialCodeCacheSize,         2048*K); // Integral multiple of CodeCacheExpansionSize
define_pd_global(intx, ReservedCodeCacheSize,        48*M);
define_pd_global(intx, CodeCacheExpansionSize,       64*K);

// Ergonomics related flags
define_pd_global(uint64_t,MaxRAM,                    128ULL*G);
#else
// InitialCodeCacheSize derived from specjbb2000 run.
define_pd_global(intx, InitialCodeCacheSize,         1536*K); // Integral multiple of CodeCacheExpansionSize
define_pd_global(intx, ReservedCodeCacheSize,        32*M);
define_pd_global(intx, CodeCacheExpansionSize,       32*K);
// Ergonomics related flags
define_pd_global(uint64_t,MaxRAM,                    4ULL*G);
#endif
define_pd_global(uintx,CodeCacheMinBlockLength,      4);

// Heap related flags
define_pd_global(uintx,PermSize,    ScaleForWordSize(16*M));
define_pd_global(uintx,MaxPermSize, ScaleForWordSize(64*M));

// Ergonomics related flags
define_pd_global(bool, NeverActAsServerClassMachine, false);
