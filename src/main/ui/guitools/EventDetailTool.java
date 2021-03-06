package ui.guitools;


import exceptions.InvalidSourceException;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

// represents a tool for editing details of a simple event
public class EventDetailTool extends EditingTool {
    public static final int DETAIL_WIDTH = MainFrame.TOOL_WIDTH * 2 - BUTTON_WIDTH + 5;

    private ArrayList<String> details;
    private JTextField detailField;
    private JPanel editorButtonPanel;
    private boolean editingDetail;

    // EFFECTS: constructs new event detail tool with mainframe and string list of details from simple event
    public EventDetailTool(MainFrame mainFrame, ArrayList<String> details) {
        super(mainFrame, " details");
        this.details = details;
        editingDetail = false;

        updateJList();

        setLayout(new BorderLayout(UIData.BORDER_HGAP, UIData.BORDER_VGAP));

        initializeDisplay();
    }

    // MODIFIES: this
    // EFFECTS: creates jpanels to display event details and editing buttons, then adds them to this JPanel
    @Override
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
        leftDisplay.setPreferredSize(new Dimension(DETAIL_WIDTH, DISPLAY_HEIGHT - TITLE_HEIGHT));
        leftDisplay.setLayout(new BorderLayout());

        jlist.setBackground(UIData.BUTTON_GREY);
        jlist.setPreferredSize((new Dimension(DETAIL_WIDTH, DISPLAY_HEIGHT / 2)));
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
    // EFFECTS: initializes editor panel, displaying JField containing text of detail and button for updating detail
    //          once the user is finished editing; packs mainframe
    private void setEditPanel(String fieldA, JButton button) {
        JPanel editPanel = new JPanel();
        editPanel.setBackground(POPUP_MENU_BACKGROUND);
        editPanel.setPreferredSize(new Dimension(DETAIL_WIDTH, DISPLAY_HEIGHT / 2));
        editPanel.setLayout(new BorderLayout());

        JPanel grid = new JPanel();
        grid.setBackground(POPUP_MENU_BACKGROUND);
        grid.setLayout(new GridLayout(2, 1));
        grid.setPreferredSize(new Dimension(DETAIL_WIDTH - BUTTON_WIDTH + 5, DISPLAY_HEIGHT / 2));

        editorButtonPanel = new JPanel();
        editorButtonPanel.setBackground(POPUP_MENU_BACKGROUND);
        editorButtonPanel.setLayout(new BoxLayout(editorButtonPanel, BoxLayout.Y_AXIS));
        editorButtonPanel.add(button);

        detailField = new JTextField(fieldA, 0);
        grid.add(detailField);

        initializeDisplay();

        editPanel.add(grid, BorderLayout.WEST);
        editPanel.add(editorButtonPanel, BorderLayout.EAST);
        leftDisplay.add(editPanel, BorderLayout.SOUTH);
        mainFrame.pack();
    }

    // MODIFIES: this
    // EFFECTS: sets selected string to empty and calls methods to reset panel and display editor mode for new detail
    private void startAddNewPanel() {
        selectedString = "";
        st.resetPanel(this);
        initializeEditDisplay();
        mainFrame.pack();
    }

    // MODIFIES: this
    // EFFECTS: calls methods to edit detail currently selected in JList; catches InvalidSourceException and displays
    //          error message if nothing is currently selected
    @Override
    protected void startEdit() {
        try {
            super.startEdit();
            st.resetPanel(this);
            initializeEditDisplay();
            mainFrame.pack();
        } catch (InvalidSourceException ise) {
            mainFrame.displayErrorMessage();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes currently selected detail from list and adds new/edited in its place, at the same index;
    //          resets panel
    private void saveDetail() {
        if (editingDetail) {
            int index = details.indexOf(selectedString);
            details.remove(selectedString);
            details.add(index, detailField.getText());
            editingDetail = false;
        } else {
            details.add(detailField.getText());
        }

        resetPanel();
    }

    // MODIFIES: this
    // EFFECTS: sets selected string to the one currently selected in JList and removes it from details; catches
    //          InvalidSourceException and displays error message if user hasn't selected anything
    private void removeSelected() {
        try {
            setSelectedString();
            int index = details.indexOf(selectedString);
            details.remove(index);
            resetPanel();
        } catch (InvalidSourceException ise) {
            mainFrame.displayErrorMessage("please make a valid selection");
        }
    }

    // MODIFIES: this
    // EFFECTS: calls methods to reset this JPanel, update JList, initialize display and packs mainframe
    private void resetPanel() {
        st.resetPanel(this);
        updateJList();
        initializeDisplay();
        mainFrame.pack();
    }

    // MODIFIES: this
    // EFFECTS: initializes save button and calls method to edit currently selected string
    private void initializeEditDisplay() {
        String detail = selectedString;
        JButton btn = st.createBoxButton("save", "save");
        setEditPanel(detail, btn);
    }

    // specified in superclass
    @Override
    void updateJList() {
        String[] detailArray = new String[details.size()];
        details.toArray(detailArray);
        jlist = new JList(detailArray);
        jlist.setForeground(TOOL_LIST_TEXT_COLOUR);
    }

    // MODIFIES: this
    // EFFECTS: processes user input, sets editingDetail and selects appropriate methods
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "edit":
                editingDetail = true;
                startEdit();
                break;
            case "addNew":
                editingDetail = false;
                startAddNewPanel();
                break;
            case "remove":
                editingDetail = false;
                removeSelected();
                break;
            case "save":
                saveDetail();
        }
    }
}
