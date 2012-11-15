package com.ortens.pages.valueinput;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.ortens.pages.valueinput.dates.InlineDate;
import com.ortens.pages.valueinput.dates.LocaleDate;
import com.ortens.pages.valueinput.dates.ResolutionDate;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Dates extends TouchLayout {

    public Dates() {

        setCaption("Dates");

        addComponent(new Label(
                "The DateField component can be used to "
                        + "produce various date and time input fields with different "
                        + "resolutions. The date and time format used with this component "
                        + "is reported to Vaadin by the browser."));

        TouchMenu menu = new TouchMenu();

        menu.addItem("Inline date selection", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new InlineDate());
            }
        });
        menu.addItem("Date selection with locale", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new LocaleDate());
            }
        });
        menu.addItem("Date selection with resolution", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new ResolutionDate());
            }
        });

        addComponent(menu);
    }

}
