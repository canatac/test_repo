package com.ortens.widgetset.client.ui;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VCssLayout;

/**
 * Lightweight layout for use with TouchPanel. If the layout has only one child,
 * it gets a different CSS class so it can be displayed differently.
 * 
 * @author mhellber
 * 
 */
public class VTouchLayout extends VCssLayout {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-touchlayout";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    VTouchLayout() {
        super();
        setStyleName(CLASSNAME);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        int children = uidl.getChildCount();
        if (children == 1) {
            // If the layout only contains one item, it should be displayed
            // full screen. Add style to indicate that this is the only item.
            addStyleName("single-child");
        }
    }

}
