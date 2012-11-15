package com.ortens.pages.basics;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class Images extends TouchLayout {

    public Images() {

        setCaption("Images");

        HorizontalLayout hl = new HorizontalLayout();
        Embedded e = new Embedded("Image from a theme resource",
                new ThemeResource("icons/64/document.png"));
        hl.addComponent(e);

        addComponent(hl);
    }
}
