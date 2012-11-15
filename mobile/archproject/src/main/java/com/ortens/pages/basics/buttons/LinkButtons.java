package com.ortens.pages.basics.buttons;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class LinkButtons extends TouchLayout implements Button.ClickListener {

    private static final String CAPTION = "Help";
    private static final String TOOLTIP = "Show help";
    private static final ThemeResource ICON = new ThemeResource(
            "icons/16/attention.png");
    private static final String NOTIFICATION = "Help clicked";

    public LinkButtons() {

        setCaption("Link Button");

        addComponent(new Label(
                "A link-styled button works like a push button, "
                        + "but looks like a Link. It does not actually link somewhere, "
                        + "but triggers a server-side event, just like a regular button."));

        VerticalLayout buttons = new VerticalLayout();

        // Button w/ text and tooltip
        Button b = new Button(CAPTION);
        b.setStyleName(Button.STYLE_LINK);
        b.setDescription(TOOLTIP);
        b.addListener(this); // react to clicks
        buttons.addComponent(b);

        // Button w/ text, icon and tooltip
        b = new Button(CAPTION);
        b.setStyleName(Button.STYLE_LINK);
        b.setDescription(TOOLTIP);
        b.setIcon(ICON);
        b.addListener(this); // react to clicks
        buttons.addComponent(b);

        // Button w/ icon and tooltip
        b = new Button();
        b.setStyleName(Button.STYLE_LINK);
        b.setDescription(TOOLTIP);
        b.setIcon(ICON);
        b.addListener(this); // react to clicks
        buttons.addComponent(b);
        addComponent(buttons);

    }

    /*
     * Shows a notification when a button is clicked.
     */
    public void buttonClick(ClickEvent event) {
        getWindow().showNotification(NOTIFICATION);
    }

}
