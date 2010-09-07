/*
 * Copyright (c) 2002, 2003, Oracle and/or its affiliates. All rights reserved.
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

package sun.jvm.hotspot.asm.x86;

import sun.jvm.hotspot.asm.*;

public class FloatGRPDecoder extends FPInstructionDecoder {

   final private int number;

   //Please refer to IA-32 Intel Architecture Software Developer's Manual Volume 2
   //APPENDIX A - Escape opcodes

   private static final FPInstructionDecoder floatGRPMap[][] = {
      /* d9_2 */
      {
      new FPInstructionDecoder("fnop"),
      null,
      null,
      null,
      null,
      null,
      null,
      null
      },
      /*  d9_4 */
      {
      new FPInstructionDecoder("fchs"),
      new FPInstructionDecoder("fabs"),
      null,
      null,
      new FPInstructionDecoder("ftst"),
      new FPInstructionDecoder("fxam"),
      null,
      null
      },
      /* d9_5 */
      {
      new FPInstructionDecoder("fld1"),
      new FPInstructionDecoder("fldl2t"),
      new FPInstructionDecoder("fldl2e"),
      new FPInstructionDecoder("fldpi"),
      new FPInstructionDecoder("fldlg2"),
      new FPInstructionDecoder("fldln2"),
      new FPInstructionDecoder("fldz"),
      null
      },
      /* d9_6 */
      {
      new FPInstructionDecoder("f2xm1"),
      new FPInstructionDecoder("fyl2x"),
      new FPInstructionDecoder("fptan"),
      new FPInstructionDecoder("fpatan"),
      new FPInstructionDecoder("fxtract"),
      new FPInstructionDecoder("fprem1"),
      new FPInstructionDecoder("fdecstp"),
      new FPInstructionDecoder("fincstp")
      },
      /* d9_7 */
      {
      new FPInstructionDecoder("fprem"),
      new FPInstructionDecoder("fyl2xp1"),
      new FPInstructionDecoder("fsqrt"),
      new FPInstructionDecoder("fsincos"),
      new FPInstructionDecoder("frndint"),
      new FPInstructionDecoder("fscale"),
      new FPInstructionDecoder("fsin"),
      new FPInstructionDecoder("fcos")
      },
      /* da_5 */
      {
      null,
      new FPInstructionDecoder("fucompp"),
      null,
      null,
      null,
      null,
      null,
      null
      },
      /* db_4 */
      {
      new FPInstructionDecoder("feni(287 only)"),
      new FPInstructionDecoder("fdisi(287 only)"),
      new FPInstructionDecoder("fNclex"),
      new FPInstructionDecoder("fNinit"),
      new FPInstructionDecoder("fNsetpm(287 only)"),
      null,
      null,
      null
      },
      /* de_3 */
      {
      null,
      new FPInstructionDecoder("fcompp"),
      null,
      null,
      null,
      null,
      null,
      null
      },
      /* df_4 */
      {
      new FPInstructionDecoder("fNstsw"),
      null,
      null,
      null,
      null,
      null,
      null,
      null
      }
   };

   public FloatGRPDecoder(String name, int number) {
      super(name);
      this.number = number;
   }

   public Instruction decode(byte[] bytesArray, int index, int instrStartIndex, int segmentOverride, int prefixes, X86InstructionFactory factory) {
      this.byteIndex = index;
      this.instrStartIndex = instrStartIndex;
      this.prefixes = prefixes;

      int ModRM = readByte(bytesArray, byteIndex);
      int rm = ModRM & 7;

      FPInstructionDecoder instrDecoder = null;
      instrDecoder = floatGRPMap[number][rm];

      Instruction instr = null;
      if(instrDecoder != null) {
         instr = instrDecoder.decode(bytesArray, byteIndex, instrStartIndex, segmentOverride, prefixes, factory);
         byteIndex = instrDecoder.getCurrentIndex();
      } else {
         instr = factory.newIllegalInstruction();
      }
      return instr;
   }
}
