package com.ortens;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.ortens.widgetset.client.ui.VTouchPanel;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;

/**
 * A panel that makes it easier to make mobile applications with Vaadin. The
 * panel takes care of navigation to different TouchLayouts.
 */
@SuppressWarnings("serial")
@ClientWidget(VTouchPanel.class)
public class TouchPanel extends Panel {

    private final Stack<TouchLayout> viewStack;

    public TouchPanel() {
        super();
        viewStack = new Stack<TouchLayout>();
    }

    public TouchPanel(TouchLayout layout) {
        this();
        navigateTo(layout);
    }

    /**
     * Replaces the content of the TouchLayout with the new component and adds
     * the component's caption as the caption of the TouchLayout. This will take
     * care of animation and handling of the bread crumb.
     * 
     * @param layout
     *            TouchLayout to navigate to
     */
    public void navigateTo(TouchLayout layout) {

        // When navigating to the first layout, we need to take care
        // not to add the default VerticalLayout to the stack
        if (getContent() != null && getContent() instanceof TouchLayout) {
            TouchLayout previousLayout = (TouchLayout) getContent();

            // Setting parent to null for inactive components
            // so they cannot receive events
            previousLayout.setParent(null);

            viewStack.push(previousLayout);
        }
        setTouchLayout(layout);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("captionStack", getCaptionStack());

    }

    private String[] getCaptionStack() {
        Iterator<TouchLayout> layouts = viewStack.iterator();
        String captionStack[] = new String[viewStack.size() + 1];
        int index = 0;
        while (layouts.hasNext()) {
            captionStack[index++] = layouts.next().getCaption();
        }
        // Finally, add the current on top
        captionStack[index] = getContent().getCaption();

        return captionStack;
    }

    /**
     * Navigates back to the previously shown component, if there is one. This
     * should be called when user presses the back button.
     */
    public void navigateBack() {
        setTouchLayout(viewStack.pop());
    }

    /**
     * Handles the changes sent by the client.
     */
    @SuppressWarnings("unchecked")
    // Panel has no type arguments, cannot override it with type arguments
    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);

        // Sanity check: is there a view to go back to?
        if (variables.containsKey("goback") && !viewStack.empty()) {
            navigateBack();
        }
    }

    @Override
    public void setContent(ComponentContainer c) {
        if (c instanceof TouchLayout) {
            navigateTo((TouchLayout) c);
        } else if (c == null) {
            // Empty panels call setContent(null) in constructor..
            super.setContent(null);
        } else {
            throw new IllegalArgumentException("You can only add TouchLayouts "
                    + "as root elements in a TouchPanel");
        }
    }

    private void setTouchLayout(TouchLayout layout) {
        super.setContent(layout);
    }
}
