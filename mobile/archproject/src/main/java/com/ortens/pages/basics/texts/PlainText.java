package com.ortens.pages.basics.texts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class PlainText extends TouchLayout {

    public PlainText() {

        setCaption("Plain Text Label");

        Label plainText = new Label("This is an example of a Label"
                + " component. The content mode of this label is set"
                + " to CONTENT_TEXT. This means that it will display"
                + " the content text as is. HTML and XML special characters"
                + " (<,>,&) are escaped properly to allow displaying them.");
        plainText.setContentMode(Label.CONTENT_TEXT);

        addComponent(plainText);
    }

}
