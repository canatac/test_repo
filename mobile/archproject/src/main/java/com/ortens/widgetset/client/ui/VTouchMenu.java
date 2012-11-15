package com.ortens.widgetset.client.ui;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ui.Icon;

/**
 * Client side implementation of the TouchMenu component.
 * 
 * @author mhellber
 * 
 */
public class VTouchMenu extends ComplexPanel implements Paintable, ClickHandler {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-touchmenu";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    protected Element container;
    protected VTouchMenu hostReference = this;
    protected MenuItem selectedItem = null;

    public VTouchMenu() {
        super();
        setElement(DOM.createElement("ul"));
        container = getElement();
        setStyleName(CLASSNAME);
        sinkEvents(Event.ONCLICK);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first.
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, true)) {
            // If client.updateComponent returns true
            // there has been no changes and we
            // do not need to update anything.
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the client side identifier (paintable id) for the widget
        paintableId = uidl.getId();

        // Go though all the items sent from the server and build menu
        UIDL itemsUIDL = uidl.getChildUIDL(0);
        Iterator<Object> iterator = itemsUIDL.getChildIterator();

        while (iterator.hasNext()) {
            UIDL item = (UIDL) iterator.next();

            StringBuffer innerHTML = new StringBuffer();
            String itemText = item.getStringAttribute("text");

            String itemDescription = null;
            if (item.hasAttribute("description")) {
                itemDescription = item.getStringAttribute("description");
            }

            final int itemId = item.getIntAttribute("id");

            boolean hasCommand = item.hasAttribute("command");
            Command cmd = null;

            // Add icon
            if (item.hasAttribute("icon")) {
                innerHTML.append("<img src=\""
                        + client.translateVaadinUri(item
                                .getStringAttribute("icon")) + "\" class=\""
                        + Icon.CLASSNAME + "\" alt=\"\" />");
            }

            // Add caption
            innerHTML.append("<div class=\"" + CLASSNAME + "-caption\">"
                    + Util.escapeHTML(itemText) + "</div>");

            // Add description
            if (itemDescription != null) {
                innerHTML.append("<div class=\"" + CLASSNAME
                        + "-description\">" + Util.escapeHTML(itemDescription)
                        + "</div>");
            }

            if (hasCommand) {
                cmd = new Command() {

                    @Override
                    public void execute() {
                        hostReference.onTap(itemId);
                    }
                };
            }

            MenuItem addedItem = addItem(innerHTML.toString(), cmd, itemText);

            if (hasCommand) {
                addedItem.addClickHandler(this);
            }

            if (itemDescription != null) {
                addedItem.addStyleName(CLASSNAME + "-w-desc");
            }

            if (item.hasAttribute("icon")) {
                addedItem.addStyleName(CLASSNAME + "-w-icon");
            }
        }

    }

    private MenuItem addItem(String html, Command cmd, String plaintextCaption) {
        MenuItem item = new MenuItem(html, cmd, plaintextCaption);
        add(item);
        return item;
    }

    @Override
    public void add(Widget child) {
        add(child, getElement());
    }

    /**
     * Send the ID of the tapped menu item to the server
     * 
     * @param itemId
     */
    void onTap(int itemId) {
        if (client != null && paintableId != null) {
            client.updateVariable(paintableId, "tappedId", itemId, true);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem it = (MenuItem) event.getSource();
            if (it.getCommand() != null && it.isEnabled()) {
                notifyParent(it.getCaption());
                DeferredCommand.addCommand(it.getCommand());
            }
        }
    }

    /**
     * Notifies the parent TouchPanel that an item has been selected so that the
     * parent can start animating.
     */
    private void notifyParent(String caption) {
        Widget parent = getParent();

        // Search for parent TouchPanel
        while (!(parent instanceof VTouchPanel) && parent != null) {
            parent = parent.getParent();
        }

        if (parent != null) {
            VTouchPanel parentPanel = (VTouchPanel) parent;
            parentPanel.menuTapped(caption);
        }
    }

    /**
     * Adds or removes active CSS style name for given MenuItem
     * 
     * @param item
     * @param active
     */
    private void setActive(MenuItem item, boolean active) {
        if (active) {
            item.addStyleName("active");
        } else {
            item.removeStyleName("active");
        }
    }

    private class MenuItem extends Widget implements HasHTML, HasClickHandlers {
        private String caption = null;
        private String html = null;
        private Command command = null;
        private boolean enabled = true;

        public MenuItem(String html, Command command, String plaintextCaption) {
            setElement(DOM.createElement("li"));
            setStyleName(CLASSNAME + "-item");
            setHTML(html);
            setCommand(command);
            setCaption(plaintextCaption);
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            registerTouchEvents();
        }

        public Command getCommand() {
            return command;
        }

        public void setCommand(Command command) {
            this.command = command;
        }

        public boolean isEnabled() {
            return enabled;
        }

        @SuppressWarnings("unused")
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (enabled) {
                removeStyleDependentName("disabled");
            } else {
                addStyleDependentName("disabled");
            }
        }

        @Override
        public String getHTML() {
            return html;
        }

        @Override
        public void setHTML(String html) {
            this.html = html;
            DOM.setInnerHTML(getElement(), html);
        }

        @Override
        public String getText() {
            return html;
        }

        @Override
        public void setText(String text) {
            setHTML(Util.escapeHTML(text));
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        // Called from JSNI
        @SuppressWarnings("unused")
        public void onTouchStart() {
            addStyleName("active");
        }

        // Called from JSNI
        @SuppressWarnings("unused")
        public void onTouchEnd() {
            removeStyleName("active");
        }

        // Called from JSNI
        @SuppressWarnings("unused")
        public void onTouchMove() {
            // De-higlight touched item if scrolling
            removeStyleName("active");
        }

        private native void registerTouchEvents()
        /*-{
            var menuitem = this;
            var element = this.@com.ortens.widgetset.client.ui.VTouchMenu$MenuItem::getElement()();
            element.ontouchstart = function(e) {
                menuitem.@com.ortens.widgetset.client.ui.VTouchMenu$MenuItem::onTouchStart()();
            }
             
            element.ontouchend = function(e) {
                menuitem.@com.ortens.widgetset.client.ui.VTouchMenu$MenuItem::onTouchEnd()();
            }
            
            element.ontouchmove = function(e) {
                menuitem.@com.ortens.widgetset.client.ui.VTouchMenu$MenuItem::onTouchMove()();
            } 
        }-*/;

        private native void unregisterTouchEvents()
        /*-{
            var element = this.@com.ortens.widgetset.client.ui.VTouchMenu$MenuItem::getElement()();
               
            element.ontouchstart = null;
               
            element.ontouchend = null;
            
            element.ontouchmove = null;
        }-*/;

        @Override
        protected void onUnload() {
            unregisterTouchEvents();
            super.onUnload();
        }

        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }
    }
}
