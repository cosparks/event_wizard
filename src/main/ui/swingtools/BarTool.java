package ui.swingtools;

import exceptions.InvalidSourceException;
import model.show.Drink;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

// Represents a tool for editing a list of Drinks
public class BarTool extends EditingTool {
    private List<Drink> drinks;
    private Drink drinkToEdit;

    private JPanel editorButtonPanel;
    private JTextField nameField;
    private JTextField costField;
    private JTextField amountField;
    private JTextField salePriceField;


    // EFFECTS: constructs a new BarTool with mainframe and list of acts
    public BarTool(MainFrame mf, List<Drink> drinks) {
        super(mf, "bar");
        this.drinks = drinks;
        updateJList();

        setLayout(new BorderLayout(UIData.BORDER_HGAP, UIData.BORDER_VGAP));

        initializeDisplay();
    }

    // MODIFIES: this
    // EFFECTS: resets this, calls method to initialize edit mode and packs mainframe
    //          catches InvalidSourceException from super.startEditor()
    @Override
    public void startEdit() {
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
    // EFFECTS: displays fields and button to edit existing drink
    private void initializeEditDisplay() {
        drinkToEdit = getDrinkToEdit();

        String name = drinkToEdit.getName();
        String cost = Integer.toString(drinkToEdit.getCost());
        String amount = Integer.toString(drinkToEdit.getAmount());
        String salePrice = Integer.toString(drinkToEdit.getSalePrice());

        JButton btn = st.createBoxButton("save", "save");

        setEditPanel(name, amount, cost, salePrice, btn);
    }

    // MODIFIES: this
    // EFFECTS: resets this and initializes button and fields for add new drink menu
    private void initializeAddNewDrink() {
        st.resetPanel(this);
        JButton createNewButton = st.createBtn("create", "create");
        createNewButton.setMaximumSize(new Dimension(BUTTON_WIDTH, MainFrame.BUTTON_HEIGHT));

        setEditPanel("name", "cost", "amount", "sale price", createNewButton);
    }

    // MODIFIES: this
    // EFFECTS: generates and displays drink editing panel with four fields and button
    private void setEditPanel(String fieldA, String fieldB, String fieldC, String fieldD, JButton button) {
        nameField = new JTextField(fieldA, 0);
        costField = new JTextField(fieldB, 0);
        amountField = new JTextField(fieldC, 0);
        salePriceField = new JTextField(fieldD, 0);

        JPanel editPanel = initializeEditPanel();
        JPanel fieldPanelA = initializeFieldPanel();
        JPanel fieldPanelB = initializeFieldPanel();

        JPanel mainFieldGrid = new JPanel();
        mainFieldGrid.setBackground(POPUP_MENU_BACKGROUND);
        mainFieldGrid.setLayout(new GridLayout(2, 1));
        mainFieldGrid.setPreferredSize(new Dimension(width - (BUTTON_WIDTH * 2), DISPLAY_HEIGHT / 2));

        initializeEditorButtonPanel(button);

        fieldPanelA.add(nameField);
        fieldPanelA.add(costField);
        fieldPanelB.add(amountField);
        fieldPanelB.add(salePriceField);
        mainFieldGrid.add(fieldPanelA);
        mainFieldGrid.add(fieldPanelB);

        super.initializeDisplay();
        editPanel.add(mainFieldGrid, BorderLayout.WEST);
        editPanel.add(editorButtonPanel, BorderLayout.EAST);
        leftDisplay.add(editPanel, BorderLayout.SOUTH);
        mainFrame.pack();
    }

    // MODIFIES: this
    // EFFECTS: initializes portion of editor panel which contains all the fields
    private JPanel initializeFieldPanel() {
        JPanel firstGridPanel = new JPanel();
        firstGridPanel.setLayout(new GridLayout(1, 2));
        firstGridPanel.setBackground(POPUP_MENU_BACKGROUND);
        return firstGridPanel;
    }

    // MODIFIES: this
    // EFFECTS: initializes portion of editor panel which contains the button
    private void initializeEditorButtonPanel(JButton button) {
        editorButtonPanel = new JPanel();
        editorButtonPanel.setBackground(POPUP_MENU_BACKGROUND);
        editorButtonPanel.setLayout(new BoxLayout(editorButtonPanel, BoxLayout.Y_AXIS));
        editorButtonPanel.add(button);
    }

    // MODIFIES: this
    // EFFECTS: initializes main editor panel
    private JPanel initializeEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setBackground(POPUP_MENU_BACKGROUND);
        editPanel.setPreferredSize(new Dimension(width * 3 / 4, DISPLAY_HEIGHT / 2));
        editPanel.setLayout(new BorderLayout());
        return editPanel;
    }

    // MODIFIES: this
    // EFFECTS: saves new values for drinkToEdit and calls reset tool panel; displays error if
    //          user has entered invalid input into any of the drink's fields
    private void saveDrink() {
        try {
            drinkToEdit.setAmount(Integer.parseInt(amountField.getText()));
            drinkToEdit.setCost(Integer.parseInt(costField.getText()));
            drinkToEdit.setSalePrice(Integer.parseInt(salePriceField.getText()));
            drinkToEdit.setName(nameField.getText());
            resetToolPanel();
        } catch (NumberFormatException e) {
            mainFrame.displayErrorMessage("please enter numerical digits for pay");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes drink selected in JList from list of drinks; displays error message if
    //          selection invalid
    private void removeSelected() {
        try {
            setSelectedString();
            Drink drink = getDrinkToEdit();
            drinks.remove(drink);
            resetToolPanel();
        } catch (InvalidSourceException ise) {
            mainFrame.displayErrorMessage();
        }
    }

    // MODIFIES: this
    // EFFECTS: instantiates new drink with values specified in fields, then adds drink to drinks
    //          displays error message if any field input is invalid
    private void createAndAddDrink() {
        int cost;
        int amount;
        int salePrice;
        String name;
        name = nameField.getText();

        try {
            cost = Integer.parseInt(costField.getText());
            amount = Integer.parseInt(amountField.getText());
            salePrice = Integer.parseInt(salePriceField.getText());
            Drink drink = new Drink(name, amount, cost);
            drink.setSalePrice(salePrice);
            drinks.add(drink);
            resetToolPanel();
        } catch (NumberFormatException e) {
            mainFrame.displayErrorMessage("please enter valid number for pay");
        }
    }

    // EFFECTS: returns drink currently selected in JList
    private Drink getDrinkToEdit() {
        Drink drinkToEdit = null;
        for (Drink d : drinks) {
            if (d.getName().equals(selectedString)) {
                drinkToEdit = d;
            }
        }
        return drinkToEdit;
    }

    // specified in superclass
    @Override
    protected void updateJList() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> pay = new ArrayList<>();
        String[] nameArray = new String[drinks.size()];
        String[] payArray = new String[drinks.size()];

        for (Drink d : drinks) {
            names.add(d.getName());
            pay.add(Integer.toString(d.getCost()));
        }

        names.toArray(nameArray);
        pay.toArray(payArray);
        jlist = new JList(nameArray);
        jlist.setForeground(TOOL_LIST_TEXT_COLOUR);
    }

    // EFFECTS: processes user input and calls appropriate methods
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "edit":
                startEdit();
                break;
            case "addNew":
                initializeAddNewDrink();
                break;
            case "remove":
                removeSelected();
                break;
            case "save":
                saveDrink();
                break;
            case "create":
                createAndAddDrink();
        }
    }

    // was supposed to be used for Observer pattern, but wound up not being useful
    @Override
    public void update(String actionCommand) {
        System.out.println("Just received action command: " + actionCommand);
    }
}
