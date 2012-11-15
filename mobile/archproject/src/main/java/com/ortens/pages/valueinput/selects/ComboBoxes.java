package com.ortens.pages.valueinput.selects;


import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.ortens.TouchLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractSelect.Filtering;

@SuppressWarnings("serial")
public class ComboBoxes extends TouchLayout implements
        Property.ValueChangeListener {

    private static final String[] cities = new String[] { "Berlin", "Brussels",
            "Helsinki", "Madrid", "Oslo", "Paris", "Stockholm" };

    public ComboBoxes() {

        addComponent(new Label(
                "A drop-down selection component with single "
                        + "item selection. Shown here is the most basic variant, which "
                        + "basically provides the same functionality as a NativeSelect "
                        + "with added lazy-loading if there are many options."));

        VerticalLayout vl = new VerticalLayout();

        ComboBox l = new ComboBox("Please select a city");
        for (int i = 0; i < cities.length; i++) {
            l.addItem(cities[i]);
        }

        l.setFilteringMode(Filtering.FILTERINGMODE_OFF);
        l.setImmediate(true);
        l.addListener(this);

        vl.addComponent(l);
        addComponent(vl);
    }

    /*
     * Shows a notification when a selection is made.
     */
    public void valueChange(ValueChangeEvent event) {
        getWindow().showNotification("Selected city: " + event.getProperty());

    }
}
