/*
 * @(#)XTextField.java	1.5 04/06/14
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sun.tools.jconsole.inspector;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import javax.swing.plaf.*;
import javax.swing.event.*;
import javax.swing.*;


/**
 * This list implements the drag and drop functionality.
 */
public class XTextField extends JPanel 
    implements DocumentListener,
	       ActionListener {
	
    private static final Color selF = Color.red;
    private static final Color selB = Color.yellow;
    private Color fore=null, back=null;
    private HashMap items = null; //used for popup menu selection
    private XObject selectedObject;
    private XObject currentObject;
    private Class expectedClass;
    private Object value;
    protected JTextField textField;
    private JButton browseObjects;

    private static boolean allowNullSelection = false; 

    protected final static int COMPATIBLE_VALUE = 1;
    protected final static int CURRENT_VALUE = 2;
    protected final static int NULL_VALUE = 3;

    private JButton button;
    private XOperations operation;
    
    //used in XTestFieldEditor
    public XTextField() {
	super(new BorderLayout());
	add(textField = new JTextField(),BorderLayout.CENTER);
	textField.addActionListener(this);
	//
    }
	
    public XTextField(Object value) {
	this(value,value.toString().length());
    }
	
    public XTextField(Object value, int colWidth) {
	this(value,value.getClass(),colWidth, true, null, null);
    }
    
    public XTextField(Object value, 
		      Class expectedClass, 
		      int colWidth,
		      boolean isCallable,
		      JButton button,
		      XOperations operation) {
	super(new BorderLayout());
	this.expectedClass = expectedClass;
	this.button = button;
	this.operation = operation;
	add(textField = new JTextField(value.toString(),colWidth),
	    BorderLayout.CENTER);
	if(isCallable)
	    textField.addActionListener(this);
	
	boolean fieldEditable = Utils.isEditableType(expectedClass.getName());
	if (fieldEditable && isCallable) {
	    textField.setEditable(true);
	}
	else {
	    textField.setEditable(false);
	}
    }
    
    public static void setNullSelectionAllowed(boolean allowNullSelection) {
	XTextField.allowNullSelection = allowNullSelection;
    }
    
    public static boolean getNullSelectionAllowed() {
	return allowNullSelection;
    }

    protected void init(Object value, Class expectedClass) {
	this.expectedClass = expectedClass;
	this.value = value;
	boolean fieldEditable =  Utils.isEditableType(expectedClass.getName());
	clearObject();
	if (value != null) {
	    currentObject = new XObject(value);
	    textField.setText(value.toString());
	}
	else {
	    currentObject = XObject.NULL_OBJECT;
	    //null String value for the moment
	    textField.setText("");
	}
	textField.setToolTipText(null);
	if (fieldEditable) {
	    if (!textField.isEditable()) {
		textField.setEditable(true);
	    }
	 	    
	}	
	else {
	    if (textField.isEditable()) {
		textField.setEditable(false);
	    }
	}
    }
	
	

      
	
    private synchronized void setObject(XObject object) {
	clearObject();
	selectedObject = object;
	currentObject = object;
	setSelectedColors();
	textField.setText(object.getText());
	textField.getDocument().addDocumentListener(this); 
	paintImmediately(getVisibleRect());
    }
	
    private synchronized void clearObject() {
	textField.getDocument().removeDocumentListener(this);
	selectedObject = null;
	currentObject = null;
	setDefaultColors();
    }
	
    private synchronized void setSelectedColors() {
	// fore = textField.getForeground();
	// back = textField.getBackground();

	//textField.setForeground(Color.red);
	// textField.setBackground(Color.yellow);
    }
	
    private synchronized void setDefaultColors() {
	//  if (fore != null) textField.setForeground(fore);
	// if (back != null)  textField.setBackground(back);
    }
	
    public void setHorizontalAlignment(int h) {
	textField.setHorizontalAlignment(h);
    }

    //can be overwritten
    protected JMenuItem buildJMenuItem(XObject xobject, int valueType) {
	if (valueType == COMPATIBLE_VALUE) {
	    return new JMenuItem(xobject.getText());
	}
	else if (valueType == CURRENT_VALUE) {
	    return new JMenuItem("> "+xobject.getText());
	}
	else if (valueType == NULL_VALUE) {
	    return new JMenuItem("null");
	}
	else {
	    return null;
	}
    }
    
    private JPopupMenu buildEditPopupMenu() {
	JPopupMenu menu = new JPopupMenu();
	return menu;
    }
	
	
    // ACTIONLISTENER IMPLEMENTATION
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() instanceof JTextField) {
	    if(operation != null)
		operation.performInvokeRequest(button);
	}
    }
                     	
    /**
     * This method returns either the user inputted String, or an XObject
     * if one was dropped on the input field.
     */
    public Object getValue() {
	if (selectedObject!=null) {
	    if (selectedObject == XObject.NULL_OBJECT) {
		//null case
		return null;
	    }
	    else {
		return selectedObject;
	    }
	}
	else {
	    return  textField.getText();
	}
    }
                         
    public void changedUpdate(DocumentEvent e) {
	// the user typed something, so remove references
	// to the obejct that was dropped.
	clearObject();
    }
	
    public void removeUpdate(DocumentEvent e) {
	// the user typed something, so remove references
	// to the obejct that was dropped.
	clearObject();
    }
	
    public void insertUpdate(DocumentEvent e) {
	// the user typed something, so remove references
	// to the obejct that was dropped.
	clearObject();
    }
    
}

