package ui.swingtools;

import ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

// class with useful methods for editing and displaying JFrame and JPanel classes
public class SwingTool {
    private ActionListener actionListener;

    public SwingTool(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void resetPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public JButton createBtn(String text, String actionCommand) {
        JButton btn = new JButton(text);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(actionListener);

        btn.setForeground(UIData.GREY_BACKGROUND);
        btn.setBackground(UIData.GREY_BACKGROUND);
        btn.setOpaque(true);

        return btn;
    }

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

    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UIData.GREY_TEXT);
        return label;
    }

    public void displayFrame(JFrame frame) {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public JFrame displayErrorMessage(String title, String text,JButton button, int width, int height) {
        JFrame frame = initializeFrame(title, width, height);

        JLabel label = createLabel(text);

        frame.add(label);
        frame.add(button);

        return frame;
    }

    private JFrame initializeFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setBackground(UIData.GREY_BACKGROUND);
        frame.getContentPane().setBackground(UIData.GREY_BACKGROUND);
        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        frame.setLayout(new FlowLayout());
        return frame;
    }

    public void closeFrame(JFrame frame) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
