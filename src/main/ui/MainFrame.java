package ui;

import exceptions.InvalidSourceException;
import model.*;
import ui.swingtools.*;
import ui.texttools.PersistenceTool;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

public class MainFrame extends JFrame implements ActionListener, MouseListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 650;
    public static final int PADDING = 5;
    public static final int TOP_DISPLAY_HEIGHT = 135;
    public static final int TOP_BUTTON_WIDTH = 95;
    public static final int BUTTON_HEIGHT = 25;
    public static final int CALENDAR_WIDTH = 145;
    public static final int EVENT_LIST_WIDTH = WIDTH - TOP_BUTTON_WIDTH - CALENDAR_WIDTH;
    public static final int EDITOR_HEIGHT = HEIGHT - TOP_DISPLAY_HEIGHT - 20;
    public static final int HEADER_HEIGHT = 40;
    public static final int TOOL_WINDOW_HEIGHT = EDITOR_HEIGHT - HEADER_HEIGHT;
    public static final int TOOL_WIDTH = WIDTH / 2 - (PADDING * 2);
    public static final int TOOL_HEIGHT = TOOL_WINDOW_HEIGHT / 4 - (PADDING * 2);

    private String saveDestination;
    private boolean editingDate;
    private boolean editingName;
    private boolean editingLocation;
    private ScheduleEvent eventToEdit;

    private Schedule schedule;
    private ActTool actTool;
    private EmployeeTool employeeTool;
    private BarTool barTool;
    private ShowDetailTool showDetailTool;
    private EventDetailTool eventDetailTool;
    private SwingTool st;
    private Subject subject;

    private JFrame errorFrame;
    private JPanel topPanel;
    private JPanel topButtons;
    private JPanel calendar;
    private JPanel editorPanel;
    private JPanel header;
    private JList eventList;
    private JLabel date;
    private JLabel eventName;
    private JLabel eventLocation;
    private JTextField headerTextField;

    public MainFrame(Schedule schedule, String filePath) {
        super("eManager");
        this.schedule = schedule;
        st = new SwingTool(this);
        subject = new Subject();
        saveDestination = filePath;
        editingDate = false;
        editingName = false;
        editingLocation = false;

        addMouseListener(this);

        topPanel = new JPanel();

        initializeFrame();
        initializeTopPanel();
        initializeEditorPanel();

        add(topPanel, BorderLayout.NORTH);
        add(editorPanel, BorderLayout.SOUTH);

        st.displayFrame(this);
    }

    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        setBackground(UIData.GREY_BACKGROUND);
        getContentPane().setBackground(UIData.GREY_BACKGROUND);
    }

    private void initializeTopPanel() {
        topPanel.setBackground(UIData.MENU_BACKGROUND);
        topPanel.setPreferredSize(new Dimension(WIDTH, TOP_DISPLAY_HEIGHT));
        topPanel.setLayout(new FlowLayout());

        updateEventList();
        initializeCalendar();
        initializeTopButtons();

        topPanel.add(calendar);
        topPanel.add(eventList);
        topPanel.add(topButtons);
    }

    private void initializeTopButtons() {
        topButtons = new JPanel();
        topButtons.setPreferredSize(new Dimension(TOP_BUTTON_WIDTH - PADDING, TOP_DISPLAY_HEIGHT - PADDING));
        topButtons.setLayout(new BoxLayout(topButtons, BoxLayout.Y_AXIS));
        topButtons.setBackground(UIData.MENU_BACKGROUND);

        JButton editButton = st.createBoxButton("edit", "edit");
        JButton addNewButton = st.createBoxButton("add new", "addNew");
        JButton saveButton = st.createBoxButton("save", "save");
        JButton quitButton = st.createBoxButton("quit", "quit");

        topButtons.add(editButton);
        topButtons.add(addNewButton);
        topButtons.add(saveButton);
        topButtons.add(quitButton);
    }

    private void updateEventList() {
        ArrayList<String> events = new ArrayList<>();

        for (int i = 0; i < schedule.getSize(); i++) {
            ScheduleEvent e = schedule.getEvent(i);
            String dateNameString = e.getStartDate().getDateAsString() + " - "
                    + e.getName();
            events.add(dateNameString);
        }

        String[] eventsArray = new String[events.size()];
        events.toArray(eventsArray);
        eventList = new JList(eventsArray);
        eventList.setPreferredSize(new Dimension(EVENT_LIST_WIDTH - PADDING, TOP_DISPLAY_HEIGHT - PADDING));
        eventList.setMinimumSize(new Dimension(EVENT_LIST_WIDTH - PADDING, TOP_DISPLAY_HEIGHT - PADDING));
        eventList.setBackground(UIData.MENU_BACKGROUND);
        eventList.setBorder(new EtchedBorder());
        eventList.setForeground(UIData.GREY_TEXT);
    }

    private void initializeCalendar() {
        calendar = new JPanel();
        calendar.setBackground(UIData.MENU_BACKGROUND);
        calendar.setPreferredSize(new Dimension(CALENDAR_WIDTH - PADDING, TOP_DISPLAY_HEIGHT - PADDING));
    }

    private void initializeEditorPanel() {
        editorPanel = new JPanel();
        editorPanel.setBackground(UIData.MENU_BACKGROUND);
        editorPanel.setPreferredSize(new Dimension(WIDTH, EDITOR_HEIGHT - PADDING));
        editorPanel.setMaximumSize(new Dimension(WIDTH, EDITOR_HEIGHT - PADDING));
        editorPanel.setLayout(new BorderLayout());
    }

    private void initializeTools(Show show) {
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new FlowLayout());
        toolPanel.setBackground(UIData.DARK_GREY);
        toolPanel.setPreferredSize(new Dimension(WIDTH - PADDING, TOOL_WINDOW_HEIGHT - PADDING));

        JPanel topToolRow = generateToolRow();
        JPanel middleToolRow = generateToolRow();

        actTool = new ActTool(this, show.getActs());
        topToolRow.add(actTool, BorderLayout.WEST);
        subject.addObserver(actTool);

        employeeTool = new EmployeeTool(this, show.getEmployees());
        topToolRow.add(employeeTool, BorderLayout.EAST);
        subject.addObserver(employeeTool);

        barTool = new BarTool(this, show.getBar());
        middleToolRow.add(barTool, BorderLayout.WEST);
        subject.addObserver(barTool);

        showDetailTool = new ShowDetailTool(this, show);
        middleToolRow.add(showDetailTool, BorderLayout.EAST);
        subject.addObserver(showDetailTool);

        toolPanel.add(topToolRow);
        toolPanel.add(middleToolRow);
        editorPanel.add(toolPanel, BorderLayout.SOUTH);
    }

    private void initializeTools(SimpleEvent event) {
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new FlowLayout());
        toolPanel.setBackground(UIData.DARK_GREY);
        toolPanel.setPreferredSize(new Dimension(WIDTH - PADDING, TOOL_WINDOW_HEIGHT - PADDING));

        JPanel topToolRow = generateToolRow();
        JPanel middleToolRow = generateToolRow();

        eventDetailTool = new EventDetailTool(this, event.getDetails());
        topToolRow.add(eventDetailTool);

        toolPanel.add(topToolRow);
        toolPanel.add(middleToolRow);
        editorPanel.add(toolPanel, BorderLayout.SOUTH);
    }

    private JPanel generateToolRow() {
        JPanel topToolRow = new JPanel();
        topToolRow.setBackground(UIData.MENU_BACKGROUND);
        topToolRow.setPreferredSize(new Dimension(WIDTH - (PADDING * 2), TOOL_WINDOW_HEIGHT / 3));
        topToolRow.setLayout(new BorderLayout());
        return topToolRow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "load":
                break;
            case "save":
                saveSchedule();
                break;
            case "edit":
                st.resetPanel(editorPanel);
                startEditor();
                break;
            case "addNew":
                createNewEvent();
                break;
            case "return":
                st.closeFrame(errorFrame);
                break;
            case "quit":
                st.closeFrame(this);
        }
    }

    private void createNewEvent() {
        new EventCreator(this);
    }

    public void addNewEventAndInitializeEditor(ScheduleEvent event) {
        if (!Objects.isNull(event)) {
            schedule.addEvent(event);
            eventToEdit = event;

            st.resetPanel(topPanel);
            initializeTopPanel();

            selectAndRunEditor();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            if (0 < e.getX() && e.getX() < 85 && 165 < e.getY() && e.getY() < 205) {
                editDate();
            } else if (300 < e.getX() && e.getX() < 450 && 165 < e.getY() && e.getY() < 205) {
                editName();
            } else if (650 < e.getX() && e.getX() < 800 && 165 < e.getY() && e.getY() < 205) {
                editLocation();
            }
        } else if ((editingDate || editingName || editingLocation) && isClickOutsideOfField(e)) {
            stopEditingHeader();
        }
    }

    private void stopEditingHeader() {
        st.resetPanel(header);
        editingDate = false;
        editingName = false;
        editingLocation = false;
        updateHeader(date, eventName, eventLocation);
        pack();
    }

    private boolean isClickOutsideOfField(MouseEvent e) {
        return (e.getY() > 205 || e.getY() < 165) || (e.getX() > 85 && e.getX() < 300)
                || (e.getX() < 660 && e.getX() > 450);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (editingDate && e.getKeyCode() == 10) {
            try {
                updateDate();
            } catch (NumberFormatException nfe) {
                displayErrorMessage("please enter date as DD/MM/YYYY");
            }
        } else if ((editingName || editingLocation) && e.getKeyCode() == 10) {
            updateNameAndLocation();
        }
    }

    private void updateDate() throws NumberFormatException {
        String dateText = headerTextField.getText();
        int day = Integer.parseInt(dateText.substring(0, 2));
        int month = Integer.parseInt(dateText.substring(3, 5));
        int year = Integer.parseInt(dateText.substring(6, 10));
        int hour = eventToEdit.getStartDate().getHour();
        int minute = eventToEdit.getStartDate().getMinute();

        editingDate = false;
        EventDate newDate = new EventDate(day, month, year, hour, minute);
        eventToEdit.setStartDate(newDate);
        date.setText(eventToEdit.getStartDate().getDateForDisplay());
        st.resetPanel(header);
        updateHeader(date, eventName, eventLocation);
        pack();
    }

    private void updateNameAndLocation() {
        String newText = headerTextField.getText();
        if (editingName) {
            eventName.setText(newText);
            eventToEdit.setName(newText);
            editingName = false;
        } else if (editingLocation) {
            eventLocation.setText(newText);
            eventToEdit.setLocation(newText);
            editingLocation = false;
        }
        st.resetPanel(header);
        updateHeader(date, eventName, eventLocation);
        pack();
    }

    private void editDate() {
        editingLocation = false;
        editingName = false;

        st.resetPanel(header);
        editingDate = true;
        headerTextField = new JTextField(date.getText());
        headerTextField.setMaximumSize(new Dimension(85, 35));
        headerTextField.addKeyListener(this);

        updateHeader(headerTextField, eventName, eventLocation);
        pack();
    }

    private void editName() {
        editingLocation = false;
        editingDate = false;

        st.resetPanel(header);
        editingName = true;
        headerTextField = new JTextField(eventName.getText());
        headerTextField.setMaximumSize(new Dimension(150, 35));
        headerTextField.addKeyListener(this);

        updateHeader(date, headerTextField, eventLocation);
        pack();
    }

    private void editLocation() {
        editingDate = false;
        editingName = false;

        st.resetPanel(header);
        editingLocation = true;
        headerTextField = new JTextField(eventLocation.getText());
        headerTextField.setMaximumSize(new Dimension(150, 35));
        headerTextField.addKeyListener(this);

        updateHeader(date, eventName, headerTextField);
        pack();
    }

    private void startEditor() {
        try {
            String eventName = extractEventName();
            eventToEdit = null;

            for (int i = 0; i < schedule.getSize(); i++) {
                ScheduleEvent e = schedule.getEvent(i);
                if (eventName.equals(e.getName())) {
                    eventToEdit = e;
                }
            }
            selectAndRunEditor();
        } catch (InvalidSourceException ise) {
            displayErrorMessage();
        }
    }

    private void selectAndRunEditor() {
        if (eventToEdit instanceof Show) {
            Show show = (Show) eventToEdit;
            runShowEditor(show);
        } else if (eventToEdit instanceof SimpleEvent) {
            SimpleEvent event = (SimpleEvent) eventToEdit;
            runSimpleEventEditor(event);
        }
    }

    private void runShowEditor(Show show) {
        setHeader(show);
        initializeTools(show);
        pack();
    }

    private void setHeader(ScheduleEvent event) {
        header = new JPanel();

        date = st.createLabel(event.getStartDate().getDateForDisplay());
        eventName = st.createLabel(event.getName());
        eventLocation = st.createLabel(event.getLocation());
        date.setForeground(UIData.GREY_TEXT);
        eventName.setForeground(UIData.GREY_TEXT);
        eventLocation.setForeground(UIData.GREY_TEXT);

        updateHeader(date, eventName, eventLocation);
        editorPanel.add(header, BorderLayout.NORTH);
    }

    private void updateHeader(JComponent date, JComponent name, JComponent location) {
        header.setBackground(UIData.DARK_GREY);
        header.setMaximumSize(new Dimension(WIDTH - PADDING * 2, HEADER_HEIGHT));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBorder(new EtchedBorder());

        Box box = Box.createHorizontalBox();
        box.setBackground(UIData.DARK_GREY);
        box.setPreferredSize(new Dimension(WIDTH - PADDING * 2, HEADER_HEIGHT));

        box.add(date);
        box.add(Box.createGlue());
        box.add(name);
        box.add(Box.createGlue());
        box.add(location);

        header.add(box);
    }

    private void runSimpleEventEditor(SimpleEvent event) {
        setHeader(event);
        initializeTools(event);
        pack();
    }

    private String extractEventName() throws InvalidSourceException {
        if (Objects.isNull(eventList.getSelectedValue())) {
            throw new InvalidSourceException();
        }
        String eventDisplayString = eventList.getSelectedValue().toString();
        return eventDisplayString.substring(19);
    }

    private void saveSchedule() {
        PersistenceTool pt = new PersistenceTool();
        try {
            pt.saveSchedule(schedule, saveDestination);
        } catch (FileNotFoundException e) {
            // do something
        }
    }

    public void displayErrorMessage() {
        JButton btn = st.createBtn("return", "return");
        String text = "please make a valid selection";
        errorFrame = st.displayErrorMessage("Error", text, btn, 300, 90);
        st.displayFrame(errorFrame);
    }

    public void displayErrorMessage(String message) {
        JButton btn = st.createBtn("return", "return");
        errorFrame = st.displayErrorMessage("Error", message, btn, 300, 90);
        st.displayFrame(errorFrame);
    }


    // Unused methods for MouseListener and KeyListener implementation
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
