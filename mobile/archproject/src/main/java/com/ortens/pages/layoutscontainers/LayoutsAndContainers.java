package com.ortens.pages.layoutscontainers;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.ortens.pages.layoutscontainers.layouts.Layouts;

@SuppressWarnings("serial")
public class LayoutsAndContainers extends TouchLayout {

    public LayoutsAndContainers() {

        setCaption("Layouts and Containers");

        TouchMenu menu = new TouchMenu();

        menu.addItem("Layouts", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Layouts());
            }
        });

        menu.addItem("Panels", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Panels());
            }
        });

        menu.addItem("Tabsheets", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Tabsheets());
            }
        });

        menu.addItem("Accordions", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Accordions());
            }
        });

        addComponent(menu);
    }
}
