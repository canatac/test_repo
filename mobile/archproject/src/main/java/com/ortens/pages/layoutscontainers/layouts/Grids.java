package com.ortens.pages.layoutscontainers.layouts;

import com.vaadin.terminal.Sizeable;
import com.ortens.TouchLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Grids extends TouchLayout {

    public Grids() {

        setCaption("Grid layout");

        addComponent(new Label(
                "<p>The GridLayout allows you to create a grid "
                        + "of components. The grid may have an arbitrary number of "
                        + "cells in each direction and you can easily set components "
                        + "to fill multiple cells.</p>"
                        + "<p>It supports all basic features, plus some advanced stuff "
                        + "- including spacing, margin, alignment, and expand ratios.</p>",
                Label.CONTENT_XHTML));

        final GridLayout grid = new GridLayout(3, 3);
        grid.setSpacing(true);

        // The style allows us to visualize the cell borders in this example.
        grid.addStyleName("gridexample");

        grid.setWidth(400, Sizeable.UNITS_PIXELS);
        grid.setHeight(400, Sizeable.UNITS_PIXELS);

        // First we insert four components that occupy one cell each
        Button topleft = new Button("Top Left");
        grid.addComponent(topleft, 0, 0);
        grid.setComponentAlignment(topleft, Alignment.MIDDLE_CENTER);

        Button topcenter = new Button("Top Center");
        grid.addComponent(topcenter, 1, 0);
        grid.setComponentAlignment(topcenter, Alignment.MIDDLE_CENTER);

        Button bottomleft = new Button("Bottom Left");
        grid.addComponent(bottomleft, 0, 2);
        grid.setComponentAlignment(bottomleft, Alignment.MIDDLE_CENTER);

        Button bottomcenter = new Button("Bottom Center");
        grid.addComponent(bottomcenter, 1, 2);
        grid.setComponentAlignment(bottomcenter, Alignment.MIDDLE_CENTER);

        // Insert a component that occupies all the rightmost cells
        Button topright = new Button("Extra height");
        grid.addComponent(topright, 2, 0, 2, 2);
        grid.setComponentAlignment(topright, Alignment.MIDDLE_CENTER);

        // Insert a component that occupies two cells in horizontal direction
        Button middleleft = new Button("This is a wide cell in GridLayout");
        grid.addComponent(middleleft, 0, 1, 1, 1);
        grid.setComponentAlignment(middleleft, Alignment.MIDDLE_CENTER);

        // Add the layout to the containing layout.
        addComponent(grid);
    }

}
