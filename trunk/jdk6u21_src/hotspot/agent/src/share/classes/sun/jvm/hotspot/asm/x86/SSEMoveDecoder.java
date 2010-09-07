/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
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

public class SSEMoveDecoder extends SSEInstructionDecoder {

   public SSEMoveDecoder(String name, int addrMode1, int operandType1, int addrMode2, int operandType2) {
      super(name, addrMode1, operandType1, addrMode2, operandType2);
   }

   protected Instruction decodeInstruction(byte[] bytesArray, boolean operandSize, boolean addrSize, X86InstructionFactory factory) {
      Operand op1 = getOperand1(bytesArray, operandSize, addrSize);
      Operand op2 = getOperand2(bytesArray, operandSize, addrSize);
      int size = byteIndex - instrStartIndex;

      if( (op1 instanceof X86Register) && (op2 instanceof ImmediateOrRegister) ) {
         return factory.newMoveInstruction(name, (X86Register)op1, (ImmediateOrRegister)op2, size, 0);
      }
      else if( (op1 instanceof Address) && (op2 instanceof Immediate) ) {
         return factory.newGeneralInstruction(name, op1, op2, size, 0);
      }
      else if( (op1 instanceof Address) && (op2 instanceof X86Register) ) {
         return factory.newMoveStoreInstruction(name, (Address)op1, (X86Register)op2, 0, size, 0);
      }
      else if( (op1 instanceof X86Register) && (op2 instanceof Address) ) {
         return factory.newMoveLoadInstruction(name, (X86Register)op1, (Address)op2, 0, size, 0);
      }

      return null;
   }
}
