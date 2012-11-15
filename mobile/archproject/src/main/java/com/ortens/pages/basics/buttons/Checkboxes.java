package com.ortens.pages.basics.buttons;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class Checkboxes extends TouchLayout implements Button.ClickListener {

    private static final String CAPTION = "Allow HTML";
    private static final String TOOLTIP = "Allow/disallow HTML in comments";
    private static final ThemeResource ICON = new ThemeResource(
            "icons/16/ok.png");

    public Checkboxes() {

        setCaption("Checkbox");

        addComponent(new Label(
                "A CheckBox works like a regular push button, "
                        + "triggering a server-side event, but it's state is 'sticky': "
                        + "the checkbox toggles between it's on and off states, instead "
                        + "of popping right back out."));

        VerticalLayout buttons = new VerticalLayout();

        // Button w/ text and tooltip
        CheckBox cb = new CheckBox(CAPTION);
        cb.setDescription(TOOLTIP);
        cb.setImmediate(true);
        cb.addListener(this); // react to clicks
        buttons.addComponent(cb);

        // Button w/ text, icon and tooltip
        cb = new CheckBox(CAPTION);
        cb.setDescription(TOOLTIP);
        cb.setIcon(ICON);
        cb.setImmediate(true);
        cb.addListener(this); // react to clicks
        buttons.addComponent(cb);

        // Button w/ icon and tooltip
        cb = new CheckBox();
        cb.setDescription(TOOLTIP);
        cb.setIcon(ICON);
        cb.setImmediate(true);
        cb.addListener(this); // react to clicks
        buttons.addComponent(cb);
        addComponent(buttons);

    }

    /*
     * Shows a notification when a checkbox is clicked.
     */
    public void buttonClick(ClickEvent event) {
        boolean enabled = event.getButton().booleanValue();
        getWindow().showNotification(
                "HTML " + (enabled ? "enabled" : "disabled"));
    }

}
