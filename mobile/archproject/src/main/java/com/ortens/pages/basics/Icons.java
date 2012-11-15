package com.ortens.pages.basics;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Icons extends TouchLayout {

    private final String[] icons = new String[] { "arrow-down.png",
            "arrow-left.png", "arrow-right.png", "arrow-up.png",
            "attention.png", "calendar.png", "cancel.png", "document.png",
            "document-add.png", "document-delete.png", "document-doc.png",
            "document-image.png", "document-pdf.png", "document-ppt.png",
            "document-txt.png", "document-web.png", "document-xsl.png" };

    private final String[] sizes = new String[] { "16" };// , "32", "64" };

    public Icons() {

        setCaption("Icons");

        addComponent(new Label(
                "<p>Most components can have an icon, which is "
                        + "usually displayed next to the caption, depending on the "
                        + "component and the containing layout.</p>"
                        + "<p>When used correctly, icons can make it significantly easier "
                        + "for the user to find a specific functionality. Beware of "
                        + "overuse, which will have the opposite effect.</p>",
                Label.CONTENT_XHTML));

        for (String size : sizes) {
            VerticalLayout vl = new VerticalLayout();
            vl.setSpacing(true);

            vl.setCaption(size + "x" + size + " pixels");
            addComponent(vl);
            for (String icon : icons) {
                Label name = new Label();
                name.setCaption(icon);
                // if (size.equals("64")) {
                // name.setWidth("185px");
                // } else {
                // name.setWidth("150px");
                // }
                name.setIcon(new ThemeResource("icons/" + size + "/" + icon));
                vl.addComponent(name);
            }
        }
    }
}
