/*
 * @(#)MeteredStream.java	1.46 04/03/01
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.net.www;

import java.net.URL;
import java.util.*;
import java.io.*;
import sun.net.ProgressSource;
import sun.net.www.http.ChunkedInputStream;


public class MeteredStream extends FilterInputStream {

    // Instance variables.
    /* if expected != -1, after we've read >= expected, we're "closed" and return -1
     * from subsequest read() 's
     */
    protected boolean closed = false;
    protected int expected;
    protected int count = 0; 
    protected int markedCount = 0; 
    protected int markLimit = -1;
    protected ProgressSource pi;

    public MeteredStream(InputStream is, ProgressSource pi, int expected) 
    {
	super(is);
	
	this.pi = pi;
	this.expected = expected;
	
	if (pi != null)	{
	    pi.updateProgress(0, expected);
	}
    }
    
    private final void justRead(int n) throws IOException   {
	if (n == -1) {

	    /*
	     * don't close automatically when mark is set and is valid;
	     * cannot reset() after close()
	     */
	    if (!isMarked()) {
	        close();
	    }
	    return;
	}

	count += n;

	/**
	 * If read beyond the markLimit, invalidate the mark
	 */
	if (count - markedCount > markLimit) {
	    markLimit = -1;
	}

	if (pi != null)
	    pi.updateProgress(count, expected);

	if (isMarked()) {
	    return;
	}

	// if expected length is known, we could determine if 
	// read overrun.
	if (expected > 0)   {
	    if (count >= expected) {
		close();
	    }
	}
    }

    /**
     * Returns true if the mark is valid, false otherwise
     */
    private boolean isMarked() {

	if (markLimit < 0) {
	    return false;
	}

	// mark is set, but is not valid anymore
	if (count - markedCount > markLimit) {
	   return false; 
	}

	// mark still holds
	return true;
    }

    public synchronized int read() throws java.io.IOException {
	if (closed) {
	    return -1;
	}
	int c = in.read();
	if (c != -1) {
	    justRead(1);
	} else {
	    justRead(c);
	}
	return c;
    }

    public synchronized int read(byte b[], int off, int len)
		throws java.io.IOException {
	if (closed) {
	    return -1;
	}
	int n = in.read(b, off, len);
	justRead(n);
	return n;
    }
    
    public synchronized long skip(long n) throws IOException {

	// REMIND: what does skip do on EOF????
	if (closed) {
	    return 0;
	}
		    
	if (in instanceof ChunkedInputStream) {
	    n = in.skip(n);
	} 
	else {
	    // just skip min(n, num_bytes_left)
	    int min = (n > expected - count) ? expected - count: (int)n;
	    n = in.skip(min);
	}
	justRead((int)n);
	return n;
    }

    public void close() throws IOException {
	if (closed) {
	    return;
	}
	if (pi != null)
	    pi.finishTracking();
	
	closed = true;
	in.close();
    }

    public synchronized int available() throws IOException {
	return closed ? 0: in.available();
    }

    public synchronized void mark(int readLimit) {
	if (closed) {
	    return;
	}
	super.mark(readLimit);

	/*
	 * mark the count to restore upon reset 
	 */
	markedCount = count;	
	markLimit = readLimit;
    }

    public synchronized void reset() throws IOException {
	if (closed) {
	    return;
	}

	if (!isMarked()) {
	    throw new IOException ("Resetting to an invalid mark");
	}

	count = markedCount;
	super.reset();
    }

    public boolean markSupported() {
	if (closed) {
	    return false;
	}
	return super.markSupported();
    }

    protected void finalize() throws Throwable {
	try {
	    close();
	    if (pi != null)
		pi.close();
	}
	finally	{
	    // Call super class
	    super.finalize();
	} 
    }	
}

