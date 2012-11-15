package com.ortens.pages.basics.texts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class PreformattedText extends TouchLayout {

    public PreformattedText() {

        setCaption("Preformatted Label");

        Label preformattedText = new Label("This is an example of a \n"
                + " Label component.\n" + "\nThe content mode \n"
                + "of this label is set" + "\nto CONTENT_PREFORMATTED. \n"
                + "This means" + "\nthat it will \n"
                + "display the content text" + "\nusing a fixed-width \n"
                + "font. You also have" + "\nto insert the line \n"
                + "breaks yourself.\n" + "\n\nHTML and XML \n"
                + "special characters" + "\n(<,>,&) are "
                + "\nescaped properly to" + "\nallow displaying them.");
        preformattedText.setContentMode(Label.CONTENT_PREFORMATTED);
        addComponent(preformattedText);
    }

}
