package ui;

import exceptions.DateFormatException;
import exceptions.InvalidSourceException;
import model.*;
import ui.audio.SoundObject;
import ui.swingtools.*;
import ui.texttools.PersistenceTool;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

// represents an editor frame where users can modify a schedule
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
    private SoundObject soundObject;

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

    // EFFECTS: creates a new frame with a schedule
    public MainFrame(Schedule schedule, String filePath) {
        super("eManager");
        this.schedule = schedule;
        st = new SwingTool(this);
        subject = new Subject();
        saveDestination = filePath;
        editingDate = false;
        editingName = false;
        editingLocation = false;
        initializeSoundObject();

        addMouseListener(this);

        topPanel = new JPanel();

        initializeFrame();
        initializeTopPanel();
        initializeEditorPanel();

        add(topPanel, BorderLayout.NORTH);
        add(editorPanel, BorderLayout.SOUTH);

        st.displayFrame(this);
    }

    // MODIFIES: this
    // EFFECTS: sets size, colour and default close operation for this JFrame
    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        setBackground(UIData.GREY_BACKGROUND);
        getContentPane().setBackground(UIData.GREY_BACKGROUND);
    }

    // MODIFIES: this
    // EFFECTS: generates the initial state of the top display panel of MainFrame
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

    // MODIFIES: this
    // EFFECTS: instantiates the buttons which are located in the top right part of MainFrame
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

    // MODIFIES: this
    // EFFECTS: instantiates sound object and plays 'openMainEditor' audio clip
    private void initializeSoundObject() {
        try {
            soundObject = new SoundObject("openMainEditor.aif");
            soundObject.play();
        } catch (IOException e) {
            System.out.println("IO exception has occurred");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Caught UnsupportedAudioFileException");
        } catch (LineUnavailableException e) {
            System.out.println("Caught LineUnavailableException");
        }
    }

    // MODIFIES: this
    // EFFECTS: updates event list display in top center panel of MainFrame
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

    // MODIFIES: this
    // EFFECTS: instantiates panel where calendar will eventually go
    //          part of functionality outside of that specified in user stories
    private void initializeCalendar() {
        calendar = new JPanel();
        calendar.setBackground(UIData.MENU_BACKGROUND);
        calendar.setPreferredSize(new Dimension(CALENDAR_WIDTH - PADDING, TOP_DISPLAY_HEIGHT - PADDING));
    }

    // MODIFIES: this
    // EFFECTS: instantiates empty editor panel below event list and header
    private void initializeEditorPanel() {
        editorPanel = new JPanel();
        editorPanel.setBackground(UIData.MENU_BACKGROUND);
        editorPanel.setPreferredSize(new Dimension(WIDTH, EDITOR_HEIGHT - PADDING));
        editorPanel.setMaximumSize(new Dimension(WIDTH, EDITOR_HEIGHT - PADDING));
        editorPanel.setLayout(new BorderLayout());
    }

    // MODIFIES: this
    // EFFECTS: fills editor panel with tools to edit show event
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

    // MODIFIES: this
    // EFFECTS: fills editor panel with tools to edit simple event
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

    // EFFECTS: instantiates and returns a panel into which event tools are added
    private JPanel generateToolRow() {
        JPanel topToolRow = new JPanel();
        topToolRow.setBackground(UIData.MENU_BACKGROUND);
        topToolRow.setPreferredSize(new Dimension(WIDTH - (PADDING * 2), TOOL_WINDOW_HEIGHT / 3));
        topToolRow.setLayout(new BorderLayout());
        return topToolRow;
    }

    // EFFECTS: processes button action commands and calls appropriate methods
    //          plays audio clip when user saves or creates a new event
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "load":
                break;
            case "save":
                soundObject.play("saveSound.aif");
                saveSchedule();
                break;
            case "edit":
                st.resetPanel(editorPanel);
                startEditor();
                break;
            case "addNew":
                soundObject.play("createNewEvent.aif");
                createNewEvent();
                break;
            case "return":
                st.closeFrame(errorFrame);
                break;
            case "quit":
                st.closeFrame(this);
        }
    }

    // EFFECTS: instantiates event creator, opening frame for event creation
    private void createNewEvent() {
        new EventCreator(this);
    }

    // MODIFIES: this
    // EFFECTS: adds newly created event to schedule and resets top panel to display it in list
    public void addNewEventAndInitializeEditor(ScheduleEvent event) {
        if (!Objects.isNull(event)) {
            schedule.addEvent(event);
            eventToEdit = event;

            st.resetPanel(topPanel);
            initializeTopPanel();

            selectAndRunEditor();
        }
    }

    // EFFECTS: processes mouse clicks--if user double clicks on the date, event name or location
    //          in the header panel, opens text field to edit each, respectively.  If user clicks outside
    //          of header, then text fields are closed
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

    // MODIFIES: this
    // EFFECTS: closes any currently active field in header and sets editing bools to false,
    //          then resets header to update display
    private void stopEditingHeader() {
        st.resetPanel(header);
        editingDate = false;
        editingName = false;
        editingLocation = false;
        updateHeader(date, eventName, eventLocation);
        pack();
    }

    // EFFECTS: returns true if mouse click occurs outside of date, name and location labels in header
    private boolean isClickOutsideOfField(MouseEvent e) {
        return (e.getY() > 205 || e.getY() < 165) || (e.getX() > 85 && e.getX() < 300)
                || (e.getX() < 660 && e.getX() > 450);
    }

    // MODIFIES: this
    // EFFECTS: deals with key events
    //          calls appropriate methods if user presses enter while editing date, name or location
    //          displays error message if date format isn't acceptable
    @Override
    public void keyPressed(KeyEvent e) {
        if (editingDate && e.getKeyCode() == 10) {
            try {
                updateDate();
            } catch (NumberFormatException nfe) {
                displayErrorMessage("please enter date as DD/MM/YYYY");
            } catch (DateFormatException dfe) {
                displayErrorMessage("please enter valid values for date");
            }
        } else if ((editingName || editingLocation) && e.getKeyCode() == 10) {
            updateNameAndLocation();
        }
    }

    // MODIFIES: this
    // EFFECTS: updates date in header with new date entered in text field and sets editingDate to false
    //          if date string isn't in acceptable format, throws NumberFormatException
    private void updateDate() throws NumberFormatException, DateFormatException {
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

    // MODIFIES: this
    // EFFECTS: updates name of eventToEdit and updates event name in header if editingName is true
    //          updates location of eventToEdit and updates location in header if editingLocation is true
    //          sets editingName and editingLocation to false
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

    // MODIFIES: this
    // EFFECTS: opens text field in header so user can enter new event date
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

    // MODIFIES: this
    // EFFECTS: opens text field in header so user can enter new event name
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

    // MODIFIES: this
    // EFFECTS: opens text field in header so user can enter new event location
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

    // MODIFIES: this
    // EFFECTS: iterates through schedule to find currently selected event in event list panel
    //          runs editor if event is found, displays an error message otherwise
    private void startEditor() {
        try {
            String eventName = getEventNameFromJList();
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

    // MODIFIES: this
    // EFFECTS: casts eventToEdit to either simple event or show then calls appropriate editor
    private void selectAndRunEditor() {
        st.resetPanel(editorPanel);

        if (eventToEdit instanceof Show) {
            Show show = (Show) eventToEdit;
            runShowEditor(show);
        } else if (eventToEdit instanceof SimpleEvent) {
            SimpleEvent event = (SimpleEvent) eventToEdit;
            runSimpleEventEditor(event);
        }
    }

    // EFFECTS: calls methods necessary to display and start editing an event
    private void runShowEditor(Show show) {
        getEventDetailsForHeader(show);
        initializeTools(show);
        pack();
    }

    // MODIFIES: this
    // EFFECTS: gets and sets date, name and location labels from event to be displayed in editor
    private void getEventDetailsForHeader(ScheduleEvent event) {
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

    // MODIFIES: this
    // EFFECTS: updates header with date, name and location of event currently being edited
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

        st.resetPanel(topPanel);
        initializeTopPanel();

        header.add(box);
    }

    // EFFECTS: calls methods to start simple event editor
    private void runSimpleEventEditor(SimpleEvent event) {
        getEventDetailsForHeader(event);
        initializeTools(event);
        pack();
    }

    // EFFECTS: returns event name currently selected item in JList, throws InvalidSourceException if nothing selected
    private String getEventNameFromJList() throws InvalidSourceException {
        if (Objects.isNull(eventList.getSelectedValue())) {
            throw new InvalidSourceException();
        }
        String eventDisplayString = eventList.getSelectedValue().toString();
        return eventDisplayString.substring(19);
    }

    // EFFECTS: saves schedule to json format--displays error frame if file path not found
    private void saveSchedule() {
        PersistenceTool pt = new PersistenceTool();
        try {
            pt.saveSchedule(schedule, saveDestination);
        } catch (FileNotFoundException e) {
            displayErrorMessage("invalid file path");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays a generic error message with a return button
    public void displayErrorMessage() {
        JButton btn = st.createBtn("return", "return");
        String text = "please make a valid selection";
        errorFrame = st.displayErrorMessage("Error", text, btn, 300, 90);
        st.displayFrame(errorFrame);
    }

    // MODIFIES: this
    // EFFECTS: displays a customizable error message with a return button
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
