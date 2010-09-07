/*
 * Copyright (c) 2002, Oracle and/or its affiliates. All rights reserved.
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

package sun.jvm.hotspot.asm.sparc;

import sun.jvm.hotspot.asm.*;

public class SPARCLogicInstruction extends SPARCFormat3AInstruction
    implements LogicInstruction {
    final private int operation;

    public SPARCLogicInstruction(String name, int opcode, int operation, SPARCRegister rs1,
                                 ImmediateOrRegister operand2, SPARCRegister rd) {
        super(name, opcode, rs1, operand2, rd);
        this.operation = operation;
    }

    protected String getDescription() {
        SPARCRegister G0 = SPARCRegisters.G0;
        if (opcode == ORcc && rd == G0 && rd == operand2) {
            StringBuffer buf = new StringBuffer();
            buf.append("tst");
            buf.append(spaces);
            buf.append(getOperand2String());
            return buf.toString();
        } else if (opcode == XNOR && G0 == operand2) {
            StringBuffer buf = new StringBuffer();
            buf.append("not");
            buf.append(spaces);
            buf.append(rs1.toString());
            if (rs1 != rd) {
                buf.append(comma);
                buf.append(rd.toString());
            }
            return buf.toString();
        } else if (opcode == ANDcc && rd == G0) {
            StringBuffer buf = new StringBuffer();
            buf.append("btst");
            buf.append(spaces);
            buf.append(getOperand2String());
            buf.append(comma);
            buf.append(rd.toString());
            return buf.toString();
        } else if (rs1 == rd) {
            StringBuffer buf = new StringBuffer();
            switch (opcode) {
                case OR:
                    buf.append("bset");
                    break;
                case ANDN:
                    buf.append("bclr");
                    break;
                case XOR:
                    buf.append("btog");
                    break;
                default:
                    return super.getDescription();
            }
            buf.append(spaces);
            buf.append(getOperand2String());
            buf.append(comma);
            buf.append(rd.toString());
            return buf.toString();
        } else {
            return super.getDescription();
        }
    }

    public Operand getLogicDestination() {
        return getDestinationRegister();
    }

    public Operand[] getLogicSources() {
        return (new Operand[] { rs1, operand2 });
    }

    public int getOperation() {
        return operation;
    }

    public boolean isLogic() {
        return true;
    }
}
