package com.ortens.pages.valueinput.dates;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.ortens.TouchLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ResolutionDate extends TouchLayout implements
        Property.ValueChangeListener {

    public static final Object resolution_PROPERTY_NAME = "name";
    // Resolution fields from DateField
    private static final int[] resolutions = { InlineDateField.RESOLUTION_YEAR,
            InlineDateField.RESOLUTION_MONTH, InlineDateField.RESOLUTION_DAY,
            InlineDateField.RESOLUTION_HOUR, InlineDateField.RESOLUTION_MIN,
            InlineDateField.RESOLUTION_SEC, InlineDateField.RESOLUTION_MSEC };
    private static final String[] resolutionNames = { "Year", "Month", "Day",
            "Hour", "Minute", "Second", "Millisecond" };

    private final InlineDateField datetime;
    private final NativeSelect localeSelection;

    public ResolutionDate() {

        setCaption("Date selection with resolution");

        VerticalLayout vl = new VerticalLayout();

        datetime = new InlineDateField("Please select the starting time:");

        // Set the value of the PopupDateField to current date
        datetime.setValue(new java.util.Date());

        // Set the correct resolution
        datetime.setResolution(InlineDateField.RESOLUTION_DAY);
        datetime.setImmediate(true);

        // Create selection
        localeSelection = new NativeSelect("Select resolution:");
        localeSelection.setNullSelectionAllowed(false);
        localeSelection.addListener(this);
        localeSelection.setImmediate(true);

        // Fill the selection with choices, set captions correctly
        localeSelection.setContainerDataSource(getResolutionContainer());
        localeSelection.setItemCaptionPropertyId(resolution_PROPERTY_NAME);
        localeSelection.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_PROPERTY);

        vl.addComponent(datetime);
        vl.addComponent(localeSelection);
        addComponent(vl);
    }

    public void valueChange(ValueChangeEvent event) {
        datetime.setResolution((Integer) event.getProperty().getValue());
        datetime.requestRepaint();
    }

    private IndexedContainer getResolutionContainer() {
        IndexedContainer resolutionContainer = new IndexedContainer();
        resolutionContainer.addContainerProperty(resolution_PROPERTY_NAME,
                String.class, null);
        for (int i = 0; i < resolutions.length; i++) {
            Item added = resolutionContainer.addItem(resolutions[i]);
            added.getItemProperty(resolution_PROPERTY_NAME).setValue(
                    resolutionNames[i]);
        }
        return resolutionContainer;
    }

}
