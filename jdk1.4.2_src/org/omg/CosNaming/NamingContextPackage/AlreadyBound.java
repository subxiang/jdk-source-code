package org.omg.CosNaming.NamingContextPackage;


/**
* org/omg/CosNaming/NamingContextPackage/AlreadyBound.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from ../../../../src/share/classes/org/omg/CosNaming/nameservice.idl
* Friday, June 20, 2003 8:34:40 AM GMT
*/

public final class AlreadyBound extends org.omg.CORBA.UserException
{

  public AlreadyBound ()
  {
    super(AlreadyBoundHelper.id());
  } // ctor


  public AlreadyBound (String $reason)
  {
    super(AlreadyBoundHelper.id() + "  " + $reason);
  } // ctor

} // class AlreadyBound