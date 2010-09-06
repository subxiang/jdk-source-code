/*
 * @(#)IllegalFormatFlagsException.java	1.2 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.util;

/**
 * Unchecked exception thrown when an illegal combination flags is given.
 *
 * <p> Unless otherwise specified, passing a <tt>null</tt> argument to any
 * method or constructor in this class will cause a {@link
 * NullPointerException} to be thrown.
 *
 * @version 	1.2, 12/19/03
 * @since 1.5
 */
public class IllegalFormatFlagsException extends IllegalFormatException {

    private static final long serialVersionUID = 790824L;

    private String flags;

    /**
     * Constructs an instance of this class with the specified flags.
     *
     * @param  f
     *         The set of format flags which contain an illegal combination
     */
    public IllegalFormatFlagsException(String f) {
 	if (f == null)
 	    throw new NullPointerException();
	this.flags = f;
    }

    /**
     * Returns the set of flags which contains an illegal combination.
     *
     * @return  The flags
     */
    public String getFlags() {
	return flags;
    }

    public String getMessage() {
	return "Flags = '" + flags + "'";
    }
}
