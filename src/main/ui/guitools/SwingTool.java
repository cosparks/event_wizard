package ui.guitools;

import ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

// class with useful methods for editing and displaying JFrame and JPanel classes
public class SwingTool {
    private ActionListener actionListener;

    // EFFECTS: constructs new SwingTool with actionListener
    public SwingTool(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    // MODIFIES: panel
    // EFFECTS: calls methods to clear and reset JPanel
    public void resetPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    // EFFECTS: creates and sets graphics for a new button with text and action command
    public JButton createBtn(String text, String actionCommand) {
        JButton btn = new JButton(text);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(actionListener);

        btn.setForeground(UIData.GREY_BACKGROUND);
        btn.setBackground(UIData.GREY_BACKGROUND);
        btn.setOpaque(true);

        return btn;
    }

    // EFFECTS: creates and sets graphics for a button intended to be displayed inside JPanel with boxLayout;
    //          sets label and actionCommand
    public JButton createBoxButton(String text, String actionCommand) {
        JButton btn = new JButton(text);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(actionListener);
        btn.setMaximumSize(new Dimension(MainFrame.TOP_BUTTON_WIDTH, MainFrame.BUTTON_HEIGHT));

        btn.setForeground(UIData.GREY_BACKGROUND);
        btn.setBackground(UIData.GREY_BACKGROUND);
        btn.setOpaque(true);

        return btn;
    }

    // EFFECTS: creates and sets graphics for jlabel with text
    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UIData.GREY_TEXT);
        return label;
    }

    // MODIFIES: frame
    // EFFECTS: calls methods necessary to display new JFrame
    public void displayFrame(JFrame frame) {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // EFFECTS: creates and displays JFrame of width and height with title, text for error message and button
    public JFrame displayErrorMessage(String title, String text, JButton button, int width, int height) {
        JFrame frame = initializeFrame(title, width, height);

        JLabel label = createLabel(text);

        frame.add(label);
        frame.add(button);

        return frame;
    }

    // EFFECTS: initializes, sets graphics for and returns new JFrame with title, width and height
    private JFrame initializeFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setBackground(UIData.GREY_BACKGROUND);
        frame.getContentPane().setBackground(UIData.GREY_BACKGROUND);
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        frame.setLayout(new FlowLayout());
        return frame;
    }

    // MODIFIES: frame
    // EFFECTS: calls methods to close frame; closes frame
    public void closeFrame(JFrame frame) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
