/*
 * @(#)rmiURLContext.java	1.9 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.jndi.url.rmi;

import java.util.Hashtable;
import java.rmi.registry.LocateRegistry;

import javax.naming.*;
import javax.naming.spi.ResolveResult;
import com.sun.jndi.toolkit.url.GenericURLContext;
import com.sun.jndi.rmi.registry.RegistryContext;


/**
 * An RMI URL context resolves names that are URLs of the form
 * <pre>
 *   rmi://[host][:port][/[object]]
 * or
 *   rmi:[/][object]
 * </pre>
 * If an object is specified, the URL resolves to the named object.
 * Otherwise, the URL resolves to the specified RMI registry.
 *
 * @author Scott Seligman
 * @version 1.9 10/03/23
 */
public class rmiURLContext extends GenericURLContext {

    public rmiURLContext(Hashtable env) {
	super(env);
    }

    /**
     * Resolves the registry portion of "url" to the corresponding
     * RMI registry, and returns the atomic object name as the
     * remaining name.
     */
    protected ResolveResult getRootURLContext(String url, Hashtable env)
	    throws NamingException
    {
	if (!url.startsWith("rmi:")) {
	    throw (new IllegalArgumentException(
		    "rmiURLContext: name is not an RMI URL: " + url));
	}

	// Parse the URL.

	String host = null;
	int port = -1;
	String objName = null;

	int i = 4;		// index into url, following the "rmi:"

	if (url.startsWith("//", i)) {		// parse "//host:port"
	    i += 2;				// skip past "//"
	    int slash = url.indexOf('/', i);
	    if (slash < 0) {
		slash = url.length();
	    }
	    if (url.startsWith("[", i)) {               // at IPv6 literal
                int brac = url.indexOf(']', i + 1);
                if (brac < 0 || brac > slash) {
                    throw new IllegalArgumentException(
			"rmiURLContext: name is an Invalid URL: " + url);
                }
                host = url.substring(i, brac + 1);      // include brackets
                i = brac + 1;                           // skip past "[...]"
	    } else {                                    // at host name or IPv4
                int colon = url.indexOf(':', i);
                int hostEnd = (colon < 0 || colon > slash)
                    ? slash
                    : colon;
                if (i < hostEnd) {
                    host = url.substring(i, hostEnd);
                }
                i = hostEnd;                            // skip past host
            }
            if ((i + 1 < slash)) {
		if ( url.startsWith(":", i)) {       // parse port
                    i++;                             // skip past ":"
                    port = Integer.parseInt(url.substring(i, slash));
		} else { 
                    throw new IllegalArgumentException(
			"rmiURLContext: name is an Invalid URL: " + url);
		}
            }
	    i = slash;
	}
	if ("".equals(host)) {
	    host = null;
	}
	if (url.startsWith("/", i)) {		// skip "/" before object name
	    i++;
	}
	if (i < url.length()) {
	    objName = url.substring(i);
	}

	// Represent object name as empty or single-component composite name.
	CompositeName remaining = new CompositeName();
	if (objName != null) {
	    remaining.add(objName);
	}

	// Debug
	//System.out.println("host=" + host + " port=" + port +
	//		   " objName=" + remaining.toString() + "\n");

	// Create a registry context.
	Context regCtx = new RegistryContext(host, port, env);

	return (new ResolveResult(regCtx, remaining));
    }
}
