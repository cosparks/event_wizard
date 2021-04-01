package ui.swingtools;

import model.Show;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// represents a tool for editing additional details of Show
public class ShowDetailTool extends JPanel implements ActionListener, Observer {
    private final int width;
    private final int height;

    private Show show;
    private MainFrame mainFrame;
    private SwingTool st;

    private JPanel editorPanel;
    private JPanel buttonPanel;
    private JTextField capacityField;
    private JTextField ticketPriceField;
    private JTextField projectedSalesField;
    private JTextField additionalCostField;
    private JLabel capacity;
    private JLabel ticketPrice;
    private JLabel projectedSales;
    private JLabel additionalCost;
    private JLabel revenue;

    // EFFECTS: constructs a ShowDetailTool with mainframe and show
    public ShowDetailTool(MainFrame mainFrame, Show show) {
        this.show = show;
        this.mainFrame = mainFrame;
        this.width = MainFrame.TOOL_WIDTH;
        this.height = mainFrame.TOOL_HEIGHT;
        st = new SwingTool(this);

        setBackground(UIData.BUTTON_GREY);
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        initializeLabels();
        initializeDisplay();
    }

    // MODIFIES: this
    // EFFECTS: sets all JLabels to display appropriate text, and calls set revenue method to display revenue
    private void initializeLabels() {
        capacity = st.createLabel("capacity: ");
        ticketPrice = st.createLabel("door: ");
        projectedSales = st.createLabel("projected turnout: ");
        additionalCost = st.createLabel("extra costs: ");
        revenue = st.createLabel("revenue: ");
        setRevenue();
    }

    // EFFECTS: initializes labels to display appropriate numerical values from show, and calls methods to generate
    //          main display panels
    private void initializeDisplay() {
        JLabel capacityValue = st.createLabel(Integer.toString(show.getCapacity()));
        JLabel ticketPriceValue = st.createLabel("$" + show.getTicketPrice());
        JLabel projectedSalesValue = st.createLabel(Integer.toString(show.getProjectedSales()));
        JLabel additionalCostValue = st.createLabel("$" + show.getAdditionalCost());

        setRevenue();

        JButton btn = st.createBoxButton("edit", "edit");
        setButtonPanel(btn);

        JLabel emptyA = new JLabel();

        JPanel topGrid = createFourColumnGrid(capacity, capacityValue, ticketPrice, ticketPriceValue);
        JPanel middleGrid = createThreeColumnGrid(projectedSales, projectedSalesValue, revenue);
        JPanel bottomGrid = createFourColumnGrid(additionalCost, additionalCostValue, emptyA, buttonPanel);

        setDisplay(topGrid, middleGrid, bottomGrid);
    }

    // MODIFIES: this
    // EFFECTS: calls methods to reset panel set revenue and JFields for editor, then creates and adds display panels
    //          to main display
    private void startEditMode() {
        st.resetPanel(this);
        setRevenue();

        setFieldsForEditor();
        JLabel emptyA = new JLabel();
        JButton btn = st.createBoxButton("save", "save");
        setButtonPanel(btn);

        JPanel topGrid = createFourColumnGrid(capacity, capacityField, ticketPrice, ticketPriceField);
        JPanel middleGrid = createThreeColumnGrid(projectedSales, projectedSalesField, revenue);
        JPanel bottomGrid = createFourColumnGrid(additionalCost, additionalCostField, emptyA, buttonPanel);

        setDisplay(topGrid, middleGrid, bottomGrid);

        mainFrame.pack();
    }

    // MODIFIES: this
    // EFFECTS: adds 3 input panels to main editor panel, then adds editor panel to this
    private void setDisplay(JPanel topGrid, JPanel middleGrid, JPanel bottomGrid) {
        JPanel top = setTitle();

        initializeEditorPanel();

        editorPanel.add(topGrid);
        editorPanel.add(middleGrid);
        editorPanel.add(bottomGrid);

        add(top, BorderLayout.NORTH);
        add(editorPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: resets revenue label to display current projected revenue for show
    public void setRevenue() {
        int i = show.calculateRevenue();
        revenue.setText("revenue: $" + i);
    }

    // MODIFIES: this
    // EFFECTS: sets all JFields to display numerical values from this show
    private void setFieldsForEditor() {
        capacityField = new JTextField(Integer.toString(show.getCapacity()));
        ticketPriceField = new JTextField(Integer.toString(show.getTicketPrice()));
        projectedSalesField = new JTextField(Integer.toString(show.getProjectedSales()));
        additionalCostField = new JTextField(Integer.toString(show.getAdditionalCost()));
    }

    // MODIFIES: this, btn
    // EFFECTS: sets graphics for btn and adds it to buttonPanel
    private void setButtonPanel(JButton btn) {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(UIData.GREY_BACKGROUND);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(btn);
    }

    // EFFECTS: sets graphics for, adds title label and returns top-most JPanel
    private JPanel setTitle() {
        JPanel top = new JPanel();
        top.setBackground(UIData.MENU_BACKGROUND);
        top.setPreferredSize(new Dimension(width, EditingTool.TITLE_HEIGHT));
        top.setLayout(new BorderLayout());

        JComponent label = st.createLabel(" additional details");

        top.add(label, BorderLayout.WEST);

        return top;
    }

    // MODIFIES: this
    // EFFECTS: resets editorPanel with display graphics
    private void initializeEditorPanel() {
        editorPanel = new JPanel();
        editorPanel.setBackground(UIData.MENU_BACKGROUND);
        editorPanel.setLayout(new GridLayout(3, 1));
        editorPanel.setPreferredSize(new Dimension(MainFrame.TOOL_WIDTH, EditingTool.DISPLAY_HEIGHT));
    }

    // MODIFIES: this
    // EFFECTS: calls methods to reset editor panel, and packs mainFrame
    private void resetEditorPanel() {
        st.resetPanel(this);
        initializeDisplay();
        mainFrame.pack();
    }

    // EFFECTS: initializes and returns JPanel with grid layout with 1 row and 4 columns; adds four JComponents
    private JPanel createFourColumnGrid(JComponent a, JComponent b, JComponent c, JComponent d) {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 4));
        gridPanel.setBackground(UIData.GREY_BACKGROUND);

        gridPanel.add(a);
        gridPanel.add(b);
        gridPanel.add(c);
        gridPanel.add(d);

        return gridPanel;
    }

    // EFFECTS: initializes and returns JPanel with grid layout with 1 row and 4 columns; adds three JComponents
    private JPanel createThreeColumnGrid(JComponent a, JComponent b, JComponent c) {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 3));
        gridPanel.setBackground(UIData.MENU_BACKGROUND);

        gridPanel.add(a);
        gridPanel.add(b);
        gridPanel.add(c);

        return gridPanel;
    }

    // MODIFIES: this
    // EFFECTS: sets show details to those specified in JFields and calls method to reset display; displays error
    //          message if NumberFormatException is caught
    private void setAdditionalDetails() {
        try {
            show.setCapacity(Integer.parseInt(capacityField.getText()));
            show.setTicketPrice(Integer.parseInt(ticketPriceField.getText()));
            show.setProjectedSales(Integer.parseInt(projectedSalesField.getText()));
            show.setAdditionalCost(Integer.parseInt(additionalCostField.getText()));
            resetEditorPanel();
        } catch (NumberFormatException e) {
            mainFrame.displayErrorMessage("please enter numerical digits only");
        }
    }

    // EFFECTS: processes user input and calls appropriate methods
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "edit":
                startEditMode();
                break;
            case "save":
                setAdditionalDetails();
        }
    }

    // unused method for observer pattern
    @Override
    public void update(String actionCommand) {

    }
}
