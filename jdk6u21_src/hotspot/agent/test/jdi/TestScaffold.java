/*
 * Copyright (c) 2002, Oracle and/or its affiliates. All rights reserved.
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
import com.sun.jdi.request.*;
import com.sun.jdi.event.*;
import java.util.*;
import java.io.*;

/**
 * Framework used by all JDI regression tests
 */
abstract public class TestScaffold extends TargetAdapter {
    private boolean shouldTrace = false;
    private VMConnection connection;
    private VirtualMachine vm;
    private EventRequestManager requestManager;
    private List listeners = Collections.synchronizedList(new LinkedList());

    /**
     * We create a VMDeathRequest, SUSPEND_ALL, to sync the BE and FE.
     */
    //private VMDeathRequest ourVMDeathRequest = null;
    Object ourVMDeathRequest = null;

    /**
     * We create an ExceptionRequest, SUSPEND_NONE so that we can
     * catch it and output a msg if an exception occurs in the
     * debuggee.
     */
    private ExceptionRequest ourExceptionRequest = null;

    /**
     * If we do catch an uncaught exception, we set this true
     * so the testcase can find out if it wants to.
     */
    private boolean exceptionCaught = false;
    ThreadReference vmStartThread = null;
    boolean vmDied = false;
    boolean vmDisconnected = false;
    final String[] args;
    protected boolean testFailed = false;

    static private class ArgInfo {
        String targetVMArgs = "";
        String targetAppCommandLine = "";
        String connectorSpec = "com.sun.jdi.CommandLineLaunch:";
        int traceFlags = 0;
    }

    /**
     * An easy way to sleep for awhile
     */
    public void mySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ee) {
        }
    }

    boolean getExceptionCaught() {
        return exceptionCaught;
    }

    void setExceptionCaught(boolean value) {
        exceptionCaught = value;
    }

    /**
     * Return true if eventSet contains the VMDeathEvent for the request in
     * the ourVMDeathRequest ivar.
     */
    private boolean containsOurVMDeathRequest(EventSet eventSet) {
        if (ourVMDeathRequest != null) {
            Iterator myIter = eventSet.iterator();
            while (myIter.hasNext()) {
                Event myEvent = (Event)myIter.next();
                if (!(myEvent instanceof VMDeathEvent)) {
                    // We assume that an EventSet contains only VMDeathEvents
                    // or no VMDeathEvents.
                    break;
                }
                if (ourVMDeathRequest.equals(myEvent.request())) {
                    return true;
                }
            }
        }
        return false;
    }

    /************************************************************************
     * The following methods override those in our base class, TargetAdapter.
     *************************************************************************/

    /**
     * Events handled directly by scaffold always resume (well, almost always)
     */
    public void eventSetComplete(EventSet set) {
        // The listener in connect(..) resumes after receiving our
        // special VMDeathEvent.  We can't also do the resume
        // here or we will probably get a VMDisconnectedException
        if (!containsOurVMDeathRequest(set)) {
            traceln("TS: set.resume() called");
            set.resume();
        }
    }

    /**
     * This method sets up default requests.
     * Testcases can override this to change default behavior.
     */
    protected void createDefaultEventRequests() {
        createDefaultVMDeathRequest();
        createDefaultExceptionRequest();
    }

    /**
     * We want the BE to stop when it issues a VMDeathEvent in order to
     * give the FE time to complete handling events that occured before
     * the VMDeath.  When we get the VMDeathEvent for this request in
     * the listener in connect(), we will do a resume.
     * If a testcase wants to do something special with VMDeathEvent's,
     * then it should override this method with an empty method or
     * whatever in order to suppress the automatic resume.  The testcase
     * will then be responsible for the handling of VMDeathEvents.  It
     * has to be sure that it does a resume if it gets a VMDeathEvent
     * with SUSPEND_ALL, and it has to be sure that it doesn't do a
     * resume after getting a VMDeath with SUSPEND_NONE (the automatically
     * generated VMDeathEvent.)
     */
    protected void createDefaultVMDeathRequest() {
//         ourVMDeathRequest = requestManager.createVMDeathRequest();
//         ourVMDeathRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
//         ourVMDeathRequest.enable();
    }

    /**
     * This will allow us to print a warning if a debuggee gets an
     * unexpected exception.  The unexpected exception will be handled in
     * the exceptionThrown method in the listener created in the connect()
     * method.
     * If a testcase does not want an uncaught exception to cause a
     * msg, it must override this method.
     */
    protected void createDefaultExceptionRequest() {
        ourExceptionRequest = requestManager.createExceptionRequest(null,
                                                                false, true);

        // We can't afford to make this be other than SUSPEND_NONE.  Otherwise,
        // it would have to be resumed.  If our connect() listener resumes it,
        // what about the case where the EventSet contains other events with
        // SUSPEND_ALL and there are other listeners who expect the BE to still
        // be suspended when their handlers get called?
        ourExceptionRequest.setSuspendPolicy(EventRequest.SUSPEND_NONE);
        ourExceptionRequest.enable();
    }

    private class EventHandler implements Runnable {
        EventHandler() {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        private void notifyEvent(TargetListener listener, Event event) {
            if (event instanceof BreakpointEvent) {
                listener.breakpointReached((BreakpointEvent)event);
            } else if (event instanceof ExceptionEvent) {
                listener.exceptionThrown((ExceptionEvent)event);
            } else if (event instanceof StepEvent) {
                listener.stepCompleted((StepEvent)event);
            } else if (event instanceof ClassPrepareEvent) {
                listener.classPrepared((ClassPrepareEvent)event);
            } else if (event instanceof ClassUnloadEvent) {
                listener.classUnloaded((ClassUnloadEvent)event);
            } else if (event instanceof MethodEntryEvent) {
                listener.methodEntered((MethodEntryEvent)event);
            } else if (event instanceof MethodExitEvent) {
                listener.methodExited((MethodExitEvent)event);
            } else if (event instanceof AccessWatchpointEvent) {
                listener.fieldAccessed((AccessWatchpointEvent)event);
            } else if (event instanceof ModificationWatchpointEvent) {
                listener.fieldModified((ModificationWatchpointEvent)event);
            } else if (event instanceof ThreadStartEvent) {
                listener.threadStarted((ThreadStartEvent)event);
            } else if (event instanceof ThreadDeathEvent) {
                listener.threadDied((ThreadDeathEvent)event);
            } else if (event instanceof VMStartEvent) {
                listener.vmStarted((VMStartEvent)event);
            } else if (event instanceof VMDeathEvent) {
                listener.vmDied((VMDeathEvent)event);
            } else if (event instanceof VMDisconnectEvent) {
                listener.vmDisconnected((VMDisconnectEvent)event);
            } else {
                throw new InternalError("Unknown event type: " + event.getClass());
            }
        }

        private void traceSuspendPolicy(int policy) {
            if (shouldTrace) {
                switch (policy) {
                case EventRequest.SUSPEND_NONE:
                    traceln("TS: eventHandler: suspend = SUSPEND_NONE");
                    break;
                case EventRequest.SUSPEND_ALL:
                    traceln("TS: eventHandler: suspend = SUSPEND_ALL");
                    break;
                case EventRequest.SUSPEND_EVENT_THREAD:
                    traceln("TS: eventHandler: suspend = SUSPEND_EVENT_THREAD");
                    break;
                }
            }
        }

        public void run() {
            boolean connected = true;
            do {
                try {
                    EventSet set = vm.eventQueue().remove();
                    traceSuspendPolicy(set.suspendPolicy());
                    synchronized (listeners) {
                        ListIterator iter = listeners.listIterator();
                        while (iter.hasNext()) {
                            TargetListener listener = (TargetListener)iter.next();
                            traceln("TS: eventHandler: listener = " + listener);
                            listener.eventSetReceived(set);
                            if (listener.shouldRemoveListener()) {
                                iter.remove();
                            } else {
                                Iterator jter = set.iterator();
                                while (jter.hasNext()) {
                                    Event event = (Event)jter.next();
                                    traceln("TS: eventHandler:    event = " + event.getClass());

                                    if (event instanceof VMDisconnectEvent) {
                                        connected = false;
                                    }
                                    listener.eventReceived(event);
                                    if (listener.shouldRemoveListener()) {
                                        iter.remove();
                                        break;
                                    }
                                    notifyEvent(listener, event);
                                    if (listener.shouldRemoveListener()) {
                                        iter.remove();
                                        break;
                                    }
                                }
                                traceln("TS: eventHandler:   end of events loop");
                                if (!listener.shouldRemoveListener()) {
                                    traceln("TS: eventHandler:   calling ESC");
                                    listener.eventSetComplete(set);
                                    if (listener.shouldRemoveListener()) {
                                        iter.remove();
                                    }
                                }
                            }
                            traceln("TS: eventHandler: end of listeners loop");
                        }
                    }
                } catch (InterruptedException e) {
                    traceln("TS: eventHandler: InterruptedException");
                } catch (Exception e) {
                    failure("FAILED: Exception occured in eventHandler: " + e);
                    e.printStackTrace();
                    connected = false;
                    synchronized(TestScaffold.this) {
                        // This will make the waiters such as waitForVMDisconnect
                        // exit their wait loops.
                        vmDisconnected = true;
                        TestScaffold.this.notifyAll();
                    }
                }
                traceln("TS: eventHandler: End of outerloop");
            } while (connected);
            traceln("TS: eventHandler: finished");
        }
    }

    /**
     * Constructor
     */
    public TestScaffold(String[] args) {
        this.args = args;
    }

    public void enableScaffoldTrace() {
        this.shouldTrace = true;
    }

    public void disableScaffoldTrace() {
        this.shouldTrace = false;
    }


    protected void startUp(String targetName) {
        List argList = new ArrayList(Arrays.asList(args));
        argList.add(targetName);
        println("run args: " + argList);
        connect((String[]) argList.toArray(args));
        waitForVMStart();
    }

    protected BreakpointEvent startToMain(String targetName) {
        startUp(targetName);
        traceln("TS: back from startUp");
        BreakpointEvent bpr = resumeTo(targetName, "main", "([Ljava/lang/String;)V");
        waitForInput();
        return bpr;
    }

    protected void waitForInput() {
        if (System.getProperty("jpda.wait") != null) {
            try {
                System.err.println("Press <enter> to continue");
                System.in.read();
                System.err.println("running...");

            } catch(Exception e) {
            }
        }
    }

    /*
     * Test cases should implement tests in runTests and should
     * initiate testing by calling run().
     */
    abstract protected void runTests() throws Exception;

    final public void startTests() throws Exception {
        try {
            runTests();
        } finally {
            shutdown();
        }
    }

    protected void println(String str) {
        System.err.println(str);
    }

    protected void print(String str) {
        System.err.print(str);
    }

    protected void traceln(String str) {
        if (shouldTrace) {
            println(str);
        }
    }

    protected void failure(String str) {
        println(str);
        testFailed = true;
    }

    private ArgInfo parseArgs(String args[]) {
        ArgInfo argInfo = new ArgInfo();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-connect")) {
                i++;
                argInfo.connectorSpec = args[i];
            } else if (args[i].equals("-trace")) {
                i++;
                argInfo.traceFlags = Integer.decode(args[i]).intValue();
            } else if (args[i].startsWith("-J")) {
                argInfo.targetVMArgs += (args[i].substring(2) + ' ');

                /*
                 * classpath can span two arguments so we need to handle
                 * it specially.
                 */
                if (args[i].equals("-J-classpath")) {
                    i++;
                    argInfo.targetVMArgs += (args[i] + ' ');
                }
            } else {
                argInfo.targetAppCommandLine += (args[i] + ' ');
            }
        }
        return argInfo;
    }

    /**
     * This is called to connect to a debuggee VM.  It starts the VM and
     * installs a listener to catch VMStartEvent, our default events, and
     * VMDisconnectedEvent.  When these events appear, that is remembered
     * and waiters are notified.
     * This is normally called in the main thread of the test case.
     * It starts up an EventHandler thread that gets events coming in
     * from the debuggee and distributes them to listeners.  That thread
     * keeps running until a VMDisconnectedEvent occurs or some exception
     * occurs during its processing.
     *
     * The 'listenUntilVMDisconnect' method adds 'this' as a listener.
     * This means that 'this's vmDied method will get called.  This has a
     * default impl in TargetAdapter.java which can be overridden in the
     * testcase.
     *
     * waitForRequestedEvent also adds an adaptor listener that listens
     * for the particular event it is supposed to wait for (and it also
     * catches VMDisconnectEvents.)  This listener is removed once
     * its eventReceived method is called.
     * waitForRequestedEvent is called by most of the methods to do bkpts,
     * etc.
     */
    public void connect(String args[]) {
        ArgInfo argInfo = parseArgs(args);

        argInfo.targetVMArgs += VMConnection.getDebuggeeVMOptions();
        connection = new VMConnection(argInfo.connectorSpec,
                                      argInfo.traceFlags);

        addListener(new TargetAdapter() {
                public void eventSetComplete(EventSet set) {
                    if (TestScaffold.this.containsOurVMDeathRequest(set)) {
                        traceln("TS: connect: set.resume() called");
                        set.resume();

                        // Note that we want to do the above resume before
                        // waking up any sleepers.
                        synchronized(TestScaffold.this) {
                            TestScaffold.this.notifyAll();
                        }
                    }
                }

                public void vmStarted(VMStartEvent event) {
                    synchronized(TestScaffold.this) {
                        vmStartThread = event.thread();
                        TestScaffold.this.notifyAll();
                    }
                }
                /**
                 * By default, we catch uncaught exceptions and print a msg.
                 * The testcase must override the createDefaultExceptionRequest
                 * method if it doesn't want this behavior.
                 */
                public void exceptionThrown(ExceptionEvent event) {
                    if (TestScaffold.this.ourExceptionRequest != null &&
                        TestScaffold.this.ourExceptionRequest.equals(
                                                        event.request())) {
                        println("Note: Unexpected Debuggee Exception: " +
                                event.exception().referenceType().name() +
                                " at line " + event.location().lineNumber());
                        TestScaffold.this.exceptionCaught = true;
                    }
                }

                public void vmDied(VMDeathEvent event) {
                    vmDied = true;
                    traceln("TS: vmDied called");
                }

                public void vmDisconnected(VMDisconnectEvent event) {
                    synchronized(TestScaffold.this) {
                        vmDisconnected = true;
                        TestScaffold.this.notifyAll();
                    }
                }
            });
        if (connection.connector().name().equals("com.sun.jdi.CommandLineLaunch")) {
            if (argInfo.targetVMArgs.length() > 0) {
                if (connection.connectorArg("options").length() > 0) {
                    throw new IllegalArgumentException("VM options in two places");
                }
                connection.setConnectorArg("options", argInfo.targetVMArgs);
            }
            if (argInfo.targetAppCommandLine.length() > 0) {
                if (connection.connectorArg("main").length() > 0) {
                    throw new IllegalArgumentException("Command line in two places");
                }
                connection.setConnectorArg("main", argInfo.targetAppCommandLine);
            }
        }

        vm = connection.open();
        requestManager = vm.eventRequestManager();
        createDefaultEventRequests();
        new EventHandler();
    }


    public VirtualMachine vm() {
        return vm;
    }

    public EventRequestManager eventRequestManager() {
        return requestManager;
    }

    public void addListener(TargetListener listener) {
        traceln("TS: Adding listener " + listener);
        listeners.add(listener);
    }

    public void removeListener(TargetListener listener) {
        traceln("TS: Removing listener " + listener);
        listeners.remove(listener);
    }


    protected void listenUntilVMDisconnect() {
        try {
            addListener (this);
        } catch (Exception ex){
            ex.printStackTrace();
            testFailed = true;
        } finally {
            // Allow application to complete and shut down
            resumeToVMDisconnect();
        }
    }

    public synchronized ThreadReference waitForVMStart() {
        while ((vmStartThread == null) && !vmDisconnected) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        if (vmStartThread == null) {
            throw new VMDisconnectedException();
        }

        return vmStartThread;
    }

    public synchronized void waitForVMDisconnect() {
        traceln("TS: waitForVMDisconnect");
        while (!vmDisconnected) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        traceln("TS: waitForVMDisconnect: done");
    }

    public Event waitForRequestedEvent(final EventRequest request) {
        class EventNotification {
            Event event;
            boolean disconnected = false;
        }
        final EventNotification en = new EventNotification();

        TargetAdapter adapter = new TargetAdapter() {
            public void eventReceived(Event event) {
                if (request.equals(event.request())) {
                    traceln("TS:Listener2: got requested event");
                    synchronized (en) {
                        en.event = event;
                        en.notifyAll();
                    }
                    removeThisListener();
                } else if (event instanceof VMDisconnectEvent) {
                    traceln("TS:Listener2: got VMDisconnectEvent");
                    synchronized (en) {
                        en.disconnected = true;
                        en.notifyAll();
                    }
                    removeThisListener();
                }
            }
        };

        addListener(adapter);

        try {
            synchronized (en) {
                traceln("TS: waitForRequestedEvent: vm.resume called");
                vm.resume();

                while (!en.disconnected && (en.event == null)) {
                    en.wait();
                }
            }
        } catch (InterruptedException e) {
            return null;
        }

        if (en.disconnected) {
            throw new RuntimeException("VM Disconnected before requested event occurred");
        }
        return en.event;
    }

    private StepEvent doStep(ThreadReference thread, int gran, int depth) {
        final StepRequest sr =
                  requestManager.createStepRequest(thread, gran, depth);

        sr.addClassExclusionFilter("java.*");
        sr.addClassExclusionFilter("sun.*");
        sr.addClassExclusionFilter("com.sun.*");
        sr.addCountFilter(1);
        sr.enable();
        StepEvent retEvent = (StepEvent)waitForRequestedEvent(sr);
        requestManager.deleteEventRequest(sr);
        return retEvent;
    }

    public StepEvent stepIntoInstruction(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_MIN, StepRequest.STEP_INTO);
    }

    public StepEvent stepIntoLine(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
    }

    public StepEvent stepOverInstruction(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_MIN, StepRequest.STEP_OVER);
    }

    public StepEvent stepOverLine(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
    }

    public StepEvent stepOut(ThreadReference thread) {
        return doStep(thread, StepRequest.STEP_LINE, StepRequest.STEP_OUT);
    }

    public BreakpointEvent resumeTo(Location loc) {
        final BreakpointRequest request =
            requestManager.createBreakpointRequest(loc);
        request.addCountFilter(1);
        request.enable();
        return (BreakpointEvent)waitForRequestedEvent(request);
    }

    public ReferenceType findReferenceType(String name) {
        List rts = vm.classesByName(name);
        Iterator iter = rts.iterator();
        while (iter.hasNext()) {
            ReferenceType rt = (ReferenceType)iter.next();
            if (rt.name().equals(name)) {
                return rt;
            }
        }
        return null;
    }

    public Method findMethod(ReferenceType rt, String name, String signature) {
        List methods = rt.methods();
        Iterator iter = methods.iterator();
        while (iter.hasNext()) {
            Method method = (Method)iter.next();
            if (method.name().equals(name) &&
                method.signature().equals(signature)) {
                return method;
            }
        }
        return null;
    }

    public Location findLocation(ReferenceType rt, int lineNumber)
                         throws AbsentInformationException {
        List locs = rt.locationsOfLine(lineNumber);
        if (locs.size() == 0) {
            throw new IllegalArgumentException("Bad line number");
        } else if (locs.size() > 1) {
            throw new IllegalArgumentException("Line number has multiple locations");
        }

        return (Location)locs.get(0);
    }

    public BreakpointEvent resumeTo(String clsName, String methodName,
                                         String methodSignature) {
        ReferenceType rt = findReferenceType(clsName);
        if (rt == null) {
            rt = resumeToPrepareOf(clsName).referenceType();
        }

        Method method = findMethod(rt, methodName, methodSignature);
        if (method == null) {
            throw new IllegalArgumentException("Bad method name/signature");
        }

        return resumeTo(method.location());
    }

    public BreakpointEvent resumeTo(String clsName, int lineNumber) throws AbsentInformationException {
        ReferenceType rt = findReferenceType(clsName);
        if (rt == null) {
            rt = resumeToPrepareOf(clsName).referenceType();
        }

        return resumeTo(findLocation(rt, lineNumber));
    }

    public ClassPrepareEvent resumeToPrepareOf(String className) {
        final ClassPrepareRequest request =
            requestManager.createClassPrepareRequest();
        request.addClassFilter(className);
        request.addCountFilter(1);
        request.enable();
        return (ClassPrepareEvent)waitForRequestedEvent(request);
    }

    public void resumeToVMDisconnect() {
        try {
            traceln("TS: resumeToVMDisconnect: vm.resume called");
            vm.resume();
        } catch (VMDisconnectedException e) {
            // clean up below
        }
        waitForVMDisconnect();
    }

    public void shutdown() {
        shutdown(null);
    }

    public void shutdown(String message) {
        traceln("TS: shutdown: vmDied= " + vmDied +
                 ", vmDisconnected= " + vmDisconnected +
                 ", connection = " + connection);

        if ((connection != null)) {
            try {
                connection.disposeVM();
             } catch (VMDisconnectedException e) {
                // Shutting down after the VM has gone away. This is
                // not an error, and we just ignore it.
            }
        } else {
            traceln("TS: shutdown: disposeVM not called");
        }
        if (message != null) {
            println(message);
        }

        vmDied = true;
        vmDisconnected = true;
    }
}
