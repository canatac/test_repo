package com.ortens;

import java.io.Serializable;

import com.ortens.widgetset.client.ui.VTouchLayout;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.CssLayout;

/**
 * A lightweight layout for use with the TouchPanel. Can contain any other
 * Vaadin components. Extends CssLayout to give the client a possibility to
 * display single item content full screen.
 * 
 * @author mhellber
 * 
 */
@SuppressWarnings("serial")
@ClientWidget(VTouchLayout.class)
public class TouchLayout extends CssLayout implements Serializable {
    protected TouchPanel parentPanel;

    public TouchLayout() {
        this("");
    }

    public TouchLayout(String caption) {
        super();
        setCaption(caption);
    }

    @Override
    public TouchPanel getParent() {
        // TODO Auto-generated method stub
        return (TouchPanel) super.getParent();
    }
}
