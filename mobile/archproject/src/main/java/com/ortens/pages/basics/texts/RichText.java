package com.ortens.pages.basics.texts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class RichText extends TouchLayout {

    public RichText() {

        setCaption("Rich Text Label");

        addComponent(new Label(
                "<h1>Rich text example</h1>"
                        + "<p>The <b>quick</b> brown fox jumps <sup>over</sup> the <b>lazy</b> dog.</p>"
                        + "<p>This text is formatted with <pre>XTHML</pre></p>",
                Label.CONTENT_XHTML));
    }

}
