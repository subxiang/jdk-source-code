/*
 * @(#)FieldUpdater.java	1.12 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.tools.tree;

import sun.tools.java.*;
import sun.tools.asm.Assembler;
import java.io.PrintStream;

/**
 * This class encapsulates the information required to generate an update to a private
 * field referenced from another class, e.g., an inner class.  An expression denoting a
 * reference to the object to which the field belongs is associated with getter and
 * setter methods.
 * <p>
 * We use this class only for assignment, increment, and decrement operators, in which
 * the old value is first retrieved and then a new value is computed and stored.
 * Simple assignment expressions in which a value is copied without modification are
 * handled by another mechanism.
 *
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */

class FieldUpdater implements Constants {

    // Location for reporting errors.
    // Errors will always indicate compiler failure, but these will be easier to diagnose
    // if the bogus error is localized to the offending assignment.
    private long where;

    // The field to which this updater applies.
    // It would be easy to eliminate the need to store the field here, but we retain it for
    // diagnostic  purposes.
    private MemberDefinition field;

    // Expression denoting the object to which the getter and setter are applied.
    // If the field is static, 'base' may be null, but need not be, as a static field
    // may be selected from an object reference. Even though the value of the object
    // reference will be ignored, it may have side-effects.
    private Expression base;

    // The getter and setter methods, generated by 'getAccessMember' and 'getUpdateMember'.
    private MemberDefinition getter;
    private MemberDefinition setter;

    // The number of words occupied on the stack by the object reference.
    // For static fields, this is zero.
    private int depth;

    /**
     * Constructor.
     */

    public FieldUpdater(long where, MemberDefinition field,
			Expression base, MemberDefinition getter, MemberDefinition setter) {
	this.where = where;
	this.field = field;
	this.base = base;
	this.getter = getter;
	this.setter = setter;
    }


    /**
     * Since the object reference expression may be captured before it has been inlined,
     * we must inline it later.  A <code>FieldUpdater</code> is inlined essentially as if
     * it were a child of the assignment node to which it belongs.
     */

    public FieldUpdater inline(Environment env, Context ctx) {
	if (base != null) {
	    if (field.isStatic()) {
		base = base.inline(env, ctx);
	    } else {
		base = base.inlineValue(env, ctx);
	    }
	}
	return this;
    }

    public FieldUpdater copyInline(Context ctx) {
	return new FieldUpdater(where, field, base.copyInline(ctx), getter, setter);
    }

    public int costInline(int thresh, Environment env, Context ctx, boolean needGet) {
	// Size of 'invokestatic' call for access method is 3 bytes.
	int cost = needGet ? 7 : 3;  // getter needs extra invokestatic + dup
	// Size of expression to compute 'this' arg if needed.
	if (!field.isStatic() && base != null) {
	    cost += base.costInline(thresh, env, ctx);
	}
	// We ignore the cost of duplicating value in value-needed context.
	return cost;
    }

    /**
     * Duplicate <code>items</code> words from the top of the stack, locating them
     * below the topmost <code>depth</code> words on the stack.
     */

    // This code was cribbed from 'Expression.java'.  We cannot reuse that code here,
    // because we do not inherit from class 'Expression'.

    private void codeDup(Assembler asm, int items, int depth) {
	switch (items) {
	  case 0:
	    return;
	  case 1:
	    switch (depth) {
	      case 0:
		asm.add(where, opc_dup);
		return;
	      case 1:
		asm.add(where, opc_dup_x1);
		return;
	      case 2:
		asm.add(where, opc_dup_x2);
		return;

	    }
	    break;
	  case 2:
	    switch (depth) {
	      case 0:
		asm.add(where, opc_dup2);
		return;
	      case 1:
		asm.add(where, opc_dup2_x1);
		return;
	      case 2:
		asm.add(where, opc_dup2_x2);
		return;

	    }
	    break;
	}
 	throw new CompilerError("can't dup: " + items + ", " + depth);
    }

    /**
     * Begin a field update by an assignment, increment, or decrement operator.
     * The current value of the field is left at the top of the stack.
     * If <code>valNeeded</code> is true, we arrange for the initial value to remain
     * on the stack after the update.
     */

    public void startUpdate(Environment env, Context ctx, Assembler asm, boolean valNeeded) {
	if (!(getter.isStatic() && setter.isStatic())) {
	    throw new CompilerError("startUpdate isStatic");
	}
	if (!field.isStatic()) {
	    // Provide explicit 'this' argument.
	    base.codeValue(env, ctx, asm);
	    depth = 1;
	} else {
	    // May need to evaluate 'base' for effect.
	    // If 'base' was a type expression, it should have previously been inlined away.
	    if (base != null) {
		base.code(env, ctx, asm);
	    }
	    depth = 0;
	}
	codeDup(asm, depth, 0);
	asm.add(where, opc_invokestatic, getter);
	if (valNeeded) {
	    codeDup(asm, field.getType().stackSize(), depth);
	}
    }

    /**
     * Complete a field update by an assignment, increment, or decrement operator.
     * The original value of the field left on the stack by <code>startUpdate</code>
     * must have been replaced with the updated value, with no other stack alterations.
     * If <code>valNeeded</code> is true, we arrange for the updated value to remain
     * on the stack after the update.  The <code>valNeeded</code> argument must not be
     * true in both <code>startUpdate</code> and <code>finishUpdate</code>.
     */

    public void finishUpdate(Environment env, Context ctx, Assembler asm, boolean valNeeded) {
	if (valNeeded) {
	    codeDup(asm, field.getType().stackSize(), depth);
	}
	asm.add(where, opc_invokestatic, setter);
    }

    /**
     * Like above, but used when assigning a new value independent of the
     * old, as in a simple assignment expression.  After 'startAssign',
     * code must be emitted to leave one additional value on the stack without
     * altering any others, followed by 'finishAssign'.
     */

    public void startAssign(Environment env, Context ctx, Assembler asm) {
	if (!setter.isStatic()) {
	    throw new CompilerError("startAssign isStatic");
	}
	if (!field.isStatic()) {
	    // Provide explicit 'this' argument.
	    base.codeValue(env, ctx, asm);
	    depth = 1;
	} else {
	    // May need to evaluate 'base' for effect.
	    // If 'base' was a type expression, it should have previously been inlined away.
	    if (base != null) {
		base.code(env, ctx, asm);
	    }
	    depth = 0;
	}
    }

    public void finishAssign(Environment env, Context ctx, Assembler asm, boolean valNeeded) {
	if (valNeeded) {
	    codeDup(asm, field.getType().stackSize(), depth);
	}
	asm.add(where, opc_invokestatic, setter);
    }

}
