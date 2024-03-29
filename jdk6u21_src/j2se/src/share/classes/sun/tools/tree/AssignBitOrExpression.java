/*
 * @(#)AssignBitOrExpression.java	1.22 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.tools.tree;

import sun.tools.java.*;
import sun.tools.asm.Assembler;

/**
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public
class AssignBitOrExpression extends AssignOpExpression {
    /**
     * Constructor
     */
    public AssignBitOrExpression(long where, Expression left, Expression right) {
	super(ASGBITOR, where, left, right);
    }


    /**
     * Code
     */
    void codeOperation(Environment env, Context ctx, Assembler asm) {
	asm.add(where, opc_ior + itype.getTypeCodeOffset());
    }
}
