/*
 * @(#)InvalidObjectException.java	1.20 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.io;

/**
 * Indicates that one or more deserialized objects failed validation
 * tests.  The argument should provide the reason for the failure.
 *
 * @see ObjectInputValidation
 * @since JDK1.1
 *
 * @author  unascribed
 * @version 1.20, 03/23/10
 * @since   JDK1.1
 */
public class InvalidObjectException extends ObjectStreamException {

    private static final long serialVersionUID = 3233174318281839583L;

    /**
     * Constructs an <code>InvalidObjectException</code>.
     * @param reason Detailed message explaining the reason for the failure.
     *
     * @see ObjectInputValidation
     */
    public  InvalidObjectException(String reason) {
	super(reason);
    }
}
