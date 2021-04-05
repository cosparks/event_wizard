package ui.swingtools;

import exceptions.InvalidSourceException;
import model.show.Employee;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

// Represents a tool for editing a list of employees
public class EmployeeTool extends EditingTool {
    private List<Employee> employees;
    private Employee employeeToEdit;

    private JPanel editorButtonPanel;
    private JTextField nameField;
    private JTextField payField;
    private JTextField jobField;

    // EFFECTS: constructs a new EmployeeTool with mainframe and list of employees
    public EmployeeTool(MainFrame mf, List<Employee> employees) {
        super(mf, "employees");
        setLayout(new BorderLayout(UIData.BORDER_HGAP, UIData.BORDER_VGAP));
        this.employees = employees;
        updateJList();

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
    // EFFECTS: displays fields and button to edit existing employee
    private void initializeEditDisplay() {
        employeeToEdit = getEmployeeToEdit();

        String name = employeeToEdit.getName();
        String job = employeeToEdit.getJob();
        String pay = Integer.toString(employeeToEdit.getPay());

        JButton btn = st.createBoxButton("save", "save");

        setEditPanel(name, pay, job, btn);
    }

    // EFFECTS: calls method to reset this and initialize creator display
    private void startAddNewPanel() {
        st.resetPanel(this);
        initializeCreatorDisplay();
    }

    // EFFECTS: initializes new button and calls method to fill editor panel
    private void initializeCreatorDisplay() {
        JButton createNewButton = st.createBtn("create", "create");
        createNewButton.setMaximumSize(new Dimension(BUTTON_WIDTH, MainFrame.BUTTON_HEIGHT));

        setEditPanel("name", "pay", "job", createNewButton);
    }

    // MODIFIES: this
    // EFFECTS: generates and displays employee editing panel with three fields and button
    private void setEditPanel(String fieldA, String fieldB, String fieldC, JButton button) {
        JPanel editPanel = initializeEditPanel();
        nameField = new JTextField(fieldA, 0);
        payField = new JTextField(fieldB, 0);
        jobField = new JTextField(fieldC, 0);

        JPanel grid = new JPanel();
        grid.setBackground(POPUP_MENU_BACKGROUND);
        grid.setLayout(new GridLayout(2, 1));
        grid.setPreferredSize(new Dimension(width - (BUTTON_WIDTH * 2), DISPLAY_HEIGHT / 2));

        JPanel firstGridPanel = new JPanel();
        firstGridPanel.setLayout(new GridLayout(1, 2));
        firstGridPanel.setBackground(POPUP_MENU_BACKGROUND);

        initializeEditorButtonPanel(button);

        firstGridPanel.add(nameField);
        firstGridPanel.add(payField);
        grid.add(firstGridPanel);
        grid.add(jobField);

        super.initializeDisplay();
        editPanel.add(grid, BorderLayout.WEST);
        editPanel.add(editorButtonPanel, BorderLayout.EAST);
        leftDisplay.add(editPanel, BorderLayout.SOUTH);
        mainFrame.pack();
    }

    // MODIFIES: this, button
    // EFFECTS: sets button graphics for button
    private void initializeEditorButtonPanel(JButton button) {
        editorButtonPanel = new JPanel();
        editorButtonPanel.setBackground(POPUP_MENU_BACKGROUND);
        editorButtonPanel.setLayout(new BoxLayout(editorButtonPanel, BoxLayout.Y_AXIS));
        editorButtonPanel.add(button);
    }

    // EFFECTS: returns JPanel to be used as the main editing panel
    private JPanel initializeEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setBackground(POPUP_MENU_BACKGROUND);
        editPanel.setPreferredSize(new Dimension(width * 3 / 4, DISPLAY_HEIGHT / 2));
        editPanel.setLayout(new BorderLayout());
        return editPanel;
    }

    // EFFECTS: returns employee currently selected in JList
    private Employee getEmployeeToEdit() {
        Employee employeeToEdit = null;
        for (Employee e : employees) {
            if (e.getName().equals(selectedString)) {
                employeeToEdit = e;
            }
        }
        return employeeToEdit;
    }

    // MODIFIES: this
    // EFFECTS: saves new values for employeeToEdit and calls reset tool panel; displays error if
    //          user has entered invalid input into any of the employee's fields
    private void saveEmployee() {
        try {
            employeeToEdit.setPay(Integer.parseInt(payField.getText()));
            employeeToEdit.setName(nameField.getText());
            employeeToEdit.setJob(jobField.getText());
            resetToolPanel();
        } catch (NumberFormatException e) {
            mainFrame.displayErrorMessage("please enter numerical digits for pay");
        }
    }

    // MODIFIES: this
    // EFFECTS: instantiates new employee with values specified in fields, then adds it to employees;
    //          displays error message if any field input is invalid
    private void createAndAddEmployee() {
        int pay;
        String name;
        String job;

        name = nameField.getText();
        job = jobField.getText();

        try {
            pay = Integer.parseInt(payField.getText());
            Employee employee = new Employee(name, pay);
            employee.setJob(job);
            employees.add(employee);
            resetToolPanel();
        } catch (NumberFormatException e) {
            mainFrame.displayErrorMessage("please enter valid number for pay");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes employee currently selected in JList from list of employees
    private void removeSelected() {
        try {
            setSelectedString();
            Employee employee = getEmployeeToEdit();
            employees.remove(employee);
            resetToolPanel();
        } catch (InvalidSourceException ise) {
            mainFrame.displayErrorMessage();
        }
    }

    // specified in superclass
    @Override
    protected void updateJList() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> pay = new ArrayList<>();
        String[] nameArray = new String[employees.size()];
        String[] payArray = new String[employees.size()];

        for (Employee e : employees) {
            names.add(e.getName());
            pay.add(Integer.toString(e.getPay()));
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
                startAddNewPanel();
                break;
            case "remove":
                removeSelected();
                break;
            case "save":
                saveEmployee();
                break;
            case "create":
                createAndAddEmployee();
        }
    }
}
