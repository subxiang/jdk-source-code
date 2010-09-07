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
DEBUG_CFLAGS/DEFAULT= $(DEBUG_CFLAGS)
DEBUG_CFLAGS/BYFILE = $(DEBUG_CFLAGS/$@)$(DEBUG_CFLAGS/DEFAULT$(DEBUG_CFLAGS/$@))

ifeq ("${Platform_compiler}", "sparcWorks")

ifeq ($(COMPILER_REV_NUMERIC),508)
  # SS11 SEGV when compiling with -g and -xarch=v8, using different backend
  DEBUG_CFLAGS/compileBroker.o = $(DEBUG_CFLAGS) -xO0
  DEBUG_CFLAGS/jvmtiTagMap.o   = $(DEBUG_CFLAGS) -xO0
endif
endif

CFLAGS += $(DEBUG_CFLAGS/BYFILE)

# Linker mapfiles
MAPFILE = $(GAMMADIR)/make/solaris/makefiles/mapfile-vers \
          $(GAMMADIR)/make/solaris/makefiles/mapfile-vers-debug \
          $(GAMMADIR)/make/solaris/makefiles/mapfile-vers-nonproduct

# This mapfile is only needed when compiling with dtrace support, 
# and mustn't be otherwise.
MAPFILE_DTRACE = $(GAMMADIR)/make/solaris/makefiles/mapfile-vers-$(TYPE)

_JUNK_ := $(shell echo >&2 ""\
 "-------------------------------------------------------------------------\n" \
 "WARNING: 'gnumake debug' is deprecated. It will be removed in the future.\n" \
 "Please use 'gnumake jvmg' to build debug JVM.                            \n" \
 "-------------------------------------------------------------------------\n")

G_SUFFIX = _g
VERSION = debug
SYSDEFS += -DASSERT -DDEBUG
PICFLAGS = DEFAULT
