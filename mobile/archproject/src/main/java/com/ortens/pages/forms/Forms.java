package com.ortens.pages.forms;


import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Forms extends TouchLayout {

    public Forms() {
        addComponent(new Label("The Form -component provides a convenient way "
                + "to organize related fields visually."));

        TouchMenu menu = new TouchMenu();

        menu.addItem("Form", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new NormalForms());
            }
        });
        menu.addItem("Login form", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new LoginForms());
            }
        });

        addComponent(menu);
    }

}
