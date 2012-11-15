package com.ortens.pages.basics.buttons;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class PushButtons extends TouchLayout implements Button.ClickListener {

    private static final String CAPTION = "Save";
    private static final String TOOLTIP = "Save changes";
    private static final ThemeResource ICON = new ThemeResource(
            "icons/16/arrow-down.png");
    private static final String NOTIFICATION = "Changes have been saved";

    public PushButtons() {

        setCaption("Push Button");

        addComponent(new Label(
                "A push-button, which can be considered a 'regular' button, "
                        + "returns to it's 'unclicked' state after emitting an event "
                        + "when the user clicks it."));

        // Normal buttons (more themable)
        VerticalLayout buttons = new VerticalLayout();
        buttons.setSpacing(true);
        buttons.setMargin(false, true, false, false);
        buttons.setSizeUndefined();
        addComponent(buttons);

        buttons.setCaption("Normal buttons");

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        // Button w/ text and tooltip
        Button b = new Button(CAPTION);
        b.setDescription(TOOLTIP);
        b.addListener(this); // react to clicks
        hl.addComponent(b);

        // Button w/ text, icon and tooltip
        b = new Button(CAPTION);
        b.setDescription(TOOLTIP);
        b.setIcon(ICON);
        b.addListener(this); // react to clicks
        hl.addComponent(b);

        // Button w/ icon and tooltip
        b = new Button();
        b.setDescription(TOOLTIP);
        b.setIcon(ICON);
        b.addListener(this); // react to clicks
        hl.addComponent(b);
        buttons.addComponent(hl);

        // NativeButtons
        buttons = new VerticalLayout();
        buttons.setSizeUndefined();
        buttons.setSpacing(true);
        buttons.setMargin(false, false, false, true);
        addComponent(buttons);

        buttons.setCaption("Native buttons");
        hl = new HorizontalLayout();
        hl.setSpacing(true);

        // NativeButton w/ text and tooltip
        b = new NativeButton(CAPTION);
        b.setDescription(TOOLTIP);
        b.addListener(this); // react to clicks
        hl.addComponent(b);

        // NativeButton w/ text, icon and tooltip
        b = new NativeButton(CAPTION);
        b.setDescription(TOOLTIP);
        b.setIcon(ICON);
        b.addListener(this); // react to clicks
        hl.addComponent(b);

        // NativeButton w/ icon and tooltip
        b = new NativeButton();
        b.setDescription(TOOLTIP);
        b.setIcon(ICON);
        b.addListener(this); // react to clicks
        hl.addComponent(b);
        buttons.addComponent(hl);

    }

    /*
     * Shows a notification when a button is clicked.
     */
    public void buttonClick(ClickEvent event) {
        getWindow().showNotification(NOTIFICATION);
    }

}
