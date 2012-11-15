package com.ortens.pages.valueinput;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class ValueInputComponents extends TouchLayout {

    public ValueInputComponents() {

        setCaption("Value Input Components");

        addComponent(new Label("Components for inputting values"));

        TouchMenu menu = new TouchMenu();

        menu.addItem("Dates", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Dates());
            }
        });
        menu.addItem("Text Input", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new TextInput());
            }
        });
        menu.addItem("Selects", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Selects());
            }
        });

        addComponent(menu);
    }
}
