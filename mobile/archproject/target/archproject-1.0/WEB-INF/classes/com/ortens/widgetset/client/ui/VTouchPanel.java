package com.ortens.widgetset.client.ui;

import java.util.Set;
import java.util.Stack;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * The main component of the Touch interface. Consists of a header and a content
 * section. The content section takes in a TouchLayout and animates the
 * transition for supported devices like the iPhone.
 * 
 * @author mhellber
 * 
 */
public class VTouchPanel extends Composite implements Container, ClickHandler {
    public static final String CLICK_EVENT_IDENTIFIER = "click";

    public static final String CLASSNAME = "v-mobilelayout";

    protected FlowPanel container;
    protected FlowPanel header;

    // There are two sets of the content, back button and caption
    // so that they can be animated.
    protected ContentPanel activeContent;
    protected ContentPanel inactiveContent;
    protected BackButton activeButton;
    protected BackButton inactiveButton;
    protected Label activeCaption;
    protected Label inactiveCaption;

    protected boolean animationsSupported;
    protected boolean readyToProcessRequests = true;
    protected boolean animationsRunning = false;

    protected Paintable currentPaintable;
    protected UIDL queuedUIDL;

    // Stack for keeping previous captions so that they can be
    // animated in before server response is received.
    protected Stack<String> captions;

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    public VTouchPanel() {
        createLayout();
        animationsSupported = animationsSupported();
        captions = new Stack<String>();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        registerOrientationChangeEvent();
    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

        // This call should be made first.
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, true)) {
            // If client.updateComponent returns true
            // there has been no changes and we
            // do not need to update anything.
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the client side identifier (paintable id) for the widget
        paintableId = uidl.getId();

        final UIDL touchLayoutUIDL = uidl.getChildUIDL(0);
        final Paintable touchLayoutWidget = client
                .getPaintable(touchLayoutUIDL);

        swapCurrentPaintable(touchLayoutWidget);

        boolean animateFirstPage;
        if (captions.isEmpty()) {
            if (uidl.hasAttribute("captionStack")) {
                fillCaptionStack(uidl.getStringArrayAttribute("captionStack"));
            }
            animateFirstPage = false;
        } else {
            animateFirstPage = true;
        }
        // First page is special, it does not animate
        if (!animateFirstPage) {
            navigateForwardWithoutAnimation();
        }

        if (animationsRunning) {
            queuedUIDL = touchLayoutUIDL;
        } else {
            addNewWidget(touchLayoutUIDL);
        }
    }

    private void fillCaptionStack(String[] newCaptions) {
        for (int i = 0; i < newCaptions.length; i++) {
            captions.push(newCaptions[i]);
        }
    }

    private void addNewWidget(UIDL uidl) {
        Widget widget = (Widget) currentPaintable;
        attachNewActiveTouchLayout(widget);
        currentPaintable.updateFromUIDL(uidl, client);
        readyToProcessRequests = true;
    }

    private void addQueuedWidget() {
        if (queuedUIDL != null) {
            addNewWidget(queuedUIDL);
            queuedUIDL = null;
        }
    }

    private void swapCurrentPaintable(Paintable touchLayoutWidget) {
        unregisterCurrentPaintable();
        currentPaintable = touchLayoutWidget;
    }

    private void unregisterCurrentPaintable() {
        if (currentPaintable != null) {
            client.unregisterPaintable(currentPaintable);
        }
    }

    /* Layout methods */

    private void createLayout() {
        createContainer();
        createHeader();
        createContentPanels();
        fillContainer();
        initWidget(container);
        createHiddenPreloadDiv();
    }

    private void fillContainer() {
        container.add(header);
        container.add(activeContent);
        container.add(inactiveContent);
    }

    private void createContentPanels() {
        activeContent = new ContentPanel();
        activeContent.setStyleName(CLASSNAME + "-content");
        inactiveContent = new ContentPanel();
        inactiveContent.setStyleName(CLASSNAME + "-content");
    }

    private void createContainer() {
        container = new FlowPanel();
        container.setStyleName(CLASSNAME);
    }

    private void createHeader() {
        header = new FlowPanel();
        header.setStyleName(CLASSNAME + "-header");

        createButtons();
        createCaptions();
        addButtonsToHeader();
        addCaptionsToHeader();
    }

    private void addCaptionsToHeader() {
        header.add(activeCaption);
        header.add(inactiveCaption);
    }

    private void addButtonsToHeader() {
        header.add(activeButton);
        header.add(inactiveButton);
    }

    private void createCaptions() {
        activeCaption = new Label("");
        activeCaption.setStyleName(CLASSNAME + "-caption");
        inactiveCaption = new Label("");
        inactiveCaption.setStyleName(CLASSNAME + "-caption");
        inactiveCaption.addStyleName("left");
    }

    private void createButtons() {
        activeButton = new BackButton("");
        activeButton.setStyleName(CLASSNAME + "-button");
        activeButton.setVisible(false);
        activeButton.addClickHandler(this);

        inactiveButton = new BackButton("");
        inactiveButton.setStyleName(CLASSNAME + "-button");
        inactiveButton.setVisible(false);
        inactiveButton.addClickHandler(this);
    }

    private void createHiddenPreloadDiv() {
        Element preloader = Document.get().createDivElement().cast();
        preloader.setClassName("preload");
        getElement().appendChild(preloader);
    }

    /* Container methods */

    @Override
    public RenderSpace getAllocatedSpace(Widget child) {

        return new RenderSpace(-1, -1) {
            @Override
            public int getWidth() {
                // We do not want content to get wider than the view
                return activeContent.getOffsetWidth() - 20; // HACK: 10px
                // margins
            }

            @Override
            public int getHeight() {
                // We do not restrict content growth vertically
                return -1;
            }
        };
    }

    @Override
    public boolean hasChildComponent(Widget component) {
        return component.equals(currentPaintable);
    }

    @Override
    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        // Not needed in TouchKit because the child component is always a
        // TouchLayout
    }

    @Override
    public boolean requestLayout(Set<Paintable> children) {
        return true;
    }

    @Override
    public void updateCaption(Paintable component, UIDL uidl) {
        // Handled in updateFromUIDL
    }

    /* Navigation methods */

    /**
     * Listens to clicks from the back button. It starts the navigation of new
     * content and notifies the server.
     */
    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == activeButton && readyToProcessRequests) {
            navigateBack();
            client.updateVariable(paintableId, "goback", true, true);
        }

    }

    /**
     * Adds received content to the appropriate container and removes loading
     * style of the container.
     * 
     * @param w
     *            Widget to add
     */
    private void attachNewActiveTouchLayout(Widget w) {
        setLoading(activeContent, false);
        activeContent.add(w);
    }

    /**
     * Clears out old content and sets loading CSS class for the container that
     * is waiting for content.
     */
    private void clearOldContent() {
        if (animationsSupported) {
            inactiveContent.clear();
            // setLoading(inactiveContent, true);
        } else {
            activeContent.clear();
            setLoading(activeContent, true);
        }
    }

    /**
     * Starts navigation forward (right) to the next content. The given caption
     * is used to update the caption and back buttons.
     * 
     * @param caption
     */
    public void navigateForward() {
        if (readyToProcessRequests) {
            readyToProcessRequests = false;
            Window.scrollTo(0, 0);
            clearOldContent();

            if (animationsSupported) {
                animationsRunning = true;
                navigateForwardWithAnimation();
            } else {
                navigateForwardWithoutAnimation();
            }
        }
    }

    /**
     * Starts navigation back to the previous content.
     */
    public void navigateBack() {
        if (readyToProcessRequests) {
            readyToProcessRequests = false;
            Window.scrollTo(0, 0);
            clearOldContent();

            if (animationsSupported) {
                animationsRunning = true;
                navigateBackWithAnimation();
            } else {
                navigateBackWithoutAnimation();
            }
            captions.pop();
        }
    }

    private void navigateBackWithoutAnimation() {
        clearOldContent();
        navigateHeaderBack();
    }

    private void navigateForwardWithoutAnimation() {
        clearOldContent();
        navigateHeaderForward();
    }

    /**
     * Animates in an empty page. Used to make interface feel more responsive
     * while waiting for server response. Running this will swap the active and
     * inactive containers, buttons and content.
     */
    public void navigateForwardWithAnimation() {
        // Animate header
        final Label newCaption = inactiveCaption;
        final Label oldCaption = activeCaption;

        final BackButton newButton = inactiveButton;
        final BackButton oldButton = activeButton;

        final String buttonText = getPreviousCaption();

        final boolean hasButton = !buttonText.equals("");

        newButton.setText(buttonText);
        newButton.removeStyleName("animate");
        newButton.removeStyleName("left");
        newButton.addStyleName("right");

        newCaption.setText(getCurrentCaption());
        newCaption.removeStyleName("animate");
        newCaption.removeStyleName("left");
        newCaption.addStyleName("right");

        // Animate Content
        final ContentPanel oldContent = activeContent;
        final ContentPanel newContent = inactiveContent;

        // New content starts out on the right side of the screen
        newContent.removeStyleName("animate");
        newContent.removeStyleName("left");
        newContent.addStyleName("right");

        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                // TODO: Refactor these to separate methods, unnecessary
                // duplication
                oldCaption.addStyleName("animate");
                oldCaption.removeStyleName("right");
                oldCaption.addStyleName("left");

                newCaption.addStyleName("animate");
                newCaption.removeStyleName("left");
                newCaption.removeStyleName("right");

                oldButton.addStyleName("animate");
                oldButton.removeStyleName("right");
                oldButton.addStyleName("left");

                if (hasButton) {
                    newButton.setVisible(true);
                    newButton.addStyleName("animate");
                    newButton.removeStyleName("left");
                    newButton.removeStyleName("right");
                }

                oldContent.addStyleName("animate");
                oldContent.removeStyleName("right");
                oldContent.addStyleName("left");

                newContent.addStyleName("animate");
                newContent.removeStyleName("left");
                newContent.removeStyleName("right");
            }
        });

        activeContent = newContent;
        inactiveContent = oldContent;
        activeButton = newButton;
        inactiveButton = oldButton;
        activeCaption = newCaption;
        inactiveCaption = oldCaption;
    }

    private String getCurrentCaption() {
        return captions.peek();
    }

    /**
     * Animates in an empty page. Used to make interface feel more responsive
     * while waiting for server response. Running this will swap the active and
     * inactive containers, buttons and content.
     */
    public void navigateBackWithAnimation() {
        // Animate Header
        final Label newCaption = inactiveCaption;
        final Label oldCaption = activeCaption;

        final BackButton newButton = inactiveButton;
        final BackButton oldButton = activeButton;

        final String captionText = getPreviousCaption();
        final String buttonText = getPreviousBackButtonText();

        final boolean hasButton = !buttonText.equals("");

        // Animate header
        newCaption.setText(captionText);

        newButton.setText(buttonText);
        newButton.removeStyleName("animate");
        newButton.addStyleName("left");
        newButton.removeStyleName("right");

        newCaption.setText(captionText);
        newCaption.removeStyleName("animate");
        newCaption.addStyleName("left");
        newCaption.removeStyleName("right");

        // Animate Content
        final ContentPanel oldContent = activeContent;
        final ContentPanel newContent = inactiveContent;

        // New content starts out at the left side of the screen
        newContent.removeStyleName("animate");
        newContent.addStyleName("left");
        newContent.removeStyleName("right");

        // Start animations at the same time
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                oldButton.addStyleName("animate");
                oldButton.removeStyleName("left");
                oldButton.addStyleName("right");

                if (hasButton) {
                    newButton.setVisible(true);
                    newButton.addStyleName("animate");
                    newButton.removeStyleName("left");
                    newButton.removeStyleName("right");
                }

                oldCaption.addStyleName("animate");
                oldCaption.removeStyleName("left");
                oldCaption.addStyleName("right");

                newCaption.addStyleName("animate");
                newCaption.removeStyleName("left");
                newCaption.removeStyleName("right");

                oldContent.addStyleName("animate");
                oldContent.removeStyleName("left");
                oldContent.addStyleName("right");

                newContent.addStyleName("animate");
                newContent.removeStyleName("left");
                newContent.removeStyleName("right");
            }
        });

        activeContent = newContent;
        inactiveContent = oldContent;
        activeButton = newButton;
        inactiveButton = oldButton;
        activeCaption = newCaption;
        inactiveCaption = oldCaption;
    }

    private void navigateHeaderForward() {
        activeCaption.setText(getCurrentCaption());

        if (hasPreviousCaption()) {
            activeButton.setVisible(true);
            activeButton.setText(getPreviousCaption());
        } else {
            activeButton.setVisible(false);
        }
    }

    private void navigateHeaderBack() {
        updateBackButtonGoingBack();
        updateCaptionGoingBack();
    }

    private void updateCaptionGoingBack() {
        activeCaption.setText(getPreviousCaption());
    }

    private void updateBackButtonGoingBack() {
        String buttonText = getPreviousBackButtonText();
        if (!buttonText.equals("")) {
            activeButton.setVisible(true);
            activeButton.setText(buttonText);
        } else {
            activeButton.setVisible(false);
        }
    }

    /* Caption stack handling */

    private boolean hasPreviousCaption() {
        // Current is on top, hence > 1
        return captions.size() > 1;
    }

    /**
     * Returns the caption for the previous page without changing the caption
     * stack.
     * 
     * @return Previous caption or empty string if none
     */
    private String getPreviousCaption() {
        String previousCaption = "";

        if (!captions.isEmpty()) {
            String currentCaption = captions.pop();

            if (!captions.isEmpty()) {
                previousCaption = captions.peek();
            }

            captions.push(currentCaption);
        }
        return previousCaption;
    }

    /**
     * Returns the back button caption for the previous page without changing
     * the caption stack.
     * 
     * @return Previous back button caption or empty string if none
     */
    private String getPreviousBackButtonText() {
        String previousBackButtonText = "";

        if (!captions.isEmpty()) {
            String currentCaption = captions.pop();

            if (!captions.isEmpty()) {
                String previousCaption = captions.pop();

                if (!captions.isEmpty()) {
                    previousBackButtonText = captions.peek();
                }

                captions.push(previousCaption);
            }

            captions.push(currentCaption);
        }
        return previousBackButtonText;
    }

    /**
     * Called by contained TouchMenu when an item is selected.
     */
    public void menuTapped(String caption) {
        captions.push(caption);
        navigateForward();
    }

    /* Methods for transitions and iPhone specific stuff */

    /**
     * Determines if the client supports animations.
     */
    private static boolean animationsSupported() {
        BrowserInfo info = BrowserInfo.get();
        boolean supported = false;

        // Supported in new webkits and gecko browsers
        if (info.isWebkit() && info.getWebkitVersion() > 522.15) {

            // Disabling android animations for now as they are not
            // hardware accelerated, so they are painfully slow.
            if (BrowserInfo.getBrowserString().indexOf("Android") < 0) {
                supported = true;
            }
        } else if (info.isGecko() && info.getGeckoVersion() > 1.9) {
            supported = true;
        }

        return supported;
    }

    /**
     * Adds/removes loading CSS class from a widget.
     * 
     * @param w
     * @param loading
     */
    private static void setLoading(Widget w, boolean loading) {
        w.removeStyleName("loading");
        if (loading) {
            w.addStyleName("loading");
        }
    }

    private void animationsCompleted() {
        // Only run these once, even if more than one component call the method
        if (animationsRunning) {
            activeButton.removeStyleName("animate");
            activeCaption.removeStyleName("animate");
            inactiveButton.removeStyleName("animate");
            inactiveCaption.removeStyleName("animate");
            activeContent.removeStyleName("animate");
            inactiveContent.removeStyleName("animate");

            animationsRunning = false;

            // If a request from a server has come during the animation, it has
            // been
            // queued. Add a small delay after the animation has finished to
            // reduce
            // flickering appearance
            if (queuedUIDL != null) {
                Timer t = new Timer() {
                    @Override
                    public void run() {
                        addQueuedWidget();
                    }
                };
                t.schedule(200);
            }
        }
    }

    public native void registerOrientationChangeEvent()
    /*-{
        var touchPanel = this;
        var orientationChangeCallback = function() {
            touchPanel.@com.ortens.widgetset.client.ui.VTouchPanel::orientationChanged()();
        }
        
        $wnd.addEventListener("orientationchange", orientationChangeCallback, false);
    }-*/;

    public native void unregisterOrientationChangeEvent()
    /*-{
        $wnd.removeEventListener("orientationchange", orientationChangeCallback, false);
     }-*/;

    public void orientationChanged() {
        client.handleComponentRelativeSize(this);
        client.updateVariable(paintableId, "height", getOffsetHeight(), false);
        client.updateVariable(paintableId, "width", getOffsetWidth(), true);
    }

    @Override
    protected void onUnload() {
        unregisterOrientationChangeEvent();
        super.onUnload();
    }

    /**
     * A simple button class for the back button. The GWT buttons had issues on
     * android devices and native buttons did not allow CSS styling.
     * 
     * @author mhellber
     * 
     */
    private class BackButton extends Label {

        @SuppressWarnings("unused")
        public BackButton() {
            super();
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            registerTouchEvents();
        }

        public BackButton(String text) {
            super(text);
        }

        @SuppressWarnings("unused")
        public BackButton(String text, ClickHandler handler) {
            this(text);
            addClickHandler(handler);
        }

        // Called from JSNI
        @SuppressWarnings("unused")
        public void onTouchStart() {
            addStyleName("active");
        }

        // Called from JSNI
        @SuppressWarnings("unused")
        public void onTouchEnd() {
            removeStyleName("active");
        }

        private native void registerTouchEvents()
        /*-{
             var backbutton = this;
             var element = this.@com.ortens.widgetset.client.ui.VTouchPanel$BackButton::getElement()();
             
             element.ontouchstart = function(e) {
                 backbutton.@com.ortens.widgetset.client.ui.VTouchPanel$BackButton::onTouchStart()();
             }
             
             element.ontouchend = function(e) {
                 backbutton.@com.ortens.widgetset.client.ui.VTouchPanel$BackButton::onTouchEnd()();
             }
             
        }-*/;

        private native void unregisterTouchEvents()
        /*-{
           var element = this.@com.ortens.widgetset.client.ui.VTouchPanel$BackButton::getElement()();
           
           element.ontouchstart = null;
           
           element.ontouchend = null;
        }-*/;

        @Override
        protected void onUnload() {
            unregisterTouchEvents();
            super.onUnload();
        }
    }

    private class ContentPanel extends FlowPanel {

        @Override
        protected void onLoad() {
            super.onLoad();
            registerTransitionEvents();
        }

        private native void registerTransitionEvents()
        /*-{
             var panel = this;
             var element = this.@com.ortens.widgetset.client.ui.VTouchPanel$BackButton::getElement()();
             
             var transitionEndCallback = function(e) {
                 panel.@com.ortens.widgetset.client.ui.VTouchPanel$ContentPanel::onTransitionEnd()();
             }
             
             element.addEventListener('webkitTransitionEnd', transitionEndCallback, false);
        }-*/;

        private native void unregisterTransitionEvents()
        /*-{
           var element = this.@com.ortens.widgetset.client.ui.VTouchPanel$BackButton::getElement()();
           
           element.removeEventListener('webkitTransitionEnd', transitionEndCallback, false);
        }-*/;

        // Called from JSNI
        @SuppressWarnings("unused")
        public void onTransitionEnd() {
            animationsCompleted();
        }

        @Override
        protected void onUnload() {
            unregisterTransitionEvents();
            super.onUnload();
        }
    }
}
