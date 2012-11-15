package com.ortens.pages.basics;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.ortens.pages.basics.texts.PlainText;
import com.ortens.pages.basics.texts.PreformattedText;
import com.ortens.pages.basics.texts.RichText;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Texts extends TouchLayout {

    public Texts() {

        setCaption("Texts");

        addComponent(new Label("A label is a simple "
                + "component that allows you "
                + "to add (optionally formatted) "
                + "text components to your application"));

        TouchMenu menu = new TouchMenu();

        menu.addItem("Plain text Label", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new PlainText());
            }
        });
        menu.addItem("Preformatted Label", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new PreformattedText());
            }
        });
        menu.addItem("Rich Text Label", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new RichText());
            }
        });

        addComponent(menu);
    }
}
