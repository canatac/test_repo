package com.ortens.pages.basics;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.ortens.pages.basics.buttons.Checkboxes;
import com.ortens.pages.basics.buttons.LinkButtons;
import com.ortens.pages.basics.buttons.PushButtons;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Buttons extends TouchLayout {

    public Buttons() {

        setCaption("Buttons");

        addComponent(new Label("A button is one of the fundamental building"
                + " blocks of any application."));

        TouchMenu menu = new TouchMenu();

        menu.addItem("Push Button", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new PushButtons());
            }
        });

        menu.addItem("Link Button", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new LinkButtons());
            }
        });
        menu.addItem("Checkbox", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Checkboxes());
            }
        });

        addComponent(menu);

    }

}
