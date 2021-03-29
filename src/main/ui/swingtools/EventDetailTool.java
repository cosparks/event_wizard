package ui.swingtools;


import exceptions.InvalidSourceException;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class EventDetailTool extends EditingTool {
    public static final int DETAIL_WIDTH = MainFrame.TOOL_WIDTH * 2 - BUTTON_WIDTH + 5;

    private ArrayList<String> details;
    private JTextField detailField;
    private JPanel editorButtonPanel;
    private boolean editingDetail;


    public EventDetailTool(MainFrame mainFrame, ArrayList<String> details) {
        super(mainFrame, " details");
        this.details = details;
        editingDetail = false;

        updateJList();

        setLayout(new BorderLayout(UIData.BORDER_HGAP, UIData.BORDER_VGAP));

        initializeDisplay();
    }

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

    private void startAddNewPanel() {
        selectedString = "";
        st.resetPanel(this);
        initializeEditDisplay();
        mainFrame.pack();
    }

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

    private void resetPanel() {
        st.resetPanel(this);
        updateJList();
        initializeDisplay();
        mainFrame.pack();
    }

    private void initializeEditDisplay() {
        String detail = selectedString;
        JButton btn = st.createBoxButton("save", "save");
        setEditPanel(detail, btn);
    }

    @Override
    void updateJList() {
        String[] detailArray = new String[details.size()];
        details.toArray(detailArray);
        jlist = new JList(detailArray);
        jlist.setForeground(TOOL_LIST_TEXT_COLOUR);
    }

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

    @Override
    public void update(String actionCommand) {

    }
}
