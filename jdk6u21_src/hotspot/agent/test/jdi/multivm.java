/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

import com.sun.jdi.*;
import com.sun.jdi.connect.*;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;

/* This class is used to test multi VM connectivity feature of
 * SA/JDI. Accepts two PIDs as arguments. Connects to first VM
 *, Connects to second VM and disposes them in that order.
 */

public class multivm {
    static AttachingConnector myPIDConn;
    static VirtualMachine vm1;
    static VirtualMachine vm2;
    static VirtualMachineManager vmmgr;

    public static void println(String msg) {
        System.out.println(msg);
    }

    private static void usage() {
        System.err.println("Usage: java multivm <pid1> <pid2>");
        System.exit(1);
    }

    public static void main(String args[]) {
        vmmgr = Bootstrap.virtualMachineManager();
        List attachingConnectors = vmmgr.attachingConnectors();
        if (attachingConnectors.isEmpty()) {
            System.err.println( "ERROR: No attaching connectors");
            return;
        }
        Iterator myIt = attachingConnectors.iterator();
        while (myIt.hasNext()) {
            AttachingConnector tmpCon = (AttachingConnector)myIt.next();
            if (tmpCon.name().equals(
                "sun.jvm.hotspot.jdi.SAPIDAttachingConnector")) {
                myPIDConn = tmpCon;
                break;
            }
        }

        int pid1 = 0, pid2 = 0;
        String pidText = null;
        switch (args.length) {
        case (2):
            try {
                pidText = args[0];
                pid1 = Integer.parseInt(pidText);
                System.out.println( "pid1: " + pid1);
                vm1 = attachPID(pid1);
                pidText = args[1];
                pid2 = Integer.parseInt(pidText);
                System.out.println( "pid2: " + pid2);
                vm2 = attachPID(pid2);
            } catch (NumberFormatException e) {
                println(e.getMessage());
                usage();
            }
            break;
        default:
            usage();
        }

        if (vm1 != null) {
            System.out.println("vm1: attached ok!");
            System.out.println(vm1.version());
            sagdoit mine = new sagdoit(vm1);
            mine.doAll();
        }

        if (vm2 != null) {
            System.out.println("vm2: attached ok!");
            System.out.println(vm2.version());
            sagdoit mine = new sagdoit(vm2);
            mine.doAll();
        }

        if (vm1 != null) {
            vm1.dispose();
        }

        if (vm2 != null) {
            vm2.dispose();
        }
    }

   private static VirtualMachine attachPID(int pid) {
        Map connArgs = myPIDConn.defaultArguments();
        System.out.println("connArgs = " + connArgs);
        VirtualMachine vm;
        Connector.StringArgument connArg = (Connector.StringArgument)connArgs.get("pid");
        connArg.setValue(Integer.toString(pid));

        try {
            vm = myPIDConn.attach(connArgs);
        } catch (IOException ee) {
            System.err.println("ERROR: myPIDConn.attach got IO Exception:" + ee);
            vm = null;
        } catch (IllegalConnectorArgumentsException ee) {
            System.err.println("ERROR: myPIDConn.attach got illegal args exception:" + ee);
            vm = null;
        }
        return vm;
   }
}
