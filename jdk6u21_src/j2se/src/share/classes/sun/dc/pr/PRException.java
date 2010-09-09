/*
 * @(#)PRException.java	1.16 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * DO NOT EDIT THIS FILE - it is automatically generated
 *
 * PRException.java		Tue Nov 18 13:32:19 PST 1997
 *
 * ---------------------------------------------------------------------
 *	Copyright (c) 1996-1997 by Ductus, Inc. All Rights Reserved.
 * ---------------------------------------------------------------------
 *
 */

package sun.dc.pr;

public class PRException extends java.lang.Exception
{

    public static final String
	BAD_COORD_setOutputArea = "setOutputArea: alpha coordinate out of bounds",
	ALPHA_ARRAY_SHORT = "writeAlpha: alpha destination array too short",
	DUMMY = "";

    // Constructors
    public PRException() {
	super();
    }

    public PRException(String s) {
	super(s);
    }
}
