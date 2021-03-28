package ui;

import exceptions.InvalidSourceException;
import model.*;
import ui.swingtools.*;
import ui.texttools.PersistenceTool;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainFrame extends JFrame implements ActionListener {
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

    private Schedule schedule;
    private ActTool actTool;
    private EmployeeTool employeeTool;
    private BarTool barTool;
    private SwingTool st;
    private Subject subject;

    private JFrame errorFrame;
    private JPanel topPanel;
    private JPanel topButtons;
    private JPanel calendar;
    private JPanel editorPanel;
    private JList eventList;

    public MainFrame(Schedule schedule, String filePath) {
        super("eManager");
        this.schedule = schedule;
        st = new SwingTool(this);
        subject = new Subject();
        saveDestination = filePath;

        initializeFrame();
        initializeTopPanel();
        initializeEditorPanel();

        JLabel label = st.createLabel("Events");

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
        topPanel = new JPanel();
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

        toolPanel.add(topToolRow);
        toolPanel.add(middleToolRow);

        editorPanel.add(toolPanel, BorderLayout.SOUTH);
    }

    private void initializeTools(SimpleEvent event) {

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
                break;
            case "return":
                st.closeFrame(errorFrame);
                break;
            case "quit":
                st.closeFrame(this);
        }
    }

    private void startEditor() {
        try {
            String eventName = extractEventName();
            ScheduleEvent eventToEdit = null;

            for (int i = 0; i < schedule.getSize(); i++) {
                ScheduleEvent e = schedule.getEvent(i);
                if (eventName.equals(e.getName())) {
                    eventToEdit = e;
                }
            }
            if (eventToEdit instanceof Show) {
                Show show = (Show) eventToEdit;
                runShowEditor(show);
            } else if (eventToEdit instanceof SimpleEvent) {
                SimpleEvent event = (SimpleEvent) eventToEdit;
                runSimpleEventEditor(event);
            }
        } catch (InvalidSourceException ise) {
            displayErrorMessage();
        }

    }

    private void runShowEditor(Show show) {
        setHeader(show);
        initializeTools(show);

        pack();
    }

    private void setHeader(ScheduleEvent event) {
        JPanel header = new JPanel();
        header.setBackground(UIData.DARK_GREY);
        header.setMaximumSize(new Dimension(WIDTH - PADDING * 2, HEADER_HEIGHT));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBorder(new EtchedBorder());

        Box box = Box.createHorizontalBox();
        box.setBackground(UIData.DARK_GREY);
        box.setPreferredSize(new Dimension(WIDTH - PADDING * 2, HEADER_HEIGHT));

        JLabel date = st.createLabel(event.getStartDate().getDateForDisplay());
        JLabel eventName = st.createLabel(event.getName());
        JLabel eventLocation = st.createLabel(event.getLocation());
        date.setForeground(UIData.GREY_TEXT);
        eventName.setForeground(UIData.GREY_TEXT);
        eventLocation.setForeground(UIData.GREY_TEXT);

        box.add(date);
        box.add(Box.createGlue());
        box.add(eventName);
        box.add(Box.createGlue());
        box.add(eventLocation);

        header.add(box);

        editorPanel.add(header, BorderLayout.NORTH);
    }

    private void runSimpleEventEditor(SimpleEvent event) {
        setHeader(event);
        initializeTools(event);
    }

    private String extractEventName() throws InvalidSourceException {
        if (Objects.isNull(eventList.getSelectedValue())) {
            throw new InvalidSourceException();
        }
        String eventDisplayString = eventList.getSelectedValue().toString();
        return eventDisplayString.substring(19);
    }

    // MODIFIES: this
    // EFFECTS: retrieves and parses json object from source
    private void loadSchedule(String source) {
        PersistenceTool pt = new PersistenceTool();
        try {
            schedule = pt.loadSchedule(source);
        } catch (IOException e) {
            // do something;
        }
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
}
