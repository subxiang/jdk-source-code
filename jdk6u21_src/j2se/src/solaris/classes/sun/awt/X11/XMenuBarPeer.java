/* 
 * @(#)XMenuBarPeer.java	1.48 10/03/23
 *
 * Copyright (c) 2007, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sun.awt.X11;

import java.awt.*;
import java.awt.peer.*;
import java.awt.event.*;

import java.lang.reflect.Field;
import java.util.Vector;
import java.util.logging.*;

public class XMenuBarPeer extends XBaseMenuWindow implements MenuBarPeer {

    /************************************************
     *
     * Data members
     *
     ************************************************/

    private static Logger log = Logger.getLogger("sun.awt.X11.XMenuBarPeer");
    
    /*
     * Primary members
     */
    private XFramePeer framePeer;
    private MenuBar menuBarTarget;

    /*
     * Index of help menu
     */
    private XMenuPeer helpMenu = null;

    /*
     * dimension constants
     */
    private final static int BAR_SPACING_TOP = 3;
    private final static int BAR_SPACING_BOTTOM = 3;
    private final static int BAR_SPACING_LEFT = 3;
    private final static int BAR_SPACING_RIGHT = 3;
    private final static int BAR_ITEM_SPACING = 2;
    private final static int BAR_ITEM_MARGIN_LEFT = 10;
    private final static int BAR_ITEM_MARGIN_RIGHT = 10;
    private final static int BAR_ITEM_MARGIN_TOP = 2;
    private final static int BAR_ITEM_MARGIN_BOTTOM = 2;

    //fields
    private static Field f_helpMenu;
    private static Field f_menus;

    static {
        f_helpMenu = XToolkit.getField(MenuBar.class, "helpMenu");
        f_menus = XToolkit.getField(MenuBar.class, "menus");
    }

    /************************************************
     *
     * Mapping data
     *
     ************************************************/

    /**
     * XBaseMenuWindow's mappingData is extended with
     * desired height of menu bar
     */
    static class MappingData extends XBaseMenuWindow.MappingData {
        int desiredHeight;

        MappingData(XMenuItemPeer[] items, int desiredHeight) {
            super(items);
            this.desiredHeight = desiredHeight;
        }

        /**
         * Constructs MappingData without items
         * This constructor should be used in case of errors
         */
        MappingData() {
            this.desiredHeight = 0;
        }

        public int getDesiredHeight() {
            return this.desiredHeight;
        }
    }

    /************************************************
     *
     * Construction
     *
     ************************************************/
    XMenuBarPeer(MenuBar menuBarTarget) {
        this.menuBarTarget = menuBarTarget;
    }

    /************************************************
     *
     * Implementaion of interface methods
     *
     ************************************************/

    /*
     * From MenuComponentPeer
     */
    public void setFont(Font f) {
        resetMapping();
        postPaintEvent();
    }

    /*
     * From MenuBarPeer
     */

    /*
     * Functions addMenu, delMenu, addHelpMenu 
     * need to have somewhat strange behaivour
     * deduced from java.awt.MenuBar.
     * We can not get index of particular item in
     * MenuBar.menus array, because MenuBar firstly
     * performs array operations and then calls peer.
     * So we need to synchronize indicies in 'items' 
     * array with MenuBar.menus. We have to follow 
     * these rules:
     * 1. Menus are always added to the end of array,
     * even when helpMenu is present
     * 2. Removal of any menu item acts as casual
     * remove from array
     * 3. MenuBar.setHelpMenu _firstly_ removes
     * previous helpMenu by calling delMenu() if 
     * necessary, then it performs addMenu(),
     * and then - addHelpMenu().
     *
     * Note that these functions don't perform
     * type checks and checks for nulls or duplicates
     */
    public void addMenu(Menu m) {
        addItem(m);
        postPaintEvent();
    }

    public void delMenu(int index) {
        synchronized(getMenuTreeLock()) {
            XMenuItemPeer item = getItem(index);
            if (item != null && item == helpMenu) {
                helpMenu = null;
            }
            delItem(index);
        }
        postPaintEvent();
    }

    public void addHelpMenu(Menu m) {
        XMenuPeer mp = (XMenuPeer)m.getPeer();
        synchronized(getMenuTreeLock()) {
            helpMenu = mp;
        }
        postPaintEvent();
    }
 
    /************************************************
     *
     * Initialization
     *
     ************************************************/
    /**
     * called from XFramePeer.setMenuBar
     */
    public void init(Frame frame) {
        this.target = frame;
        this.framePeer = (XFramePeer)frame.getPeer();
        XCreateWindowParams params = getDelayedParams();
        params.remove(DELAYED);
        params.add(PARENT_WINDOW, framePeer.getShell());
        params.add(TARGET, frame);
        init(params);
    }

    /**
     * Overriden initialization
     */
    void postInit(XCreateWindowParams params) {
        super.postInit(params);
        Vector targetMenuVector = null;
        Menu targetHelpMenu = null;
        try {
            // Get menus from the target.
            targetMenuVector = (Vector)f_menus.get(menuBarTarget);
            targetHelpMenu = (Menu)f_helpMenu.get(menuBarTarget);
            reloadItems(targetMenuVector);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
        if (targetHelpMenu != null) {
            addHelpMenu(targetHelpMenu);
        }
        xSetVisible(true);
        toFront();
    }

    /************************************************
     *
     * Implementation of abstract methods
     *
     ************************************************/

    /**
     * Menu bar is always root window in menu window's
     * hierarchy
     */
    protected XBaseMenuWindow getParentMenuWindow() {
        return null;
    }

    /**
     * @see XBaseMenuWindow.map
     */
    protected MappingData map() {
        XMenuItemPeer[] itemVector = copyItems();
        int itemCnt = itemVector.length;
        XMenuItemPeer helpMenu = this.helpMenu;
        int helpMenuPos = -1;
        //find helpMenu and move it to the end of array
        if (helpMenu != null) {
            //Fixed 6270847: PIT: HELP menu is not shown at the right place when normal menus added to MB are removed, XToolkit
            for (int i = 0; i < itemCnt; i++) {
                if (itemVector[i] == helpMenu) {
                    helpMenuPos = i;
                    break;
                }
            }
            if (helpMenuPos != -1 && helpMenuPos != itemCnt - 1) {
                System.arraycopy(itemVector, helpMenuPos + 1, itemVector, helpMenuPos, itemCnt - 1 - helpMenuPos);
                itemVector[itemCnt - 1] = helpMenu;
            }
        }
        //We need maximum height before calculating item's bounds
        int maxHeight = 0;
        XMenuItemPeer.TextMetrics[] itemMetrics = new XMenuItemPeer.TextMetrics[itemCnt];
        for (int i = 0; i < itemCnt; i++) {
            itemMetrics[i] = itemVector[i].getTextMetrics();
            Dimension dim = itemMetrics[i].getTextDimension();
            if (dim != null) {
                maxHeight = Math.max(maxHeight, dim.height);
            }
        }
        //Calculate bounds
        int nextOffset = 0;
        int itemHeight = BAR_ITEM_MARGIN_TOP + maxHeight + BAR_ITEM_MARGIN_BOTTOM;
        int mappedCnt = itemCnt;
        for (int i = 0; i < itemCnt; i++) {
            XMenuItemPeer item = itemVector[i];
            XMenuItemPeer.TextMetrics metrics = itemMetrics[i];
            Dimension dim = metrics.getTextDimension();
            if (dim != null) {
                int itemWidth = BAR_ITEM_MARGIN_LEFT + dim.width + BAR_ITEM_MARGIN_RIGHT;
                //Fix for 6270757: PIT: Menus and Sub-menus are shown outside the frame, XToolkit
                //Cut-off items that don't fit in window
                //At least one item must remain in menu
                if ((nextOffset + itemWidth > this.width) && (i > 0)) {
                    mappedCnt = i;
                    break;
                }
                //If this item is help menu, move it to the right edge
                if ((i == itemCnt - 1) && helpMenuPos != -1) {
                    nextOffset = Math.max(nextOffset, this.width - itemWidth - BAR_SPACING_RIGHT);
                }
                Rectangle bounds = new Rectangle(nextOffset, BAR_SPACING_TOP, itemWidth, itemHeight);
                //text should be centered vertically in menu item's bounds
                int y = (maxHeight + dim.height) / 2  - metrics.getTextBaseline();
                Point textOrigin = new Point(nextOffset + BAR_ITEM_MARGIN_LEFT, BAR_SPACING_TOP + BAR_ITEM_MARGIN_TOP + y);
                nextOffset += itemWidth + BAR_ITEM_SPACING;
                item.map(bounds, textOrigin);
            } else {
                Rectangle bounds = new Rectangle(nextOffset, BAR_SPACING_TOP, 0, 0);
                Point textOrigin = new Point(nextOffset + BAR_ITEM_MARGIN_LEFT, BAR_SPACING_TOP + BAR_ITEM_MARGIN_TOP);
            }
        }
        XMenuItemPeer mappedVector[] = new XMenuItemPeer[mappedCnt];
        System.arraycopy(itemVector, 0, mappedVector, 0, mappedCnt);
        MappingData mappingData = new MappingData(mappedVector, BAR_SPACING_TOP + itemHeight + BAR_SPACING_BOTTOM);
        return mappingData;
    }

    /**
     * @see XBaseMenuWindow.getSubmenuBounds
     */
    protected Rectangle getSubmenuBounds(Rectangle itemBounds, Dimension windowSize) {
        Rectangle globalBounds = toGlobal(itemBounds);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle res;
        res = fitWindowBelow(globalBounds, windowSize, screenSize);
        if (res != null) {
            return res;
        }
        res = fitWindowAbove(globalBounds, windowSize, screenSize);
        if (res != null) {
            return res;
        }
        res = fitWindowRight(globalBounds, windowSize, screenSize);
        if (res != null) {
            return res;
        }
        res = fitWindowLeft(globalBounds, windowSize, screenSize);
        if (res != null) {
            return res;
        }
        return fitWindowToScreen(windowSize, screenSize);
    }

    /**
     * This function is called when it's likely that
     * size of items has changed. 
     * Invokes framePeer's updateChildrenSizes()
     */
    protected void updateSize() {
        resetMapping();
        if (framePeer != null) {
            XToolkit.executeOnEventHandlerThread(target, new Runnable() {
                    public void run() {
                        framePeer.updateChildrenSizes();
                    }
                });
        }
    }


    /************************************************
     *
     * Utility functions
     *
     ************************************************/

    /** 
     * Returns desired height of menu bar
     */
    int getDesiredHeight() {
        MappingData mappingData = (MappingData)getMappingData();
        return mappingData.getDesiredHeight();
    }

    /**
     * Returns true if framePeer is not null and is enabled
     * Used to fix 6185057: Disabling a frame does not disable 
     * the menus on the frame, on solaris/linux
     */
    boolean isFramePeerEnabled() {
        if (framePeer != null) {
            return framePeer.isEnabled();
        }
        return false;
    }

    /************************************************
     *
     * Overriden XBaseMenuWindow functions
     *
     ************************************************/

    /**
     * @see XBaseMenuWindow.doDispose()
     */
    protected void doDispose() {
        super.doDispose();
        XToolkit.targetDisposedPeer(menuBarTarget, this);        
    }

    /************************************************
     *
     * Overriden XWindow general-purpose functions
     *
     ************************************************/

    /**
     * For menu bars this function is called from framePeer's
     * reshape(...) and updateChildrenSizes()
     */
    public void reshape(int x, int y, int width, int height) {
        if ((width != this.width) || (height != this.height)) {
            resetMapping();
        }
        super.reshape(x, y, width, height);
    }

    /**
     * Performs ungrabbing of input
     * @see XBaseWindow.ungrabInputImpl()
     */
    void ungrabInputImpl() {
        selectItem(null, false);
        super.ungrabInputImpl();
        postPaintEvent();
    }

    /************************************************
     *
     * Overriden XWindow painting & printing
     *
     ************************************************/
    public void paint(Graphics g) {
        resetColors();
        /* Calculate menubar dimension. */
        int width = getWidth();
        int height = getHeight();

        flush();
        //Fill background of rectangle
        g.setColor(getBackgroundColor());
        g.fillRect(1, 1, width - 2, height - 2);

        draw3DRect(g, 0, 0, width, height, true);

        //Paint menus
        MappingData mappingData = (MappingData)getMappingData();
        XMenuItemPeer[] itemVector = mappingData.getItems();
        XMenuItemPeer selectedItem = getSelectedItem();
        for (int i = 0; i < itemVector.length; i++) {
            XMenuItemPeer item = itemVector[i];
            //paint item
            g.setFont(item.getTargetFont());
            Rectangle bounds = item.getBounds();
            Point textOrigin = item.getTextOrigin();
            if (item == selectedItem) {
                g.setColor(getSelectedColor());
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                draw3DRect(g, bounds.x, bounds.y, bounds.width, bounds.height, false);
            }
            if (isFramePeerEnabled() && item.isTargetItemEnabled()) {
                g.setColor(getForegroundColor());
            } else {
                g.setColor(getDisabledColor());
            }
            g.drawString(item.getTargetLabel(), textOrigin.x, textOrigin.y);
        }
        flush();
    }

    static final int W_DIFF = (XFramePeer.CROSSHAIR_INSET + 1) * 2;
    static final int H_DIFF = XFramePeer.BUTTON_Y + XFramePeer.BUTTON_H;

    void print(Graphics g) {
        //TODO:Implement
    }
    
    /************************************************
     *
     * Overriden XBaseMenuWindow event handling
     *
     ************************************************/
    protected void handleEvent(AWTEvent event) {
        // explicitly block all events except PaintEvent.PAINT for menus,
        // that are in the modal blocked window
        if ((framePeer != null) &&
            (event.getID() != PaintEvent.PAINT))
        {
            if (framePeer.isModalBlocked()) {
                return;
            }
        }
        switch(event.getID()) {
        case MouseEvent.MOUSE_PRESSED:
        case MouseEvent.MOUSE_RELEASED:
        case MouseEvent.MOUSE_CLICKED:
        case MouseEvent.MOUSE_MOVED:
        case MouseEvent.MOUSE_ENTERED:
        case MouseEvent.MOUSE_EXITED:
        case MouseEvent.MOUSE_DRAGGED:
            //Fix for 6185057: Disabling a frame does not disable 
            //the menus on the frame, on solaris/linux
            if (isFramePeerEnabled()) {
                doHandleJavaMouseEvent((MouseEvent)event);
            }
            break;
        case KeyEvent.KEY_PRESSED:
        case KeyEvent.KEY_RELEASED:
            //Fix for 6185057: Disabling a frame does not disable 
            //the menus on the frame, on solaris/linux
            if (isFramePeerEnabled()) {
                doHandleJavaKeyEvent((KeyEvent)event);
            }
            break;
        default:
            super.handleEvent(event);
            break;
        }        
    }



    /************************************************
     *
     * Overriden XWindow keyboard processing
     *
     ************************************************/
    
    /*
     * This function is called from XWindow
     * @see XWindow.handleF10onEDT()
     */
    void handleF10KeyPress(KeyEvent event) {
        // explicitly block F10 key press for menus,
        // that are in the modal blocked window
        if (framePeer != null) {
            if (framePeer.isModalBlocked()) {
                return;
            }
        }

        int keyState = event.getModifiers();
        if (((keyState & InputEvent.ALT_MASK) != 0) ||
            ((keyState & InputEvent.SHIFT_MASK) != 0) ||
            ((keyState & InputEvent.CTRL_MASK) != 0)) {
            return;
        }  
        grabInput();
        selectItem(getFirstSelectableItem(), true);
    }

    /*
     * In previous version keys were handled in handleKeyPress.
     * Now we override this function do disable F10 explicit
     * processing. All processing is done using KeyEvent.
     */
    public void handleKeyPress(XEvent xev) {
        XKeyEvent xkey = xev.get_xkey();
        if (log.isLoggable(Level.FINE)) log.fine(xkey.toString());
        if (isEventDisabled(xev)) {
            return;
        }
        final Component currentSource = (Component)getEventSource();
        //This is the only difference from XWindow.handleKeyPress
        //Ancestor's function can invoke handleF10KeyPress here
        handleKeyPress(xkey);
    }

} //class XMenuBarPeer
