package com.ortens.pages.gridstrees;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.Action;
import com.ortens.TouchLayout;
import com.ortens.sampler.ExampleUtil;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class Trees extends TouchLayout implements Property.ValueChangeListener,
        Button.ClickListener, Action.Handler {

    // Actions for the context menu
    private static final Action ACTION_ADD = new Action("Add child item");
    private static final Action ACTION_DELETE = new Action("Delete");
    private static final Action[] ACTIONS = new Action[] { ACTION_ADD,
            ACTION_DELETE };

    private final Tree tree;

    HorizontalLayout editBar;
    private final TextField editor;
    private final Button change;

    public Trees() {

        setCaption("Trees");

        addComponent(new Label(
                "<p>The Tree component provides a natural way to "
                        + "represent data that has hierarchical relationships, such as "
                        + "filesystems or message threads.</p>"
                        + "<p>In this example, you can select any single tree node and "
                        + "modify its 'name' property. Click again to de-select.</p>",
                Label.CONTENT_XHTML));

        VerticalLayout vl = new VerticalLayout();

        // Create the Tree,a dd to layout
        tree = new Tree("Hardware Inventory");
        vl.addComponent(tree);

        // Contents from a (prefilled example) hierarchical container:
        tree.setContainerDataSource(ExampleUtil.getHardwareContainer());

        // Add Valuechangelistener and Actionhandler
        tree.addListener(this);

        // Add actions (context menu)
        tree.addActionHandler(this);

        // Cause valueChange immediately when the user selects
        tree.setImmediate(true);

        // Set tree to show the 'name' property as caption for items
        tree.setItemCaptionPropertyId(ExampleUtil.hw_PROPERTY_NAME);
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);

        // Expand whole tree
        for (Object id : tree.rootItemIds()) {
            tree.expandItemsRecursively(id);
        }

        // Create the 'editor bar' (textfield and button in a horizontallayout)
        editBar = new HorizontalLayout();
        editBar.setMargin(false, false, false, true);
        editBar.setEnabled(false);
        addComponent(editBar);
        // textfield
        editor = new TextField("Item name");
        editor.setImmediate(true);
        editBar.addComponent(editor);
        // apply-button
        change = new Button("Apply", this, "buttonClick");
        editBar.addComponent(change);
        editBar.setComponentAlignment(change, "bottom");

        addComponent(vl);
    }

    public void valueChange(ValueChangeEvent event) {
        if (event.getProperty().getValue() != null) {
            // If something is selected from the tree, get it's 'name' and
            // insert it into the textfield
            editor.setValue(tree.getItem(event.getProperty().getValue())
                    .getItemProperty(ExampleUtil.hw_PROPERTY_NAME));
            editor.requestRepaint();
            editBar.setEnabled(true);
        } else {
            editor.setValue("");
            editBar.setEnabled(false);
        }
    }

    public void buttonClick(ClickEvent event) {
        // If the edited value contains something, set it to be the item's new
        // 'name' property
        if (!editor.getValue().equals("")) {
            Item item = tree.getItem(tree.getValue());
            Property name = item.getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
            name.setValue(editor.getValue());
        }
    }

    /*
     * Returns the set of available actions
     */
    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    /*
     * Handle actions
     */
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ACTION_ADD) {
            // Allow children for the target item, and expand it
            tree.setChildrenAllowed(target, true);
            tree.expandItem(target);

            // Create new item, set parent, disallow children (= leaf node)
            Object itemId = tree.addItem();
            tree.setParent(itemId, target);
            tree.setChildrenAllowed(itemId, false);

            // Set the name for this item (we use it as item caption)
            Item item = tree.getItem(itemId);
            Property name = item.getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
            name.setValue("New Item");

        } else if (action == ACTION_DELETE) {
            Object parent = tree.getParent(target);
            tree.removeItem(target);
            // If the deleted object's parent has no more children, set it's
            // childrenallowed property to false (= leaf node)
            if (parent != null && tree.getChildren(parent).size() == 0) {
                tree.setChildrenAllowed(parent, false);
            }
        }
    }

}
