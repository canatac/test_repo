package com.ortens.pages.layoutscontainers;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class Accordions extends TouchLayout implements
        Accordion.SelectedTabChangeListener {

    private static final ThemeResource icon1 = new ThemeResource(
            "../sampler/icons/action_save.gif");
    private static final ThemeResource icon2 = new ThemeResource(
            "../sampler/icons/comment_yellow.gif");
    private static final ThemeResource icon3 = new ThemeResource(
            "../sampler/icons/icon_info.gif");

    private final Accordion a;

    public Accordions() {

        setCaption("Accordions");

        addComponent(new Label(
                "An accordion component is a specialized case of "
                        + "a tabsheet. Within an accordion, the tabs are organized "
                        + "vertically, and the content will be shown directly below the tab."));

        Label l1 = new Label("There are no previously saved actions.");
        Label l2 = new Label("There are no saved notes.");
        Label l3 = new Label("There are currently no issues.");

        a = new Accordion();

        a.addTab(l1, "Saved actions", icon1);
        a.addTab(l2, "Notes", icon2);
        a.addTab(l3, "Issues", icon3);
        a.addListener(this);

        addComponent(a);
    }

    public void selectedTabChange(SelectedTabChangeEvent event) {
        TabSheet tabsheet = event.getTabSheet();
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if (tab != null) {
            getWindow().showNotification("Selected tab: " + tab.getCaption());
        }
    }
}
