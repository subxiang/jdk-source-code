/*
 * @(#)ContentSignerParameters.java	1.4 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.jarsigner;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.zip.ZipFile;

/**
 * This interface encapsulates the parameters for a ContentSigner object.
 *
 * @since 1.5
 * @version 1.4, 03/23/10
 * @author Vincent Ryan
 */

public interface ContentSignerParameters {

    /**
     * Retrieves the command-line arguments passed to the jarsigner tool.
     *
     * @return The command-line arguments. May be null.
     */
    public String[] getCommandLine();

    /**
     * Retrieves the identifier for a Timestamping Authority (TSA).
     *
     * @return The TSA identifier. May be null.
     */
    public URI getTimestampingAuthority();

    /**
     * Retrieves the certificate for a Timestamping Authority (TSA).
     *
     * @return The TSA certificate. May be null.
     */
    public X509Certificate getTimestampingAuthorityCertificate();

    /**
     * Retrieves the JAR file's signature.
     *
     * @return The non-null array of signature bytes.
     */
    public byte[] getSignature();

    /**
     * Retrieves the name of the signature algorithm.
     *
     * @return The non-null string name of the signature algorithm.
     */
    public String getSignatureAlgorithm();

    /**
     * Retrieves the signer's X.509 certificate chain.
     *
     * @return The non-null array of X.509 public-key certificates.
     */
    public X509Certificate[] getSignerCertificateChain();

    /**
     * Retrieves the content that was signed.
     * The content is the JAR file's signature file.
     *
     * @return The content bytes. May be null.
     */
    public byte[] getContent();

    /**
     * Retrieves the original source ZIP file before it was signed.
     *
     * @return The original ZIP file. May be null.
     */
    public ZipFile getSource();
}
