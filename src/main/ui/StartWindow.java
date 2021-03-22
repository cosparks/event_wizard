package ui;

import exceptions.SameNameException;
import model.Schedule;
import ui.tools.PersistenceTool;
import ui.tools.UIColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class StartWindow extends JFrame implements ActionListener {
    private static final int FRAME_WIDTH = 550;
    private static final int FRAME_HEIGHT = 550;

    private JFrame errorFrame;
    private JFrame newScheduleFrame;
    private JFrame loadScheduleFrame;
    private JPanel loadSchedulePanel;
    private JTextField field;
    private JList fileList;

    private Schedule schedule;

    public StartWindow() {
        super("Start");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        setBackground(UIColors.GREY_BACKGROUND);
        getContentPane().setBackground(UIColors.GREY_BACKGROUND);

        field = new JTextField(8);
        JLabel label = createLabel("please select:");
        add(label);

        initializeStartButtons();

        displayFrame(this);
    }

    private void initializeStartButtons() {
        JButton createNewBtn = createBtn("create new", "createNewButton");
        JButton loadBtn = createBtn("load", "loadButton");
        JButton quitBtn = createBtn("quit", "quitButton");

        add(createNewBtn);
        add(loadBtn);
        add(quitBtn);
    }

    //This is the method that is called when the the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "loadButton":
                startLoadScheduleWindow();
                break;
            case "quitButton":
                closeFrame(this);
                break;
            case "returnButton":
                closeFrame(errorFrame);
                break;
            case "createNewButton":
                startCreateNewScheduleWindow();
                break;
            case "create":
                initializeNewSchedule();
                break;
            case "open":
                openLoadedSchedule();
        }
    }

    private void initializeNewSchedule() {
        String scheduleName = field.getText();
        if (Objects.isNull(scheduleName) || scheduleName.equals("")) {
            displayError("Invalid Name", "Please enter valid name", 200, 90);
        } else {
            try {
                checkFileName(scheduleName);
                schedule = new Schedule(scheduleName);
                closeFrame(newScheduleFrame);
                closeFrame(this);
                new MainFrame(schedule); // TODO: start working on MainFrame display
            } catch (SameNameException e) {
                displayError("Name Already in Use", "Schedule named "
                        + scheduleName + " already exists. Please enter valid name", 450, 120);
            }
        }
    }

    private void checkFileName(String name) throws SameNameException {
        File[] array = getFiles();
        for (File f : array) {
            if (f.getName().equals(name + ".json")) {
                throw new SameNameException();
            }
        }
    }

    private File[] getFiles() {
        return new File(PersistenceTool.JSON_STORE).listFiles();
    }

    private String[] getFileNames() {
        File[] files = new File(PersistenceTool.JSON_STORE).listFiles();
        ArrayList<String> fileNames = new ArrayList<>();
        for (File f : files) {
            String name = f.getName();
            int index = name.lastIndexOf(".json");
            if (index > 0) {
                name = name.substring(0, index);
            }
            fileNames.add(name);
        }
        String[] stringArray = new String[fileNames.size()];

        return fileNames.toArray(stringArray);
    }

    private void startCreateNewScheduleWindow() {
        newScheduleFrame = initializeFrame("New Schedule");
        newScheduleFrame.setPreferredSize(new Dimension(325, 100));

        JLabel label = createLabel("Enter name: ");
        JButton createBtn = createBtn("create", "create");

        newScheduleFrame.add(label);
        newScheduleFrame.add(field);
        newScheduleFrame.add(createBtn);

        displayFrame(newScheduleFrame);
    }

    private void startLoadScheduleWindow() {
        loadScheduleFrame = initializeFrame("Load Schedule");

        // JUST MESSING AROUND
        // loadSchedulePanel = initializePanel();  // remove this later

        JLabel label = createLabel("select existing");
        JButton createBtn =  createBtn("open", "open");

        String[] fileNames = getFileNames();
        initializeFileList(fileNames);

        loadScheduleFrame.add(label);
        loadScheduleFrame.add(fileList);
        loadScheduleFrame.add(createBtn);

        displayFrame(loadScheduleFrame);

        // JUST MESSING AROUND
        // add(loadSchedulePanel);
        // displayFrame(this);
    }

    private JFrame initializeFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setBackground(UIColors.GREY_BACKGROUND);
        frame.getContentPane().setBackground(UIColors.GREY_BACKGROUND);
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        frame.setLayout(new FlowLayout());
        return frame;
    }

    private JPanel initializePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIColors.GREY_BACKGROUND);
        panel.setBackground(UIColors.GREY_BACKGROUND);
        panel.setBorder(new EmptyBorder(13, 13, 13, 13));
        panel.setLayout(new FlowLayout());
        return panel;
    }

    private JButton createBtn(String text, String actionCommand) {
        JButton btn = new JButton(text);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(this);

        btn.setForeground(UIColors.GREY_BACKGROUND);
        btn.setBackground(UIColors.GREY_BACKGROUND);
        btn.setOpaque(true);

        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UIColors.GREY_TEXT);
        return label;
    }

    private void displayFrame(JFrame frame) {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void initializeFileList(String[] fileNames) {
        fileList = new JList(fileNames);
        fileList.setPreferredSize(new Dimension(130, 80));
        fileList.setBackground(UIColors.BUTTON_GREY);
        fileList.setForeground(UIColors.GREY_TEXT);
        fileList.setBorder(new EtchedBorder());
    }

    private void openLoadedSchedule() {
        try {
            String source = getSelectedSource();
            loadSchedule(source);
            new MainFrame(schedule); // TODO: start working on MainFrame display
            closeFrame(loadScheduleFrame);
            closeFrame(this);
        } catch (FileNotFoundException e) {
            displayError("Error", "Please make valid selection", 300, 90);
        }
    }

    private String getSelectedSource() throws FileNotFoundException {
        if (Objects.isNull(fileList.getSelectedValue())) {
            throw new FileNotFoundException();
        }
        return PersistenceTool.JSON_STORE + fileList.getSelectedValue().toString() + ".json";
    }

    // MODIFIES: this
    // EFFECTS: retrieves and parses json object from source
    private void loadSchedule(String source) {
        PersistenceTool pt = new PersistenceTool();
        try {
            schedule = pt.loadSchedule(source);
        } catch (IOException e) {
            displayError("Error", "File not found", 300, 90);
        }
    }

    private void saveSchedule(String destination) {
        PersistenceTool pt = new PersistenceTool();
        try {
            pt.saveSchedule(schedule, destination);
        } catch (FileNotFoundException e) {
            displayError("Error", "File folder not found", 300, 90);
        }
    }

    private void displayError(String title, String text, int width, int height) {
        errorFrame = initializeFrame(title);
        errorFrame.setPreferredSize(new Dimension(width, height));

        JLabel label = createLabel(text);
        JButton returnBtn = createBtn("return", "returnButton");

        errorFrame.add(label);
        errorFrame.add(returnBtn);

        displayFrame(errorFrame);
    }

    private void closeFrame(JFrame frame) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        new StartWindow();
    }
}