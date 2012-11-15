package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class Custom extends TouchLayout {

    public Custom() {

        setCaption("Custom layouts");

        addComponent(new Label(
                "The CustomLayout allows you to make a layout "
                        + "in regular HTML, using styles and embedding images to suit "
                        + "your needs. You can even make the layout using a WYSIWYG "
                        + "editor.</p>"
                        + "<p>Marking an area in the HTML as a named location will allow "
                        + "you to replace that area with a component later.</p>"
                        + "<p>HTML prototypes can often be quickly converted into a "
                        + "working application this way, providing a clear path from "
                        + "design to implementation.</p>", Label.CONTENT_XHTML));

        // Create the custom layout and set it as a component in
        // the current layout
        CustomLayout custom = new CustomLayout(
                "../../sampler/layouts/examplecustomlayout");
        addComponent(custom);

        // Create components and bind them to the location tags
        // in the custom layout.
        TextField username = new TextField();
        custom.addComponent(username, "username");

        TextField password = new TextField();
        password.setSecret(true);
        custom.addComponent(password, "password");

        Button ok = new Button("Login");
        custom.addComponent(ok, "okbutton");

        addComponent(custom);
    }

}
