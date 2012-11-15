package com.ortens.pages.valueinput.dates;

import java.util.Locale;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.ortens.TouchLayout;
import com.ortens.sampler.ExampleUtil;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class LocaleDate extends TouchLayout implements
        Property.ValueChangeListener {

    private final InlineDateField datetime;
    private final NativeSelect localeSelection;

    public LocaleDate() {

        setCaption("Date selection with locale");

        VerticalLayout vl = new VerticalLayout();
        datetime = new InlineDateField("Please select the starting time:");

        // Set the value of the PopupDateField to current date
        datetime.setValue(new java.util.Date());

        // Set the correct resolution
        datetime.setResolution(InlineDateField.RESOLUTION_MIN);
        datetime.setImmediate(true);

        // Create selection and fill it with locales
        localeSelection = new NativeSelect("Select date format:");
        localeSelection.addListener(this);
        localeSelection.setImmediate(true);
        localeSelection
                .setContainerDataSource(ExampleUtil.getLocaleContainer());

        vl.addComponent(datetime);
        vl.addComponent(localeSelection);
        addComponent(vl);
    }

    public void valueChange(ValueChangeEvent event) {
        Item selected = localeSelection.getItem(event.getProperty().getValue());
        datetime.setLocale((Locale) selected.getItemProperty(
                ExampleUtil.locale_PROPERTY_LOCALE).getValue());
        datetime.requestRepaint();
    }

}
