/*
 * @(#)EnumJvmMemPoolType.java	1.8 04/07/26
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.management.snmp.jvmmib;

//
// Generated by mibgen version 5.0 (06/02/03) when compiling JVM-MANAGEMENT-MIB.
//

// java imports
//
import java.io.Serializable;
import java.util.Hashtable;

// RI imports
//
import com.sun.jmx.snmp.Enumerated;

/**
 * The class is used for representing "JvmMemPoolType".
 */
public class EnumJvmMemPoolType extends Enumerated implements Serializable {

    protected static Hashtable intTable = new Hashtable();
    protected static Hashtable stringTable = new Hashtable();
    static  {
        intTable.put(new Integer(2), "heap");
        intTable.put(new Integer(1), "nonheap");
        stringTable.put("heap", new Integer(2));
        stringTable.put("nonheap", new Integer(1));
    }

    public EnumJvmMemPoolType(int valueIndex) throws IllegalArgumentException {
        super(valueIndex);
    }

    public EnumJvmMemPoolType(Integer valueIndex) throws IllegalArgumentException {
        super(valueIndex);
    }

    public EnumJvmMemPoolType() throws IllegalArgumentException {
        super();
    }

    public EnumJvmMemPoolType(String x) throws IllegalArgumentException {
        super(x);
    }

    protected Hashtable getIntTable() {
        return intTable ;
    }

    protected Hashtable getStringTable() {
        return stringTable ;
    }

}
