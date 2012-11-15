package com.ortens;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.ortens.widgetset.client.ui.VTouchMenu;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

/**
 * A menu for the Touch UI. Can have an optional caption.
 * 
 * @author mhellber
 * 
 */
@SuppressWarnings("serial")
@ClientWidget(VTouchMenu.class)
public class TouchMenu extends AbstractComponent {

    private static int numberOfItems = 0;

    public TouchMenu() {
        super();
    }

    public TouchMenu(String caption) {
        super();
        setCaption(caption);
    }

    protected LinkedList<TouchMenuItem> menuItems = new LinkedList<TouchMenuItem>();

    /**
     * Create a new TouchMenuItem with a given text and command.
     * 
     * @param text
     * @param command
     * @return The created TouchMenuItem
     */
    public TouchMenuItem addItem(String text, TouchCommand command) {
        return addItem(text, null, null, command);
    }

    /**
     * Create a new TouchMenuItem with a given text and command and icon.
     * 
     * @param text
     * @param icon
     * @param command
     * @return The created TouchMenuItem
     */
    public TouchMenuItem addItem(String text, Resource icon,
            TouchCommand command) {
        return addItem(text, null, icon, command);
    }

    /**
     * Creates a new TouchMenuItem with a given text, description and command.
     * 
     * @param text
     * @param description
     * @param command
     * @return The created TouchMenuItem
     */
    public TouchMenuItem addItem(String text, String description,
            TouchCommand command) {
        return addItem(text, description, null, command);
    }

    /**
     * Creates a new TouchMenuItem with a given text, description, icon and
     * command.
     * 
     * @param text
     * @param description
     * @param icon
     * @param command
     * @return The created TouchMenuItem
     */
    public TouchMenuItem addItem(String text, String description,
            Resource icon, TouchCommand command) {
        TouchMenuItem it = new TouchMenuItem(text, description, icon, command);
        menuItems.add(it);
        return it;
    }

    /**
     * Removes a TouchMenuItem from the menu
     * 
     * @param it
     */
    public void removeItem(TouchMenuItem it) {
        menuItems.remove(it);
        requestRepaint();
    }

    /**
     * Returns an iterator over the TouchMenuItems in this menu.
     * 
     * @return
     */
    public Iterator<TouchMenuItem> getMenuItemIterator() {
        return menuItems.iterator();
    }

    /**
     * Replaces an old TouchMenuItem with a new one in the same position.
     * 
     * @param oldComponent
     * @param newComponent
     */
    public void replaceMenuItem(TouchMenuItem oldComponent,
            TouchMenuItem newComponent) {
        if (menuItems.contains(oldComponent)) {
            for (int i = 0; i < menuItems.size(); i++) {
                if (menuItems.get(i).equals(oldComponent)) {
                    menuItems.remove(i);
                    menuItems.add(i, newComponent);
                }
            }
            requestRepaint();
        }
    }

    /**
     * Paints menu items to client.
     */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.startTag("items");

        // Adds all items
        for (TouchMenuItem it : menuItems) {
            target.startTag("item");
            target.addAttribute("id", it.getId());
            target.addAttribute("text", it.getText());

            if (it.getDescriptionText() != null) {
                target.addAttribute("description", it.getDescriptionText());
            }

            TouchCommand command = it.getCommand();
            if (command != null) {
                target.addAttribute("command", true);
            }

            Resource icon = it.getIcon();

            if (icon != null) {
                target.addAttribute("icon", icon);
            }

            if (!it.isEnabled()) {
                target.addAttribute("disabled", true);
            }

            target.endTag("item");
        }
        target.endTag("items");
    }

    /**
     * Receives changes from the client and checks which menu item has been
     * tapped.
     * 
     * @author mhellber
     * 
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        if (variables.containsKey("tappedId")) {
            int tappedId = ((Integer) variables.get("tappedId")).intValue();
            for (TouchMenuItem item : menuItems) {
                if (item.getId() == tappedId) {
                    // Only fire event for enabled items,
                    // but break anyway if found
                    if (item.isEnabled()) {
                        item.getCommand().itemTouched(item);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Interface for menu commands. It fires when a user taps or clicks a
     * {@link com.vaadin.touchkit.TouchMenu.TouchMenuItem} and is given the
     * selected item as an argument.
     * 
     * @author mhellber
     * 
     */
    public interface TouchCommand {
        public void itemTouched(TouchMenuItem selectedItem);
    }

    /**
     * Class for menu items in the Touch Menu. Every TouchMenuItem has at least
     * a text. They may also contain a description, an icon and a TouchCommand.
     * 
     * @author mhellber
     * 
     */
    public class TouchMenuItem implements Serializable {

        protected final int id;
        protected String text;
        protected String descriptionText;
        protected TouchCommand command = null;
        protected Resource icon = null;
        protected boolean enabled = true;

        public TouchMenuItem() {
            super();
            id = ++numberOfItems;
        }

        /**
         * Creates a new TouchMenuItem. Every TouchMenuItem must have a text.
         * 
         * @param text
         */
        public TouchMenuItem(String text) {
            this();
            if (text == null) {
                throw new IllegalArgumentException(
                        "Menu item must have caption.");
            }
            this.text = text;
        }

        public TouchMenuItem(String text, String description) {
            this();
            setText(text);
            setDescriptionText(description);
        }

        public TouchMenuItem(String text, TouchCommand command) {
            this(text);
            this.command = command;
        }

        public TouchMenuItem(String text, String description,
                TouchCommand command) {
            this(text, description);
            this.command = command;
        }

        public TouchMenuItem(String text, Resource icon, TouchCommand command) {
            this(text, command);
            this.icon = icon;
        }

        public TouchMenuItem(String text, String description, Resource icon,
                TouchCommand command) {
            this(text, description, command);
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setDescriptionText(String descriptionText) {
            this.descriptionText = descriptionText;
        }

        public String getDescriptionText() {
            return descriptionText;
        }

        public TouchCommand getCommand() {
            return command;
        }

        public void setCommand(TouchCommand command) {
            this.command = command;
        }

        public Resource getIcon() {
            return icon;
        }

        public void setIcon(Resource icon) {
            this.icon = icon;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getId() {
            return id;
        }
    }
}
