package com.ortens.pages.layoutscontainers.layouts;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Layouts extends TouchLayout {

    public Layouts() {

        setCaption("Layouts");

        addComponent(new Label("Making a usable, good looking, dynamic layout "
                + "can be tricky, but with the right tools almost anything is "
                + "possible."));

        TouchMenu menu = new TouchMenu();

        menu.addItem("Layout margins", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Margins());
            }
        });
        menu.addItem("Layout spacing", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Spacing());
            }
        });
        menu.addItem("Vertical layout", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Vertical());
            }
        });
        menu.addItem("Horizontal layout", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Horizontal());
            }
        });
        menu.addItem("Grid layout", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Grids());
            }
        });
        menu.addItem("Component alignment", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Alignments());
            }
        });

        menu.addItem("Expanding components", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Expanding());
            }
        });

        menu.addItem("Custom layouts", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Custom());
            }
        });
        menu.addItem("CSS Layout", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Css());
            }
        });

        addComponent(menu);
    }
}
