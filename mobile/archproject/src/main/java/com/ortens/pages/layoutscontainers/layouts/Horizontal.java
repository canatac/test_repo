package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class Horizontal extends TouchLayout {

    public Horizontal() {

        setCaption("Horizontal layout");

        addComponent(new Label(
                "<p>The HorizontalLayout arranges components "
                        + "horizontally.</p>"
                        + "<p>It supports all basic features, plus some advanced stuff "
                        + "- including spacing, margin, alignment, and expand ratios.</p>",
                Label.CONTENT_XHTML));

        HorizontalLayout hl = new HorizontalLayout();

        // First TextField
        TextField tf = new TextField();
        tf.setWidth("70px");
        hl.addComponent(tf);

        // A dash
        Label dash = new Label("-");
        hl.addComponent(dash);
        hl.setComponentAlignment(dash, "middle");

        // Second TextField
        tf = new TextField();
        tf.setWidth("70px");
        hl.addComponent(tf);

        // Another dash
        dash = new Label("-");
        hl.addComponent(dash);
        hl.setComponentAlignment(dash, "middle");

        // Third TextField
        tf = new TextField();
        tf.setWidth("70px");
        hl.addComponent(tf);

        addComponent(hl);
    }

}
