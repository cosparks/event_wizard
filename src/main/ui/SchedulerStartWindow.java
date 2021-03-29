package ui;

import exceptions.SameNameException;
import model.Schedule;
import ui.audio.SoundObject;
import ui.swingtools.SwingTool;
import ui.texttools.PersistenceTool;
import ui.swingtools.UIData;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SchedulerStartWindow extends JFrame implements ActionListener {
    public static final int FRAME_WIDTH = 550;
    public static final int FRAME_HEIGHT = 250;
    public static final int MENU_WIDTH = 375;
    public static final int MENU_HEIGHT = 150;
    public static final int PADDING = 25;

    private JFrame errorFrame;
    private JPanel menuPanel;
    private JTextField field;
    private JList fileList;
    private SoundObject soundObject;

    private Schedule schedule;
    private SwingTool st;

    public SchedulerStartWindow() {
        super("Start");
        st = new SwingTool(this);
        initializeSoundObject();

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        setBackground(UIData.GREY_BACKGROUND);
        getContentPane().setBackground(UIData.GREY_BACKGROUND);

        field = new JTextField(8);
        JLabel label = st.createLabel("please select:");
        add(label);

        initializeStartButtons();
        initializeMenuPanel();

        st.displayFrame(this);
    }

    private void initializeSoundObject() {
        try {
            soundObject = new SoundObject();
        } catch (IOException e) {
            System.out.println("IO exception has occurred");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Caught UnsupportedAudioFileException");
        } catch (LineUnavailableException e) {
            System.out.println("Caught LineUnavailableException");
        }
    }

    private void initializeStartButtons() {
        JButton createNewBtn = st.createBtn("create new", "createNewButton");
        JButton loadBtn = st.createBtn("load", "loadButton");
        JButton quitBtn = st.createBtn("quit", "quitButton");

        add(createNewBtn);
        add(loadBtn);
        add(quitBtn);
    }

    //This is the method that is called when the the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "loadButton":
                soundObject.play();
                st.resetPanel(menuPanel);
                displayLoadOptions();
                break;
            case "quitButton":
                st.closeFrame(this);
                break;
            case "returnButton":
                st.closeFrame(errorFrame);
                break;
            case "createNewButton":
                st.resetPanel(menuPanel);
                displayCreateNewOptions();
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
                String filePath = generateFilePath(scheduleName);
                st.closeFrame(this);
                new MainFrame(schedule, filePath);
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

    private String generateFilePath(String name) {
        return PersistenceTool.JSON_STORE + name + ".json";
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

    private void initializeMenuPanel() {
        menuPanel = initializePanel();
        add(menuPanel, BorderLayout.SOUTH);
    }

    private JPanel initializePanelForMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.setBackground(UIData.BUTTON_GREY);
        panel.setBackground(UIData.MENU_BACKGROUND);
        return panel;
    }

    private void displayCreateNewOptions() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(4, 1));
        fieldPanel.add(field);
        fieldPanel.setBackground(UIData.MENU_BACKGROUND);

        JLabel label = st.createLabel("enter name: ");
        JButton createBtn = st.createBtn("create", "create");

        fillMenuPanel(label, fieldPanel, createBtn);

        pack();
    }

    private void displayLoadOptions() {
        JLabel label = st.createLabel("select existing");
        JButton createBtn =  st.createBtn("open", "open");
        String[] fileNames = getFileNames();
        initializeFileList(fileNames);

        fillMenuPanel(label, fileList, createBtn);

        pack();
    }

    private void fillMenuPanel(JComponent leftComponent, JComponent center, JComponent rightComponent) {
        JPanel grid = new JPanel();

        JPanel rightPanel = initializePanelForMenu();
        JPanel leftPanel = initializePanelForMenu();

        grid.setLayout(new GridLayout());
        grid.setPreferredSize(new Dimension(MENU_WIDTH - PADDING, MENU_HEIGHT - PADDING));
        grid.setBackground(UIData.MENU_BACKGROUND);

        leftPanel.add(leftComponent);
        rightPanel.add(rightComponent);

        grid.add(leftPanel);
        grid.add(center);
        grid.add(rightPanel);

        menuPanel.add(grid);
    }

    private JFrame initializeFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setBackground(UIData.GREY_BACKGROUND);
        frame.getContentPane().setBackground(UIData.GREY_BACKGROUND);
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        frame.setLayout(new FlowLayout());
        return frame;
    }

    private JPanel initializePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIData.MENU_BACKGROUND);
        panel.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        panel.setLayout(new FlowLayout());
        return panel;
    }

    private void initializeFileList(String[] fileNames) {
        fileList = new JList(fileNames);
        fileList.setPreferredSize(new Dimension(130, 80));
        fileList.setBackground(UIData.BUTTON_GREY);
        fileList.setForeground(UIData.GREY_TEXT);
        fileList.setBorder(new EtchedBorder());
    }

    private void openLoadedSchedule() {
        try {
            String source = getSelectedSource();
            loadSchedule(source);
            new MainFrame(schedule, source);
            st.closeFrame(this);
        } catch (FileNotFoundException e) {
            displayError("Error", "Please make valid selection", 250, 110);
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

    private void displayError(String title, String text, int width, int height) {
        errorFrame = initializeFrame(title);
        errorFrame.setPreferredSize(new Dimension(width, height));

        JLabel label = st.createLabel(text);
        JButton returnBtn = st.createBtn("return", "returnButton");

        errorFrame.add(label);
        errorFrame.add(returnBtn);

        st.displayFrame(errorFrame);
    }

    public static void main(String[] args) {
        new SchedulerStartWindow();
    }
}