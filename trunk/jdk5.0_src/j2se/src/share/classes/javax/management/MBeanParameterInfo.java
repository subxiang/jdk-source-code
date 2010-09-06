/*
 * @(#)MBeanParameterInfo.java	1.24 03/12/19
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.management;


/**
 * Describes an argument of an operation exposed by an MBean.
 * Instances of this class are immutable.  Subclasses may be mutable
 * but this is not recommended.
 *
 * @since 1.5
 */
public class MBeanParameterInfo extends MBeanFeatureInfo implements java.io.Serializable, Cloneable  { 

    /* Serial version */
    static final long serialVersionUID = 7432616882776782338L;

    /* All zero-length arrays are interchangeable. */
    static final MBeanParameterInfo[] NO_PARAMS = new MBeanParameterInfo[0];

    /**
     * @serial The type or class name of the data.  
     */
    private final String type;
     
   
    /**
     * Constructs a <CODE>MBeanParameterInfo</CODE> object.
     *
     * @param name The name of the data
     * @param type The type or class name of the data
     * @param description A human readable description of the data. Optional.
     */
    public MBeanParameterInfo(String name,
			      String type,
			      String description)
	    throws IllegalArgumentException {
	
	super(name, description);

	this.type = type;
    }
    

    /**
     * <p>Returns a shallow clone of this instance. 
     * The clone is obtained by simply calling <tt>super.clone()</tt>, 
     * thus calling the default native shallow cloning mechanism 
     * implemented by <tt>Object.clone()</tt>.
     * No deeper cloning of any internal field is made.</p>
     *
     * <p>Since this class is immutable, cloning is chiefly of
     * interest to subclasses.</p>
     */
     public Object clone () {
	 try {
	     return  super.clone() ;
	 } catch (CloneNotSupportedException e) {
	     // should not happen as this class is cloneable
	     return null;
	 }
     }

    /**
     * Returns the type or class name of the data.  
     *
     * @return the type string.
     */
    public String getType() {
	return type;
    }

    /**
     * Compare this MBeanParameterInfo to another.
     *
     * @param o the object to compare to.
     *
     * @return true iff <code>o</code> is an MBeanParameterInfo such
     * that its {@link #getName()}, {@link #getType()}, and {@link
     * #getDescription()} values are equal (not necessarily identical)
     * to those of this MBeanParameterInfo.
     */
    public boolean equals(Object o) {
	if (o == this)
	    return true;
	if (!(o instanceof MBeanParameterInfo))
	    return false;
	MBeanParameterInfo p = (MBeanParameterInfo) o;
	return (p.getName().equals(getName()) &&
		p.getType().equals(getType()) &&
		p.getDescription().equals(getDescription()));
    }

    public int hashCode() {
	return getName().hashCode() ^ getType().hashCode();
    }
}
