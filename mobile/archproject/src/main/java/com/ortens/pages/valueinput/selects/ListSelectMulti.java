package com.ortens.pages.valueinput.selects;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.ortens.TouchLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ListSelectMulti extends TouchLayout implements
        Property.ValueChangeListener {

    private static final String[] cities = new String[] { "Berlin", "Brussels",
            "Helsinki", "Madrid", "Oslo", "Paris", "Stockholm" };

    public ListSelectMulti() {

        setCaption("List select, multiple selection");

        addComponent(new Label(
                "A simple list select component with single "
                        + "item selection. You can allow or disallow null selection - "
                        + "i.e the possibility to make an empty selection. Null selection "
                        + "is not allowed in this example."));

        VerticalLayout vl = new VerticalLayout();

        ListSelect l = new ListSelect("Please select some cities");
        for (int i = 0; i < cities.length; i++) {
            l.addItem(cities[i]);
        }
        l.setRows(7);
        l.setNullSelectionAllowed(true);
        l.setMultiSelect(true);
        l.setImmediate(true);
        l.addListener(this);

        vl.addComponent(l);
        addComponent(vl);
    }

    /*
     * Shows a notification when a selection is made.
     */
    public void valueChange(ValueChangeEvent event) {
        getWindow().showNotification("Selected cities: " + event.getProperty());

    }
}
