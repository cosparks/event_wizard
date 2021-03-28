package ui.swingtools;

import exceptions.InvalidSourceException;
import model.show.Employee;
import ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTool extends EditingTool {
    private List<Employee> employees;
    private Employee employeeToEdit;

    private JPanel editorButtonPanel;
    private JTextField nameField;
    private JTextField payField;
    private JTextField jobField;

    public EmployeeTool(MainFrame mf, List<Employee> employees) {
        super(mf, "employees");
        setLayout(new BorderLayout(UIData.BORDER_HGAP, UIData.BORDER_VGAP));
        this.employees = employees;
        updateJList();

        initializeDisplay();
    }

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

    private void initializeEditDisplay() {
        employeeToEdit = getEmployeeToEdit();

        String name = employeeToEdit.getName();
        String job = employeeToEdit.getJob();
        String pay = Integer.toString(employeeToEdit.getPay());

        JButton btn = st.createBoxButton("save", "save");

        setEditPanel(name, pay, job, btn);
    }

    private void startAddNewPanel() {
        st.resetPanel(this);
        initializeCreatorDisplay();
    }

    private void initializeCreatorDisplay() {
        JButton createNewButton = st.createBtn("create", "create");
        createNewButton.setMaximumSize(new Dimension(BUTTON_WIDTH, MainFrame.BUTTON_HEIGHT));

        setEditPanel("name", "pay", "job", createNewButton);
    }

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

    private void initializeEditorButtonPanel(JButton button) {
        editorButtonPanel = new JPanel();
        editorButtonPanel.setBackground(POPUP_MENU_BACKGROUND);
        editorButtonPanel.setLayout(new BoxLayout(editorButtonPanel, BoxLayout.Y_AXIS));
        editorButtonPanel.add(button);
    }

    private JPanel initializeEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setBackground(POPUP_MENU_BACKGROUND);
        editPanel.setPreferredSize(new Dimension(width * 3 / 4, DISPLAY_HEIGHT / 2));
        editPanel.setLayout(new BorderLayout());
        return editPanel;
    }

    private Employee getEmployeeToEdit() {
        Employee employeeToEdit = null;
        for (Employee e : employees) {
            if (e.getName().equals(selectedString)) {
                employeeToEdit = e;
            }
        }
        return employeeToEdit;
    }

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

    @Override
    public void update(String actionCommand) {
    }

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
