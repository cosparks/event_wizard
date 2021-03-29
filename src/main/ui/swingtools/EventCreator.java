package ui.swingtools;

import exceptions.InvalidNameException;
import model.*;
import ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventCreator extends JFrame implements ActionListener {
    private static final int WIDTH = 350;
    private static final int HEIGHT = 200;

    private MainFrame mainFrame;
    private SwingTool st;
    private ScheduleEvent event;

    private JTextField nameField;
    private JTextField locationField;
    private JTextField dateField;
    private JTextField timeField;
    private JRadioButton radioA;
    private JRadioButton radioB;
    private JButton createBtn;

    public EventCreator(MainFrame mainFrame) {
        super("Create New");
        this.mainFrame = mainFrame;
        st = new SwingTool(this);

        initializeMainFrame();
        setFields();
        setButtons();
        initializeContents();
        st.displayFrame(this);
    }

    private void initializeMainFrame() {
        setLayout(new GridLayout(5, 1));
        setBackground(UIData.GREY_BACKGROUND);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        getContentPane().setBackground(UIData.GREY_BACKGROUND);
        setPreferredSize((new Dimension(WIDTH, HEIGHT)));
    }

    private void initializeContents() {
        JPanel namePanel = getBorderPanel();
        JPanel locationPanel = getBorderPanel();
        JPanel bottom = getBorderPanel();

        JPanel datePanel = createBox(dateField, timeField);
        JPanel radioPanel = new JPanel();
        radioPanel.setBackground(UIData.MENU_BACKGROUND);
        radioPanel.setLayout(new FlowLayout());

        namePanel.add(nameField, BorderLayout.CENTER);
        locationPanel.add(locationField, BorderLayout.CENTER);
        radioPanel.add(radioA);
        radioPanel.add(radioB);
        bottom.add(createBtn, BorderLayout.CENTER);

        add(namePanel);
        add(locationPanel);
        add(datePanel);
        add(radioPanel);
        add(bottom);
    }

    private JPanel getBorderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIData.MENU_BACKGROUND);
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JPanel createBox(JComponent partA, JComponent partB) {
        JPanel panel = new JPanel();
        panel.setBackground(UIData.MENU_BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        Box box = Box.createHorizontalBox();
        box.setBackground(UIData.DARK_GREY);
        box.setPreferredSize(new Dimension());

        box.add(partA);
        box.add(Box.createGlue());
        box.add(partB);

        panel.add(box);

        return panel;
    }

    private void setFields() {
        nameField = new JTextField("enter name", 15);
        locationField = new JTextField("location", 15);
        dateField = new JTextField("DD/MM/YYYY", 5);
        timeField = new JTextField("12:00", 5);
    }

    private void setButtons() {
        createBtn = st.createBoxButton("create", "create");

        radioA = new JRadioButton("show");
        radioA.addActionListener(this);
        radioA.setActionCommand("show");
        radioA.setForeground(UIData.GREY_TEXT);
        radioA.setSelected(true);

        radioB = new JRadioButton("simple");
        radioB.addActionListener(this);
        radioB.setActionCommand("simple");
        radioB.setForeground(UIData.GREY_TEXT);
    }

    private EventDate getEventDate() throws NumberFormatException {
        String dateText = dateField.getText();
        int day = Integer.parseInt(dateText.substring(0, 2));
        int month = Integer.parseInt(dateText.substring(3, 5));
        int year = Integer.parseInt(dateText.substring(6, 10));

        String timeText = timeField.getText();
        int hour = Integer.parseInt(timeText.substring(0, 2));
        int minute = Integer.parseInt(timeText.substring(3, 5));

        return new EventDate(day, month, year, hour, minute);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "simple":
                radioA.setSelected(false);
                break;
            case "show":
                radioB.setSelected(false);
                break;
            case "create":
                runCreateEvent();
        }
    }

    private void runCreateEvent() {
        try {
            createAndAddEvent();
        } catch (NumberFormatException nfe) {
            mainFrame.displayErrorMessage("date and time must be in format DD/MM/YYYY HH:MM");
        } catch (InvalidNameException ine) {
            mainFrame.displayErrorMessage("please enter event name");
        }
    }

    private void createAndAddEvent() throws NumberFormatException, InvalidNameException {
        String name = nameField.getText();
        String location = locationField.getText();

        EventDate date = getEventDate();
        if (name.equals("")) {
            throw new InvalidNameException();
        }

        if (radioA.isSelected()) {
            event = new Show(name);
        } else if (radioB.isSelected()) {
            event  = new SimpleEvent(name);
        }
        event.setStartDate(date);
        event.setLocation(location);

        mainFrame.addNewEventAndInitializeEditor(event);
        st.closeFrame(this);
    }
}
