package com.ortens.pages.basics;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Links extends TouchLayout {

    private static final String CAPTION = "Open Google";
    private static final String TOOLTIP = "http://www.google.com";
    private static final ThemeResource ICON = new ThemeResource(
            "icons/16/globe.png");

    public Links() {

        setCaption("Links");

        addComponent(new Label("Links open in a new window on an iPhone"));

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        // Link w/ text and tooltip
        Link l = new Link(CAPTION,
                new ExternalResource("http://www.google.com"));
        l.setDescription(TOOLTIP);
        layout.addComponent(l);

        // Link w/ text, icon and tooltip
        l = new Link(CAPTION, new ExternalResource("http://www.google.com"));
        l.setDescription(TOOLTIP);
        l.setIcon(ICON);
        layout.addComponent(l);

        // Link w/ icon and tooltip
        l = new Link();
        l.setResource(new ExternalResource("http://www.google.com"));
        l.setDescription(TOOLTIP);
        l.setIcon(ICON);
        layout.addComponent(l);
        addComponent(layout);
    }

}
