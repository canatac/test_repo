package com.ortens.pages.layoutscontainers;

import com.vaadin.terminal.ThemeResource;
import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class Tabsheets extends TouchLayout implements
        TabSheet.SelectedTabChangeListener {

    // Icons for the table
    private static final ThemeResource icon1 = new ThemeResource(
            "../sampler/icons/action_save.gif");
    private static final ThemeResource icon2 = new ThemeResource(
            "../sampler/icons/comment_yellow.gif");
    private static final ThemeResource icon3 = new ThemeResource(
            "../sampler/icons/icon_info.gif");

    private final TabSheet t;

    public Tabsheets() {

        setCaption("Tab Sheets");

        addComponent(new Label("A Tabsheet organizes multiple components so "
                + "that only the one component associated with the currently "
                + "selected 'tab' is shown. Typically a tab will contain a "
                + "Layout, which in turn may contain many components."));

        // Tab 1 content
        VerticalLayout l1 = new VerticalLayout();
        l1.setMargin(true);
        l1.addComponent(new Label("There are no previously saved actions."));
        // Tab 2 content
        VerticalLayout l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.addComponent(new Label("There are no saved notes."));
        // Tab 3 content
        VerticalLayout l3 = new VerticalLayout();
        l3.setMargin(true);
        l3.addComponent(new Label("There are currently no issues."));

        t = new TabSheet();

        t.addTab(l1, "Saved actions", icon1);
        t.addTab(l2, "Notes", icon2);
        t.addTab(l3, "Issues", icon3);
        t.addListener(this);

        addComponent(t);
    }

    public void selectedTabChange(SelectedTabChangeEvent event) {
        TabSheet tabsheet = event.getTabSheet();
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if (tab != null) {
            getWindow().showNotification("Selected tab: " + tab.getCaption());
        }
    }

}
