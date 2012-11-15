package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Css extends TouchLayout {

    public Css() {

        setCaption("CSS Layout");

        addComponent(new Label(
                "<p>Most commonly developers using Vaadin don't "
                        + "want to think of the browser environment at all. With the "
                        + "flexible layout API found from Grid, Horizontal and Vertical "
                        + "layouts, developers can build almost anything with plain Java. "
                        + "But sometimes experienced web developers miss the flexibility "
                        + "that pure CSS and HTML can offer.</p>"
                        + "<p>CssLayout is a simple layout that places its contained "
                        + "components into a DIV element. It has a simple DOM structure "
                        + "and it leaves all the power to the CSS designer's hands. "
                        + "While having a very narrow feature set, CssLayout is the "
                        + "fastest layout to render in Vaadin.</p>"
                        + "<p>Perhaps, the best example of the usage of a CSS layout "
                        + "is the sampler you are using. The content of the main layout "
                        + "is contained in a CssLayout for maximum lightness and styleability.</p>",
                Label.CONTENT_XHTML));
    }

}
