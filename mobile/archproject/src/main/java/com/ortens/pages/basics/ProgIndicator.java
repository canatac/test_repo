package com.ortens.pages.basics;

import com.ortens.TouchLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class ProgIndicator extends TouchLayout {

    private final ProgressIndicator pi1;
    private final ProgressIndicator pi2;

    private Worker1 worker1;
    private Worker2 worker2;

    private final Button startButton1;
    private final Button startButton2;

    public ProgIndicator() {

        setCaption("Progress Indicator");

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeUndefined();
        vl.setCaption("Normal mode");
        vl.setSpacing(true);
        addComponent(vl);

        vl.addComponent(new Label("Runs for 20 seconds"));

        // Add a normal progress indicator
        pi1 = new ProgressIndicator();
        pi1.setIndeterminate(false);
        pi1.setEnabled(false);
        vl.addComponent(pi1);
        vl.setComponentAlignment(pi1, "middle");

        startButton1 = new Button("Start normal", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                worker1 = new Worker1();
                worker1.start();
                pi1.setEnabled(true);
                pi1.setValue(0f);
                startButton1.setEnabled(false);
            }
        });
        startButton1.setStyleName("small");
        vl.addComponent(startButton1);

        vl = new VerticalLayout();
        vl.setCaption("Indeterminate mode");
        vl.setSpacing(true);
        vl.setSizeUndefined();
        addComponent(vl);

        vl.addComponent(new Label("Runs for 10 seconds"));

        // Add an indeterminate progress indicator
        pi2 = new ProgressIndicator();
        pi2.setIndeterminate(true);
        pi2.setPollingInterval(5000);
        pi2.setEnabled(false);
        vl.addComponent(pi2);

        startButton2 = new Button("Start indeterminate",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        worker2 = new Worker2();
                        worker2.start();
                        pi2.setEnabled(true);
                        pi2.setVisible(true);
                        startButton2.setEnabled(false);
                    }
                });

        startButton2.setStyleName("small");
        vl.addComponent(startButton2);

    }

    public void prosessed() {
        int i = worker1.getCurrent();
        if (i == Worker1.MAX) {
            pi1.setEnabled(false);
            startButton1.setEnabled(true);
            pi1.setValue(1f);
        } else {
            pi1.setValue((float) i / Worker1.MAX);
        }
    }

    public void prosessed2() {
        pi2.setEnabled(false);
        startButton2.setEnabled(true);
    }

    public class Worker1 extends Thread {
        int current = 1;
        public final static int MAX = 20;

        @Override
        public void run() {
            for (; current <= MAX; current++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // All modifications to Vaadin components should be synchronized
                // over application instance. For normal requests this is done
                // by the servlet. Here we are changing the application state
                // via a separate thread.
                synchronized (getApplication()) {
                    prosessed();
                }
            }
        }

        public int getCurrent() {
            return current;
        }

    }

    public class Worker2 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // synchronize changes over application
            synchronized (getApplication()) {
                prosessed2();
            }
        }
    }
}
