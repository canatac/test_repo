package com.ortens.pages.valueinput;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;
import com.ortens.pages.valueinput.selects.ListSelectMulti;
import com.ortens.pages.valueinput.selects.ListSelectSingle;
import com.ortens.pages.valueinput.selects.OptionGroups;

@SuppressWarnings("serial")
public class Selects extends TouchLayout {

    public Selects() {

        setCaption("Selects");

        TouchMenu menu = new TouchMenu();

        menu.addItem("List select, single selection", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new ListSelectSingle());
            }
        });
        menu.addItem("List select, multiple selection", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new ListSelectMulti());
            }
        });
        menu.addItem("Option Group", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new OptionGroups());
            }
        });

        addComponent(menu);
    }

}
