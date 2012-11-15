package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Vertical extends TouchLayout {

    public Vertical() {

        setCaption("Vertical layout");

        addComponent(new Label("<p>The VerticalLayout arranges components "
                + "vertically. It is 100% wide by default, which is nice "
                + "in many cases, but something to be aware of if trouble "
                + "arises.</p>"
                + "<p>It supports all basic features, plus some advanced "
                + "stuff - including spacing, margin, alignment, and expand "
                + "ratios.</p>", Label.CONTENT_XHTML));

        VerticalLayout vl = new VerticalLayout();
        for (int i = 0; i < 5; i++) {
            TextField tf = new TextField("Row " + (i + 1));
            tf.setWidth("200px");
            // Add the component to the VerticalLayout
            vl.addComponent(tf);
        }

        addComponent(vl);
    }

}
