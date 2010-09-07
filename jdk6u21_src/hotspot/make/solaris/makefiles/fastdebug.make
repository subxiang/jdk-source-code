#
# Copyright (c) 1998, 2008, Oracle and/or its affiliates. All rights reserved.
# ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#  
#

# Sets make macros for making debug version of VM

# Compiler specific DEBUG_CFLAGS are passed in from gcc.make, sparcWorks.make
# They may also specify FASTDEBUG_CFLAGS, but it defaults to DEBUG_CFLAGS.

FASTDEBUG_CFLAGS$(FASTDEBUG_CFLAGS) = $(DEBUG_CFLAGS)

# Compiler specific OPT_CFLAGS are passed in from gcc.make, sparcWorks.make
OPT_CFLAGS/DEFAULT= $(OPT_CFLAGS)
OPT_CFLAGS/BYFILE = $(OPT_CFLAGS/$@)$(OPT_CFLAGS/DEFAULT$(OPT_CFLAGS/$@))

ifeq ("${Platform_compiler}", "sparcWorks")
OPT_CFLAGS/SLOWER = -xO2

# Problem with SS12 compiler, dtrace doesn't like the .o files  (bug 6693876)
ifeq ($(COMPILER_REV_NUMERIC), 509)
  # To avoid jvm98 crash
  OPT_CFLAGS/instanceKlass.o = $(OPT_CFLAGS/SLOWER)
  # Not clear this workaround could be skipped in some cases.
  OPT_CFLAGS/vmGCOperations.o = $(OPT_CFLAGS/SLOWER)
  OPT_CFLAGS/java.o = $(OPT_CFLAGS/SLOWER)
  OPT_CFLAGS/jni.o = $(OPT_CFLAGS/SLOWER)
endif

ifeq ($(COMPILER_REV_NUMERIC), 505)
# CC 5.5 has bug 4908364 with -xO4  (Fixed in 5.6)
OPT_CFLAGS/library_call.o = $(OPT_CFLAGS/SLOWER)
endif # COMPILER_REV_NUMERIC == 505

ifeq ($(shell expr $(COMPILER_REV_NUMERIC) \<= 504), 1)
# Compilation of *_<arch>.cpp can take an hour or more at O3.  Use O2
# See comments at top of sparc.make.
OPT_CFLAGS/ad_$(Platform_arch_model).o = $(OPT_CFLAGS/SLOWER)
OPT_CFLAGS/dfa_$(Platform_arch_model).o = $(OPT_CFLAGS/SLOWER)
endif # COMPILER_REV_NUMERIC <= 504

ifeq ($(shell expr $(COMPILER_REV_NUMERIC) \< 505), 1)
# Same problem with Solaris/x86 compiler (both 5.0 and 5.2) on ad_x86_{32,64}.cpp.
# CC build time is also too long for ad_$(Platform_arch_model)_{gen,misc}.o
OPT_CFLAGS/ad_$(Platform_arch_model).o = -c
OPT_CFLAGS/ad_$(Platform_arch_model)_gen.o = -c
OPT_CFLAGS/ad_$(Platform_arch_model)_misc.o = -c
ifeq ($(Platform_arch), x86)
# Same problem for the wrapper roosts: jni.o jvm.o
OPT_CFLAGS/jni.o = -c
OPT_CFLAGS/jvm.o = -c
# Same problem in parse2.o (probably the Big Switch over bytecodes)
OPT_CFLAGS/parse2.o = -c
endif # Platform_arch == x86
endif

# Frame size > 100k  if we allow inlining via -g0!
DEBUG_CFLAGS/bytecodeInterpreter.o = -g
DEBUG_CFLAGS/bytecodeInterpreterWithChecks.o = -g
ifeq ($(Platform_arch), x86)
# ube explodes on x86
OPT_CFLAGS/bytecodeInterpreter.o = -xO1
OPT_CFLAGS/bytecodeInterpreterWithChecks.o =  -xO1
endif # Platform_arch == x86

endif # Platform_compiler == sparcWorks

# Workaround for a bug in dtrace.  If ciEnv::post_compiled_method_load_event()
# is inlined, the resulting dtrace object file needs a reference to this
# function, whose symbol name is too long for dtrace.  So disable inlining
# for this method for now. (fix this when dtrace bug 6258412 is fixed)
OPT_CFLAGS/ciEnv.o = $(OPT_CFLAGS) -xinline=no%__1cFciEnvbFpost_compiled_method_load_event6MpnHnmethod__v_

# (OPT_CFLAGS/SLOWER is also available, to alter compilation of buggy files)

# If you set HOTSPARC_GENERIC=yes, you disable all OPT_CFLAGS settings
CFLAGS$(HOTSPARC_GENERIC) += $(OPT_CFLAGS/BYFILE)

# Set the environment variable HOTSPARC_GENERIC to "true"
# to inhibit the effect of the previous line on CFLAGS.

# The following lines are copied from debug.make, except that we
# consult FASTDEBUG_CFLAGS instead of DEBUG_CFLAGS.
# Compiler specific DEBUG_CFLAGS are passed in from gcc.make, sparcWorks.make
DEBUG_CFLAGS/DEFAULT= $(FASTDEBUG_CFLAGS)
DEBUG_CFLAGS/BYFILE = $(DEBUG_CFLAGS/$@)$(DEBUG_CFLAGS/DEFAULT$(DEBUG_CFLAGS/$@))
CFLAGS += $(DEBUG_CFLAGS/BYFILE)

# Linker mapfiles
MAPFILE = $(GAMMADIR)/make/solaris/makefiles/mapfile-vers \
	  $(GAMMADIR)/make/solaris/makefiles/mapfile-vers-debug \
	  $(GAMMADIR)/make/solaris/makefiles/mapfile-vers-nonproduct

# This mapfile is only needed when compiling with dtrace support, 
# and mustn't be otherwise.
MAPFILE_DTRACE = $(GAMMADIR)/make/solaris/makefiles/mapfile-vers-$(TYPE)

G_SUFFIX = _g
VERSION = optimized
SYSDEFS += -DASSERT -DFASTDEBUG -DCHECK_UNHANDLED_OOPS
PICFLAGS = DEFAULT
