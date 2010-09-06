/* 
 * @(#)SubjectDelegator.java	1.3 04/05/27
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */ 

package com.sun.jmx.remote.security;

import java.security.AccessController;
import java.security.AccessControlContext;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;

import javax.management.remote.SubjectDelegationPermission;

import com.sun.jmx.remote.util.CacheMap;

public class SubjectDelegator {
    private static final int PRINCIPALS_CACHE_SIZE = 10;
    private static final int ACC_CACHE_SIZE = 10;

    private CacheMap principalsCache;
    private CacheMap accCache;

    /* Return the AccessControlContext appropriate to execute an
       operation on behalf of the delegatedSubject.  If the
       authenticatedAccessControlContext does not have permission to
       delegate to that subject, throw SecurityException.  */
    public synchronized AccessControlContext
	delegatedContext(AccessControlContext authenticatedACC,
			 Subject delegatedSubject)
	    throws SecurityException {

	if (principalsCache == null || accCache == null) {
	    principalsCache = new CacheMap(PRINCIPALS_CACHE_SIZE);
	    accCache = new CacheMap(ACC_CACHE_SIZE);
	}

	// Retrieve the principals for the given
	// delegated subject from the cache
	//
	Principal[] delegatedPrincipals = (Principal[])
	    principalsCache.get(delegatedSubject);

	// Convert the set of principals stored in the
	// delegated subject into an array of principals
	// and store it in the cache
	//
	if (delegatedPrincipals == null) {
	    delegatedPrincipals = (Principal[])
		delegatedSubject.getPrincipals().toArray(new Principal[0]);
	    principalsCache.put(delegatedSubject, delegatedPrincipals);
	}

	// Retrieve the access control context for the
	// given delegated subject from the cache
	//
	AccessControlContext delegatedACC = (AccessControlContext)
	    accCache.get(delegatedSubject);

	// Build the access control context to be used
	// when executing code as the delegated subject
	// and store it in the cache
	//
	if (delegatedACC == null) {
	    delegatedACC =
		JMXSubjectDomainCombiner.getContext(delegatedSubject);
	    accCache.put(delegatedSubject, delegatedACC);
	}

	// Check if the subject delegation permission allows the
	// authenticated subject to assume the identity of each
	// principal in the delegated subject
	//
	final Principal[] dp = delegatedPrincipals;
	PrivilegedAction action =
	    new PrivilegedAction() {
		public Object run() {
		    for (int i = 0 ; i < dp.length ; i++) {
			final String pname =
			    dp[i].getClass().getName() + "." + dp[i].getName();
			Permission sdp =
			    new SubjectDelegationPermission(pname);
			AccessController.checkPermission(sdp);
		    }
		    return null;
		}
	    };
	AccessController.doPrivileged(action, authenticatedACC);

	return delegatedACC;
    }
}
