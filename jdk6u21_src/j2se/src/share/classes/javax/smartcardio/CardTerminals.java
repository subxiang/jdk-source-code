/*
 * @(#)CardTerminals.java	1.2 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.smartcardio;

import java.util.*;

/**
 * The set of terminals supported by a TerminalFactory.
 * This class allows applications to enumerate the available CardTerminals,
 * obtain a specific CardTerminal, or wait for the insertion or removal of 
 * cards.
 *
 * <p>This class is multi-threading safe and can be used by multiple
 * threads concurrently. However, this object keeps track of the card
 * presence state of each of its terminals. Multiple objects should be used
 * if independent calls to {@linkplain #waitForChange} are required.
 *
 * <p>Applications can obtain instances of this class by calling
 * {@linkplain TerminalFactory#terminals}.
 *
 * @see TerminalFactory
 * @see CardTerminal
 *
 * @version 1.2, 03/23/10
 * @since   1.6
 * @author  Andreas Sterbenz
 * @author  JSR 268 Expert Group
 */
public abstract class CardTerminals {
    
    /**
     * Constructs a new CardTerminals object.
     * 
     * <p>This constructor is called by subclasses only. Application should 
     * call {@linkplain TerminalFactory#terminals}
     * to obtain a CardTerminals object.
     */
    protected CardTerminals() {
	// empty
    }
    
    /**
     * Returns an unmodifiable list of all available terminals.
     *
     * @return an unmodifiable list of all available terminals.
     *
     * @throws CardException if the card operation failed 
     */
    public List<CardTerminal> list() throws CardException {
	 return list(State.ALL);
    }

    /**
     * Returns an unmodifiable list of all terminals matching the specified 
     * state.
     *
     * <p>If state is {@link State#ALL State.ALL}, this method returns
     * all CardTerminals encapsulated by this object.
     * If state is {@link State#CARD_PRESENT State.CARD_PRESENT} or 
     * {@link State#CARD_ABSENT State.CARD_ABSENT}, it returns all
     * CardTerminals where a card is currently present or absent, respectively.
     *
     * <p>If state is {@link State#CARD_INSERTION State.CARD_INSERTION} or 
     * {@link State#CARD_REMOVAL State.CARD_REMOVAL}, it returns all
     * CardTerminals for which an insertion (or removal, respectively)
     * was detected during the last call to {@linkplain #waitForChange}.
     * If <code>waitForChange()</code> has not been called on this object,
     * <code>CARD_INSERTION</code> is equivalent to <code>CARD_PRESENT</code>
     * and <code>CARD_REMOVAL</code> is equivalent to <code>CARD_ABSENT</code>.
     * For an example of the use of <code>CARD_INSERTION</code>,
     * see {@link #waitForChange}.
     *
     * @param state the State
     * @return an unmodifiable list of all terminals matching the specified 
     *   attribute.
     *
     * @throws NullPointerException if attr is null
     * @throws CardException if the card operation failed 
     */
    public abstract List<CardTerminal> list(State state) throws CardException;

    /**
     * Returns the terminal with the specified name or null if no such
     * terminal exists.
     *
     * @return the terminal with the specified name or null if no such
     * terminal exists.
     *
     * @throws NullPointerException if name is null
     */
    public CardTerminal getTerminal(String name) {
	if (name == null) {
	    throw new NullPointerException();
	}
	try {
	    for (CardTerminal terminal : list()) {
		if (terminal.getName().equals(name)) {
		    return terminal;
		}
	    }
	    return null;
	} catch (CardException e) {
	    return null;
	}
    }
    
    /**
     * Waits for card insertion or removal in any of the terminals of this
     * object.
     *
     * <p>This call is equivalent to calling
     * {@linkplain #waitForChange(long) waitForChange(0)}.
     *
     * @throws IllegalStateException if this <code>CardTerminals</code>
     *   object does not contain any terminals
     * @throws CardException if the card operation failed 
     */
    public void waitForChange() throws CardException {
	waitForChange(0);
    }

    /**
     * Waits for card insertion or removal in any of the terminals of this
     * object or until the timeout expires.
     *
     * <p>This method examines each CardTerminal of this object.
     * If a card was inserted into or removed from a CardTerminal since the
     * previous call to <code>waitForChange()</code>, it returns
     * immediately.
     * Otherwise, or if this is the first call to <code>waitForChange()</code>
     * on this object, it blocks until a card is inserted into or removed from
     * a CardTerminal.
     *
     * <p>If <code>timeout</code> is greater than 0, the method returns after
     * <code>timeout</code> milliseconds even if there is no change in state.
     * In that case, this method returns <code>false</code>; otherwise it
     * returns <code>true</code>.
     *
     * <p>This method is often used in a loop in combination with
     * {@link #list(CardTerminals.State) list(State.CARD_INSERTION)},
     * for example:
     * <pre>
     *  TerminalFactory factory = ...;
     *  CardTerminals terminals = factory.terminals();
     *  while (true) {
     *      for (CardTerminal terminal : terminals.list(CARD_INSERTION)) {
     *          // examine Card in terminal, return if it matches
     *      }
     *      terminals.waitForChange();
     *  }</pre>
     *
     * @param timeout if positive, block for up to <code>timeout</code>
     *   milliseconds; if zero, block indefinitely; must not be negative
     * @return false if the method returns due to an expired timeout,
     *   true otherwise.
     *
     * @throws IllegalStateException if this <code>CardTerminals</code>
     *   object does not contain any terminals
     * @throws IllegalArgumentException if timeout is negative
     * @throws CardException if the card operation failed 
     */
    public abstract boolean waitForChange(long timeout) throws CardException;

    /**
     * Enumeration of attributes of a CardTerminal.
     * It is used as a parameter to the {@linkplain CardTerminals#list} method.
     *
     * @since 1.6
     */
    public static enum State {
	/**
	 * All CardTerminals.
	 */
	ALL,
	/**
	 * CardTerminals in which a card is present.
	 */
	CARD_PRESENT,
	/**
	 * CardTerminals in which a card is not present.
	 */
	CARD_ABSENT,
	/**
	 * CardTerminals for which a card insertion was detected during the
	 * latest call to {@linkplain State#waitForChange waitForChange()}
	 * call.
	 */
	CARD_INSERTION,
	/**
	 * CardTerminals for which a card removal was detected during the
	 * latest call to {@linkplain State#waitForChange waitForChange()}
	 * call.
	 */
	CARD_REMOVAL,
    }
    
}
