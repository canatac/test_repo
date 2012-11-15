package com.ortens.pages.basics;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class UiBasics extends TouchLayout {

    public UiBasics() {

        setCaption("UI Basics");

        addComponent(new Label(
                "These are some of the basic UI building blocks."));

        TouchMenu menu = new TouchMenu();

        menu.addItem("Buttons", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Buttons());
            }
        });

        menu.addItem("Links", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Links());
            }
        });
        menu.addItem("Texts", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Texts());
            }
        });
        menu.addItem("Images", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Images());
            }
        });

        menu.addItem("Icons", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Icons());
            }
        });
        menu.addItem("Progress Indicator", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new ProgIndicator());
            }
        });
        menu.addItem("JavaScript API", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new JSAPI());
            }
        });

        addComponent(menu);

    }
}
