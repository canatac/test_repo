package com.ortens.pages.layoutscontainers;

import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class Panels extends TouchLayout implements ClickListener {

    private final Panel panel;

    public Panels() {

        setCaption("Panels");

        addComponent(new Label(
                "Panel is a simple container. It's internal layout "
                        + "(by default VerticalLayout) "
                        + "can be configured or exchanged to get desired results. "
                        + "Components that are added to the Panel will in effect be "
                        + "added to the layout."));

        // Panel 1 - with caption
        panel = new Panel("This is a standard Panel");

        // let's adjust the panels default layout (a VerticalLayout)
        VerticalLayout layout = (VerticalLayout) panel.getContent();
        layout.setMargin(true); // we want a margin
        layout.setSpacing(true); // and spacing between components
        addComponent(panel);

        // Let's add a few rows
        for (int i = 0; i < 20; i++) {
            panel.addComponent(new Label(
                    "The quick brown fox jumps over the lazy dog."));
        }
    }

    public void buttonClick(ClickEvent event) {
        if (panel.getCaption().equals("")) {
            panel.setCaption("This is a standard Panel");
        } else {
            panel.setCaption("");
        }
    }

}
