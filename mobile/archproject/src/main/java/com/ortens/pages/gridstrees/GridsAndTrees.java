package com.ortens.pages.gridstrees;

import com.ortens.TouchLayout;
import com.ortens.TouchMenu;
import com.ortens.TouchMenu.TouchCommand;
import com.ortens.TouchMenu.TouchMenuItem;

@SuppressWarnings("serial")
public class GridsAndTrees extends TouchLayout {

    public GridsAndTrees() {

        setCaption("Grids and Trees");

        TouchMenu menu = new TouchMenu();

        menu.addItem("Grids", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Grids());
            }
        });

        menu.addItem("Trees", new TouchCommand() {

            @Override
            public void itemTouched(TouchMenuItem selectedItem) {
                getParent().navigateTo(new Trees());
            }
        });

        addComponent(menu);
    }

}
