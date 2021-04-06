package ui.guitools;

import exceptions.InvalidSourceException;
import model.show.*;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

// Represents a tool for editing a list of acts
public class ActTool extends EditingTool {
    private List<Act> acts;
    private Act actToEdit;

    private JTextField nameField;
    private JPanel editorButtonPanel;
    private JTextField payField;

    // EFFECTS: constructs a new ActTool with mainframe and list of acts
    public ActTool(MainFrame mf, List<Act> acts) {
        super(mf, "acts");
        this.acts = acts;
        updateJList();

        setLayout(new BorderLayout(UIData.BORDER_HGAP, UIData.BORDER_VGAP));

        initializeDisplay();
    }

    // MODIFIES: this
    // EFFECTS: updates JList to include any newly edited or added acts
    @Override
    protected void updateJList() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> pay = new ArrayList<>();
        String[] nameArray = new String[acts.size()];
        String[] payArray = new String[acts.size()];

        for (Act a : acts) {
            names.add(a.getName());
            pay.add(Integer.toString(a.getPay()));
        }

        names.toArray(nameArray);
        pay.toArray(payArray);
        jlist = new JList(nameArray);
        jlist.setForeground(TOOL_LIST_TEXT_COLOUR);
    }

    // MODIFIES: this
    // EFFECTS: starts editor display
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

    // EFFECTS: searches for and returns act with name matching selectedString
    private Act getActToEdit() {
        Act actToEdit = null;
        for (Act a : acts) {
            if (a.getName().equals(selectedString)) {
                actToEdit = a;
            }
        }
        return actToEdit;
    }

    // MODIFIES: this
    // EFFECTS: initializes event display
    private void initializeEditDisplay() {
        actToEdit = getActToEdit();
        String name = actToEdit.getName();
        String pay = Integer.toString(actToEdit.getPay());

        JButton btn = st.createBoxButton("save", "save");

        setEditPanel(name, pay, btn);
    }

    // MODIFIES: this
    // EFFECTS: sets values for new or existing act to be saved
    private void saveAct() {
        try {
            actToEdit.setPay(Integer.parseInt(payField.getText()));
            actToEdit.setName(nameField.getText());
            resetToolPanel();
        } catch (NumberFormatException e) {
            mainFrame.displayErrorMessage("please enter numerical digits only");
        }
    }

    // EFFECTS: calls appropriate methods to start initializing act creator display
    private void startAddNewPanel() {
        st.resetPanel(this);
        initializeCreatorDisplay();
    }

    // EFFECTS: calls appropriate methods to initialize act creator display
    private void initializeCreatorDisplay() {
        JButton createNewButton = st.createBoxButton("create", "create");
        setEditPanel("name", "pay", createNewButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a new act with values specified in payField and nameField
    private void createAndAddAct() {
        int pay;
        String name;
        try {
            pay = Integer.parseInt(payField.getText());
            name = nameField.getText();
            Act act = new Act(name, pay);
            acts.add(act);
            resetToolPanel();
        } catch (NumberFormatException e) {
            mainFrame.displayErrorMessage("please enter valid number for pay");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes selected act from list of acts
    private void removeSelected() {
        try {
            setSelectedString();
            Act act = getActToEdit();
            acts.remove(act);
            resetToolPanel();
        } catch (InvalidSourceException ise) {
            mainFrame.displayErrorMessage();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets edit panel to display fields where user can modify an existing act
    private void setEditPanel(String fieldA, String fieldB, JButton button) {
        JPanel editPanel = new JPanel();
        editPanel.setBackground(POPUP_MENU_BACKGROUND);
        editPanel.setPreferredSize(new Dimension(width * 3 / 4, DISPLAY_HEIGHT / 2));
        editPanel.setLayout(new BorderLayout());

        JPanel grid = new JPanel();
        grid.setBackground(POPUP_MENU_BACKGROUND);
        grid.setLayout(new GridLayout(2, 1));
        grid.setPreferredSize(new Dimension(width - (BUTTON_WIDTH * 2), DISPLAY_HEIGHT / 2));

        editorButtonPanel = new JPanel();
        editorButtonPanel.setBackground(POPUP_MENU_BACKGROUND);
        editorButtonPanel.setLayout(new BoxLayout(editorButtonPanel, BoxLayout.Y_AXIS));
        editorButtonPanel.add(button);

        nameField = new JTextField(fieldA, 0);
        payField = new JTextField(fieldB, 0);
        grid.add(nameField);
        grid.add(payField);
        super.initializeDisplay();
        editPanel.add(grid, BorderLayout.WEST);
        editPanel.add(editorButtonPanel, BorderLayout.EAST);
        leftDisplay.add(editPanel, BorderLayout.SOUTH);
        mainFrame.pack();
    }

    // EFFECTS: processes user input and calls appropriate methods
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "edit":
                startEdit();
                break;
            case "addNew":
                startAddNewPanel();
                break;
            case "remove":
                removeSelected();
                break;
            case "save":
                saveAct();
                break;
            case "create":
                createAndAddAct();
        }
    }
}
