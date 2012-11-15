package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class Margins extends TouchLayout implements Button.ClickListener {

    VerticalLayout marginLayout;
    Button topMargin;
    Button rightMargin;
    Button bottomMargin;
    Button leftMargin;

    public Margins() {

        setCaption("Layout margins");

        addComponent(new Label("<p>Layouts can have margins on any of the "
                + "sides. The actual size of the margin is determined by the "
                + "theme, and can be customized using CSS - in this example, "
                + "the right margin size is increased.</p>"
                + "<p>Note that margin is the space around the layout as a "
                + "whole, and spacing is the space between the components "
                + "within the layout.</p>", Label.CONTENT_XHTML));

        addComponent(new Label("Toggle layout margins with the checkboxes."));

        GridLayout gl = new GridLayout(3, 3);
        gl.setWidth("300px");
        gl.setSpacing(true);

        gl.space();
        topMargin = new CheckBox("Top", this);
        topMargin.setValue(true);
        topMargin.setImmediate(true);
        gl.addComponent(topMargin);
        gl.setComponentAlignment(topMargin, "center");

        gl.space();
        leftMargin = new CheckBox("Left", this);
        leftMargin.setValue(true);
        leftMargin.setImmediate(true);
        gl.addComponent(leftMargin);
        gl.setComponentAlignment(leftMargin, "middle");

        marginLayout = new VerticalLayout();
        marginLayout.setStyleName("marginexample");
        marginLayout.setSizeUndefined();
        marginLayout.setMargin(true);
        gl.addComponent(marginLayout);
        marginLayout.addComponent(new Label("Margins all around?"));

        rightMargin = new CheckBox("Right", this);
        rightMargin.setValue(true);
        rightMargin.setImmediate(true);
        gl.addComponent(rightMargin);
        gl.setComponentAlignment(rightMargin, "middle");

        gl.space();
        bottomMargin = new CheckBox("Bottom", this);
        bottomMargin.setValue(true);
        bottomMargin.setImmediate(true);
        gl.addComponent(bottomMargin);
        gl.setComponentAlignment(bottomMargin, "center");

        addComponent(gl);

    }

    public void buttonClick(ClickEvent event) {
        marginLayout.setMargin(topMargin.booleanValue(), rightMargin
                .booleanValue(), bottomMargin.booleanValue(), leftMargin
                .booleanValue());

    }

}
