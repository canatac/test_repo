package com.ortens.mobileapplication;

import com.vaadin.terminal.gwt.server.ApplicationServlet;
import java.io.BufferedWriter;
import java.io.IOException;

public class MobileApplicationServlet extends ApplicationServlet {

    private static final long serialVersionUID = 1337L;

    /**
     * Overrides the default ApplicationServlet to add some iPhone-specific
     * meta-tags to the HTML header.
     */

    protected void writeAjaxPageHtmlHeader(BufferedWriter page, String title,
            String themeUri) throws IOException {

        // POUR 6.3.0 : super.writeAjaxPageHtmlHeader(page, title, themeUri);

        page.append("<meta name=\"viewport\" content="
                + "\"user-scalable=no, width=device-width, "
                + "initial-scale=1.0, maximum-scale=1.0;\" />");
        page.append("<meta name=\"apple-touch-fullscreen\" content=\"yes\" />");
        page.append("<meta name=\"apple-mobile-web-app-capable\" "
                + "content=\"yes\" />");
        page.append("<meta name=\"apple-mobile-web-app-status-bar-style\" "
                + "content=\"black\" />");
        page.append("<link rel=\"apple-touch-icon\" " + "href=\"" + themeUri
                + "/img/icon.png\" />");
        page.append("<link rel=\"apple-touch-startup-image\" " + "href=\""
                + themeUri + "/img/startup.png\" />");
    }
}
