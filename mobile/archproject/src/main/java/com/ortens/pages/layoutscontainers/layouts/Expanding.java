package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Expanding extends TouchLayout {

    public Expanding() {

        setCaption("Expanding components");

        addComponent(new Label("<p>You can expand components to make them "
                + "occupy the space left over by other components.</p>"
                + "<p>If more than one component is expanded, the ratio "
                + "determines how the leftover space is shared between the "
                + "expanded components.</p>", Label.CONTENT_XHTML));

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("80%"); // make the layout grow with the window
        // size
        addComponent(layout);

        Button naturalButton = new Button("Natural");
        layout.addComponent(naturalButton);

        Button expandedButton = new Button("Expanded");
        expandedButton.setWidth("100%");
        layout.addComponent(expandedButton);
        layout.setExpandRatio(expandedButton, 1.0f);

        Button sizedButton = new Button("Explicit");
        sizedButton.setWidth("80px");
        layout.addComponent(sizedButton);

        addComponent(layout);

        layout = new HorizontalLayout();
        layout.setWidth("80%"); // make the layout grow with the window
        // size
        addComponent(layout);

        naturalButton = new Button("Natural");
        layout.addComponent(naturalButton);

        Button expandedButton1 = new Button("Ratio 1.0");
        expandedButton1.setWidth("100%");
        layout.addComponent(expandedButton1);
        layout.setExpandRatio(expandedButton1, 1.0f);

        Button expandedButton2 = new Button("Ratio 2.0");
        expandedButton2.setWidth("100%");
        layout.addComponent(expandedButton2);
        layout.setExpandRatio(expandedButton2, 2.0f);

        addComponent(layout);
    }

}
