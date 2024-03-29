/*
 * @(#)IssuerAlternativeNameExtension.java	1.23 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import sun.security.util.*;

/**
 * This represents the Issuer Alternative Name Extension.
 *
 * This extension, if present, allows the issuer to specify multiple
 * alternative names.
 *
 * <p>Extensions are represented as a sequence of the extension identifier
 * (Object Identifier), a boolean flag stating whether the extension is to
 * be treated as being critical and the extension value itself (this is again
 * a DER encoding of the extension value).
 *
 * @author Amit Kapoor
 * @author Hemma Prafullchandra
 * @version 1.23
 * @see Extension
 * @see CertAttrSet
 */
public class IssuerAlternativeNameExtension
extends Extension implements CertAttrSet {
    /**
     * Identifier for this attribute, to be used with the
     * get, set, delete methods of Certificate, x509 type.
     */
    public static final String IDENT =
                         "x509.info.extensions.IssuerAlternativeName";
    /**
     * Attribute names.
     */
    public static final String NAME = "IssuerAlternativeName";
    public static final String ISSUER_NAME = "issuer_name";

    // private data members
    GeneralNames names = null;

    // Encode this extension
    private void encodeThis() throws IOException {
        if (names == null || names.isEmpty()) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream os = new DerOutputStream();
	names.encode(os);
        this.extensionValue = os.toByteArray();
    }

    /**
     * Create a IssuerAlternativeNameExtension with the passed GeneralNames.
     *
     * @param names the GeneralNames for the issuer.
     * @exception IOException on error.
     */
    public IssuerAlternativeNameExtension(GeneralNames names)
    throws IOException {
        this.names = names;
        this.extensionId = PKIXExtensions.IssuerAlternativeName_Id;
        this.critical = false;
        encodeThis();
    }

    /**
     * Create a default IssuerAlternativeNameExtension.
     */
    public IssuerAlternativeNameExtension() {
        extensionId = PKIXExtensions.IssuerAlternativeName_Id;
        critical = false;
        names = new GeneralNames();
    }

    /**
     * Create the extension from the passed DER encoded value.
     *
     * @param critical true if the extension is to be treated as critical.
     * @param value an array of DER encoded bytes of the actual value.
     * @exception ClassCastException if value is not an array of bytes
     * @exception IOException on error.
     */
    public IssuerAlternativeNameExtension(Boolean critical, Object value)
    throws IOException {
        this.extensionId = PKIXExtensions.IssuerAlternativeName_Id;
        this.critical = critical.booleanValue();
        this.extensionValue = (byte[]) value;
        DerValue val = new DerValue(this.extensionValue);
	if (val.data == null) {
	    names = new GeneralNames();
	    return;
	}

	names = new GeneralNames(val);
    }

    /**
     * Returns a printable representation of the IssuerAlternativeName.
     */
    public String toString() {

        String result = super.toString() + "IssuerAlternativeName [\n";
        if(names == null) {
            result += "  null\n";
        } else {
            for(GeneralName name: names.names()) {
                result += "  "+name+"\n";
            }
        }
        result += "]\n";
        return result;
    }

    /**
     * Write the extension to the OutputStream.
     *
     * @param out the OutputStream to write the extension to.
     * @exception IOException on encoding error.
     */
    public void encode(OutputStream out) throws IOException {
        DerOutputStream tmp = new DerOutputStream();
	if (extensionValue == null) {
            extensionId = PKIXExtensions.IssuerAlternativeName_Id;
	    critical = false;
	    encodeThis();
	}
	super.encode(tmp);
	out.write(tmp.toByteArray());
    }

    /**
     * Set the attribute value.
     */
    public void set(String name, Object obj) throws IOException {
	if (name.equalsIgnoreCase(ISSUER_NAME)) {
	    if (!(obj instanceof GeneralNames)) {
	      throw new IOException("Attribute value should be of" +
                                    " type GeneralNames.");
	    }
	    names = (GeneralNames)obj;
	} else {
	  throw new IOException("Attribute name not recognized by " +
			"CertAttrSet:IssuerAlternativeName.");
	}
        encodeThis();
    }

    /**
     * Get the attribute value.
     */
    public Object get(String name) throws IOException {
	if (name.equalsIgnoreCase(ISSUER_NAME)) {
	    return (names);
	} else {
	  throw new IOException("Attribute name not recognized by " +
			"CertAttrSet:IssuerAlternativeName.");
	}
    }

    /**
     * Delete the attribute value.
     */
    public void delete(String name) throws IOException {
	if (name.equalsIgnoreCase(ISSUER_NAME)) {
	    names = null;
	} else {
	  throw new IOException("Attribute name not recognized by " +
			"CertAttrSet:IssuerAlternativeName.");
	}
        encodeThis();
    }

    /**
     * Return an enumeration of names of attributes existing within this
     * attribute.
     */
    public Enumeration getElements() {
        AttributeNameEnumeration elements = new AttributeNameEnumeration();
        elements.addElement(ISSUER_NAME);

	return (elements.elements());
    }

    /**
     * Return the name of this attribute.
     */
    public String getName() {
        return (NAME);
    }
}
