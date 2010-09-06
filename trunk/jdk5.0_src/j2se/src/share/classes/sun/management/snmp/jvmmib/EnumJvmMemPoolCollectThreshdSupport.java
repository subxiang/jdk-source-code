/*
 * @(#)EnumJvmMemPoolCollectThreshdSupport.java	1.3 04/07/26
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
 * The class is used for representing "JvmMemPoolCollectThreshdSupport".
 */
public class EnumJvmMemPoolCollectThreshdSupport extends Enumerated implements Serializable {

    protected static Hashtable intTable = new Hashtable();
    protected static Hashtable stringTable = new Hashtable();
    static  {
        intTable.put(new Integer(2), "supported");
        intTable.put(new Integer(1), "unsupported");
        stringTable.put("supported", new Integer(2));
        stringTable.put("unsupported", new Integer(1));
    }

    public EnumJvmMemPoolCollectThreshdSupport(int valueIndex) throws IllegalArgumentException {
        super(valueIndex);
    }

    public EnumJvmMemPoolCollectThreshdSupport(Integer valueIndex) throws IllegalArgumentException {
        super(valueIndex);
    }

    public EnumJvmMemPoolCollectThreshdSupport() throws IllegalArgumentException {
        super();
    }

    public EnumJvmMemPoolCollectThreshdSupport(String x) throws IllegalArgumentException {
        super(x);
    }

    protected Hashtable getIntTable() {
        return intTable ;
    }

    protected Hashtable getStringTable() {
        return stringTable ;
    }

}
