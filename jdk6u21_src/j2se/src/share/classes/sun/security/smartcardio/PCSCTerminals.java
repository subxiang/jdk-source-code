/*
 * @(#)PCSCTerminals.java	1.6 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.security.smartcardio;

import java.util.*;
import java.lang.ref.*;

import javax.smartcardio.*;
import static javax.smartcardio.CardTerminals.State.*;

import static sun.security.smartcardio.PCSC.*;

/**
 * TerminalFactorySpi implementation class.
 *
 * @since   1.6
 * @version 1.6, 03/23/10
 * @author  Andreas Sterbenz
 */
final class PCSCTerminals extends CardTerminals {

    // SCARDCONTEXT, currently shared between all threads/terminals
    private static long contextId;

    // terminal state used by waitForCard()
    private Map<String,ReaderState> stateMap;

    PCSCTerminals() {
	// empty
    }
    
    static synchronized void initContext() throws PCSCException {
	if (contextId == 0) {
	    contextId = SCardEstablishContext(SCARD_SCOPE_USER);
	}
    }
    
    private static final Map<String,Reference<TerminalImpl>> terminals
	= new HashMap<String,Reference<TerminalImpl>>();

    private static synchronized TerminalImpl implGetTerminal(String name) {
	Reference<TerminalImpl> ref = terminals.get(name);
	TerminalImpl terminal = (ref != null) ? ref.get() : null;
	if (terminal != null) {
	    return terminal;
	}
	terminal = new TerminalImpl(contextId, name);
	terminals.put(name, new WeakReference<TerminalImpl>(terminal));
	return terminal;
	
    }

    public synchronized List<CardTerminal> list(State state) throws CardException {
	if (state == null) {
	    throw new NullPointerException();
	}
	try {
	    String[] readerNames = SCardListReaders(contextId);
	    List<CardTerminal> list = new ArrayList<CardTerminal>(readerNames.length);
	    if (stateMap == null) {
		// If waitForChange() has never been called, treat event
		// queries as status queries.
		if (state == CARD_INSERTION) {
		    state = CARD_PRESENT;
		} else if (state == CARD_REMOVAL) {
		    state = CARD_ABSENT;
		}
	    }
	    for (String readerName : readerNames) {
		CardTerminal terminal = implGetTerminal(readerName);
		ReaderState readerState;
		switch (state) {
		case ALL:
		    list.add(terminal);
		    break;
		case CARD_PRESENT:
		    if (terminal.isCardPresent()) {
			list.add(terminal);
		    }
		    break;
		case CARD_ABSENT:
		    if (terminal.isCardPresent() == false) {
			list.add(terminal);
		    }
		    break;
		case CARD_INSERTION:
		    readerState = stateMap.get(readerName);
		    if ((readerState != null) && readerState.isInsertion()) {
			list.add(terminal);
		    }
		    break;
		case CARD_REMOVAL:
		    readerState = stateMap.get(readerName);
		    if ((readerState != null) && readerState.isRemoval()) {
			list.add(terminal);
		    }
		    break;
		default:
		    throw new CardException("Unknown state: " + state);
		}
	    }
	    return Collections.unmodifiableList(list);
	} catch (PCSCException e) {
	    throw new CardException("list() failed", e);
	}
    }
    
    private static class ReaderState {
	private int current, previous;
	ReaderState() {
	    current = SCARD_STATE_UNAWARE;
	    previous = SCARD_STATE_UNAWARE;
	}
	int get() {
	    return current;
	}
	void update(int newState) {
	    previous = current;
	    current = newState;
	}
	boolean isInsertion() {
	    return !present(previous) && present(current);
	}
	boolean isRemoval() {
	    return present(previous) && !present(current);
	}
	static boolean present(int state) {
	    return (state & SCARD_STATE_PRESENT) != 0;
	}
    }
    
    public synchronized boolean waitForChange(long timeout) throws CardException {
	if (timeout < 0) {
	    throw new IllegalArgumentException
		("Timeout must not be negative: " + timeout);
	}
	if (stateMap == null) {
	    // We need to initialize the state database.
	    // Do that with a recursive call, which will return immediately
	    // because we pass SCARD_STATE_UNAWARE.
	    // After that, proceed with the real call.
	    stateMap = new HashMap<String,ReaderState>();
	    waitForChange(0);
	}
	if (timeout == 0) {
	    timeout = TIMEOUT_INFINITE;
	}
	try {
	    String[] readerNames = SCardListReaders(contextId);
	    int n = readerNames.length;
	    if (n == 0) {
		throw new IllegalStateException("No terminals available");
	    }
	    int[] status = new int[n];
	    ReaderState[] readerStates = new ReaderState[n];
	    for (int i = 0; i < readerNames.length; i++) {
		String name = readerNames[i];
		ReaderState state = stateMap.get(name);
		if (state == null) {
		    state = new ReaderState();
		}
		readerStates[i] = state;
		status[i] = state.get();
	    }
	    status = SCardGetStatusChange(contextId, timeout, status, readerNames);
	    stateMap.clear(); // remove any readers that are no longer available
	    for (int i = 0; i < n; i++) {
		ReaderState state = readerStates[i];
		state.update(status[i]);
		stateMap.put(readerNames[i], state);
	    }
	    return true;
	} catch (PCSCException e) {
	    if (e.code == SCARD_E_TIMEOUT) {
		return false;
	    } else {
		throw new CardException("waitForChange() failed", e);
	    }
	}
    }

    static List<CardTerminal> waitForCards(List<? extends CardTerminal> terminals, 
	    long timeout, boolean wantPresent) throws CardException {
	// the argument sanity checks are performed in
	// javax.smartcardio.TerminalFactory or TerminalImpl

	long thisTimeout;
	if (timeout == 0) {
	    timeout = TIMEOUT_INFINITE;
	    thisTimeout = TIMEOUT_INFINITE;
	} else {
	    // if timeout is not infinite, do the initial call that retrieves
	    // the status with a 0 timeout. Otherwise, we might get incorrect
	    // timeout exceptions (seen on Solaris with PC/SC shim)
	    thisTimeout = 0;
	}

	String[] names = new String[terminals.size()];
	int i = 0;
	for (CardTerminal terminal : terminals) {
	    if (terminal instanceof TerminalImpl == false) {
		throw new IllegalArgumentException
		    ("Invalid terminal type: " + terminal.getClass().getName());
	    }
	    TerminalImpl impl = (TerminalImpl)terminal;
	    names[i++] = impl.name;
	}
	
	int[] status = new int[names.length];
	Arrays.fill(status, SCARD_STATE_UNAWARE);

	try {
	    while (true) {
		// note that we pass "timeout" on each native PC/SC call
		// that means that if we end up making multiple (more than 2)
		// calls, we might wait too long.
		// for now assume that is unlikely and not a problem.
		status = SCardGetStatusChange(contextId, thisTimeout, status, names);
		thisTimeout = timeout;

		List<CardTerminal> results = null;
		for (i = 0; i < names.length; i++) {
		    boolean nowPresent = (status[i] & SCARD_STATE_PRESENT) != 0;
		    if (nowPresent == wantPresent) {
			if (results == null) {
			    results = new ArrayList<CardTerminal>();
			}
			results.add(implGetTerminal(names[i]));
		    }
		}
		
		if (results != null) {
		    return Collections.unmodifiableList(results);
		}
	    }
	} catch (PCSCException e) {
	    if (e.code == SCARD_E_TIMEOUT) {
		return Collections.emptyList();
	    } else {
		throw new CardException("waitForCard() failed", e);
	    }
	}
    }

}
