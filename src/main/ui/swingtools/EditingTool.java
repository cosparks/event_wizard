package ui.swingtools;

import exceptions.InvalidSourceException;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

// Represents an abstract class of event editing tools
public abstract class EditingTool extends JPanel implements Observer, ActionListener {
    public static final int TITLE_HEIGHT = 25;
    public static final int DISPLAY_HEIGHT = MainFrame.TOOL_HEIGHT + 20;
    public static final int BUTTON_WIDTH = MainFrame.TOOL_WIDTH / 4;
    public static final Color POPUP_MENU_BACKGROUND = UIData.GREY_BACKGROUND;
    public static final Color TOOL_LIST_TEXT_COLOUR = UIData.GREY_TEXT;

    protected MainFrame mainFrame;

    protected String type;
    protected String selectedString;
    protected SwingTool st;
    protected JList jlist;
    protected JPanel buttonPanel;
    protected JPanel leftDisplay;

    protected final int width;
    protected final int height;

    // EFFECTS: creates new editing tool with main frame and string for its subclass' type
    public EditingTool(MainFrame mainFrame, String type) {
        this.mainFrame = mainFrame;
        this.width = MainFrame.TOOL_WIDTH;
        this.height = MainFrame.TOOL_HEIGHT;
        this.type = type;
        st = new SwingTool(this);
        jlist = new JList();
        setBackground(UIData.BUTTON_GREY);
        setPreferredSize(new Dimension(width, height));
    }

    // MODIFIES: this
    // EFFECTS: initializes main display panel
    protected void initializeDisplay() {
        JPanel top = new JPanel();
        top.setBackground(UIData.MENU_BACKGROUND);
        top.setPreferredSize(new Dimension(width, TITLE_HEIGHT));
        top.setLayout(new BorderLayout());

        JPanel bottom = new JPanel();
        bottom.setBackground(UIData.DARK_GREY);
        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(MainFrame.TOOL_WIDTH, DISPLAY_HEIGHT));

        leftDisplay = new JPanel();
        leftDisplay.setBackground(UIData.MENU_BACKGROUND);
        leftDisplay.setPreferredSize(new Dimension(width * 3 / 4, DISPLAY_HEIGHT - TITLE_HEIGHT));
        leftDisplay.setLayout(new BorderLayout());

        jlist.setBackground(UIData.BUTTON_GREY);
        jlist.setPreferredSize((new Dimension(width * 3 / 4, DISPLAY_HEIGHT / 2)));
        JComponent label = st.createLabel(" " + type);
        leftDisplay.add(jlist, BorderLayout.NORTH);

        initializeMainButtons();

        bottom.add(buttonPanel, BorderLayout.EAST);
        bottom.add(leftDisplay, BorderLayout.WEST);

        top.add(label, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: initializes main editor buttons for panel on right hand side
    protected void initializeMainButtons() {
        JButton editButton = st.createBoxButton("edit", "edit");
        JButton addNewButton = st.createBoxButton("add new", "addNew");
        JButton removeButton = st.createBoxButton("remove", "remove");

        buttonPanel = new JPanel();
        buttonPanel.setBackground(POPUP_MENU_BACKGROUND);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(width / 4, height - TITLE_HEIGHT));

        buttonPanel.add(editButton);
        buttonPanel.add(addNewButton);
        buttonPanel.add(removeButton);
    }

    // MODIFIES: this
    // EFFECTS: gets string value from currently item in JList and sets selected string
    //          throws InvalidSourceException if nothing is selected
    protected void setSelectedString() throws InvalidSourceException {
        if (Objects.isNull(jlist.getSelectedValue())) {
            throw new InvalidSourceException();
        }
        selectedString = jlist.getSelectedValue().toString();
    }

    // EFFECTS: abstract method to update subclass' JList
    abstract void updateJList();

    // EFFECTS: called by children when editing an item in JList
    protected void startEdit() throws InvalidSourceException {
        setSelectedString();
    }

    // EFFECTS: resets and reinitializes display
    protected void resetToolPanel() {
        st.resetPanel(this);
        updateJList();
        initializeDisplay();
        mainFrame.pack();
    }
}
