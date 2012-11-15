package com.ortens.pages.basics;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class JSAPI extends TouchLayout {
    private final Label toBeUpdatedFromThread;
    private final Button startThread;
    private final Label running = new Label("");

    public JSAPI() {

        setCaption("JavaScript API");

        VerticalLayout vl = new VerticalLayout();
        vl.setCaption("Run Native JavaScript");
        vl.setSpacing(true);

        final TextField script = new TextField();
        script.setRows(3);
        script.setValue("alert(\"Hello Vaadin\");");
        vl.addComponent(script);

        vl.addComponent(new Button("Run script", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                getWindow().executeJavaScript(script.getValue().toString());
            }
        }));

        addComponent(vl);

        vl = new VerticalLayout();
        vl.setSpacing(true);
        vl.setCaption("Force Server Syncronization");

        vl.addComponent(new Label("For advanced client side programmers "
                + "Vaadin offers a simple method which can "
                + "be used to force the client to synchronize "
                + "with the server. This may be needed for "
                + "example if another part of a mashup "
                + "changes things on server."));

        toBeUpdatedFromThread = new Label("This Label component will "
                + "be updated by a background thread. Click \"Start "
                + "background thread\" button and "
                + "start clicking on the link below to force "
                + "synchronization.", Label.CONTENT_XHTML);
        vl.addComponent(toBeUpdatedFromThread);

        // This label will be show for 10 seconds while the background process
        // is working
        running.setCaption("Background process is "
                + "running for 10 seconds, click the link below");
        running.setIcon(new ThemeResource(
                "../base/common/img/ajax-loader-medium.gif"));

        // Clicking on this button will start a repeating thread that updates
        // the label value
        startThread = new Button("Start background thread",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        ((Layout) startThread.getParent()).replaceComponent(
                                startThread, running);
                        new BackgroundProcess().start();
                    }
                });
        vl.addComponent(startThread);

        // This link will make an Ajax request to the server that will respond
        // with UI changes that have happened since last request
        vl
                .addComponent(new Label(
                        "<a href=\"javascript:vaadin.forceSync();\">javascript: vaadin.forceSync();</a>",
                        Label.CONTENT_XHTML));
        addComponent(vl);
    }

    private class BackgroundProcess extends Thread {
        private final SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");

        @Override
        public void run() {
            try {
                int i = 0;
                while (i++ < 10) {
                    Thread.sleep(1000);
                    toBeUpdatedFromThread.setValue("<strong>Server time is "
                            + f.format(new Date()) + "</strong>");
                }
                toBeUpdatedFromThread.setValue("Background process finished");
                ((Layout) running.getParent()).replaceComponent(running,
                        startThread);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
