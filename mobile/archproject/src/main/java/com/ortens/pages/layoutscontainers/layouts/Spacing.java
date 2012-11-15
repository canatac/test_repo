package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class Spacing extends TouchLayout {

    public Spacing() {

        setCaption("Layout spacing");

        addComponent(new Label(
                "<p>Spacing between components can be enabled or "
                        + "disabled. The actual size of the spacing is determined by "
                        + "the theme, and can be customized with CSS.</p>"
                        + "<p>Note that spacing is the space between components within "
                        + "the layout, and margin is the space around the layout as a "
                        + "whole.</p>", Label.CONTENT_XHTML));

        final GridLayout grid = new GridLayout(3, 3);

        // Enable spacing for the example layout (this is the one we'll toggle
        // with the checkbox)
        grid.setSpacing(true);

        // CheckBox for toggling spacing on and off
        final CheckBox spacing = new CheckBox("Spacing enabled");
        spacing.setValue(true);
        spacing.setImmediate(true);
        spacing.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                grid.setSpacing(spacing.booleanValue());
            }
        });
        grid.addComponent(spacing, 0, 0, 2, 0);

        // Add the layout to the containing layout.
        addComponent(grid);

        // Populate the layout with components.
        for (int i = 0; i < 9; i++) {
            grid.addComponent(new Button("" + (i + 1)));
        }
    }
}
