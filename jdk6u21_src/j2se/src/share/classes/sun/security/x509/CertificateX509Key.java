/*
 * @(#)CertificateX509Key.java	1.16 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.security.x509;

import java.security.PublicKey;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import sun.security.util.*;

/**
 * This class defines the X509Key attribute for the Certificate.
 *
 * @author Amit Kapoor
 * @author Hemma Prafullchandra
 * @version 1.16
 * @see CertAttrSet
 */
public class CertificateX509Key implements CertAttrSet {
    /**
     * Identifier for this attribute, to be used with the
     * get, set, delete methods of Certificate, x509 type.
     */  
    public static final String IDENT = "x509.info.key";
    /**
     * Sub attributes name for this CertAttrSet.
     */
    public static final String NAME = "key";
    public static final String KEY = "value";

    // Private data member
    private PublicKey key;

    /**
     * Default constructor for the certificate attribute.
     *
     * @param key the X509Key
     */
    public CertificateX509Key(PublicKey key) {
        this.key = key;
    }

    /**
     * Create the object, decoding the values from the passed DER stream.
     *
     * @param in the DerInputStream to read the X509Key from.
     * @exception IOException on decoding errors.
     */
    public CertificateX509Key(DerInputStream in) throws IOException {
        DerValue val = in.getDerValue();
        key = X509Key.parse(val);
    }

    /**
     * Create the object, decoding the values from the passed stream.
     *
     * @param in the InputStream to read the X509Key from.
     * @exception IOException on decoding errors.
     */
    public CertificateX509Key(InputStream in) throws IOException {
        DerValue val = new DerValue(in);
        key = X509Key.parse(val);
    }

    /**
     * Return the key as printable string.
     */
    public String toString() {
        if (key == null) return "";
        return(key.toString());
    }

    /**
     * Encode the key in DER form to the stream.
     *
     * @param out the OutputStream to marshal the contents to.
     * @exception IOException on errors.
     */
    public void encode(OutputStream out) throws IOException {
        DerOutputStream tmp = new DerOutputStream();
        tmp.write(key.getEncoded());

        out.write(tmp.toByteArray());
    }

    /**
     * Set the attribute value.
     */
    public void set(String name, Object obj) throws IOException {
        if (name.equalsIgnoreCase(KEY)) {
            this.key = (PublicKey)obj;
        } else {
            throw new IOException("Attribute name not recognized by " +
                                  "CertAttrSet: CertificateX509Key.");
        }
    }

    /**
     * Get the attribute value.
     */
    public Object get(String name) throws IOException {
        if (name.equalsIgnoreCase(KEY)) {
            return(key);
        } else {
            throw new IOException("Attribute name not recognized by " +
                                  "CertAttrSet: CertificateX509Key.");
        }
    }

    /**
     * Delete the attribute value.
     */
    public void delete(String name) throws IOException {
      if (name.equalsIgnoreCase(KEY)) {
        key = null;
      } else {
            throw new IOException("Attribute name not recognized by " +
                                  "CertAttrSet: CertificateX509Key.");
      }
    }

    /**
     * Return an enumeration of names of attributes existing within this
     * attribute.
     */
    public Enumeration getElements() {
        AttributeNameEnumeration elements = new AttributeNameEnumeration();
        elements.addElement(KEY);

        return(elements.elements());
    }

    /**
     * Return the name of this attribute.
     */
    public String getName() {
        return(NAME);
    }
}
