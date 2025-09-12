package org.gui;

import org.calculation.*;

import javax.swing.*;
import java.awt.*;

import static org.util.Parser.parseAndTransposeMatrix;

public class DataInputFrame extends JFrame {

    public DataInputFrame(){
        JFrame frame = new JFrame("ANOVA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.decode("#DFD4BD"));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel titleLabel = new JLabel("ANOVA Calculator with Contrast Technique", JLabel.CENTER);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#000000"));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        JLabel descriptionLabel = new JLabel("<html>A simple program that allows comparing the performance of multiple systems using ANOVA and contrast technique.<br>" +
                "To calculate ANOVA parameters and view contrasts, enter the data as follows:<br>" +
                "All measurements for one alternative should be in a single row, separated by commas.<br>" +
                "Measurements for the next alternative should be in the next row.<br>" +
                "NOTES: <br>" +
                "All alternatives must have the same number of measurements!<br>" +
                "Confidence level: 95% for ANOVA parameters and 90% for contrasts.</html>", JLabel.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabel.setForeground(Color.decode("#000000"));
        topPanel.add(descriptionLabel);
        topPanel.setPreferredSize(new Dimension(500, 200));

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        middlePanel.setBackground(Color.decode("#DFD4BD"));
        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.decode("#000000"));
        textArea.setForeground(Color.decode("#D6AB4F"));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setCaretColor(Color.decode("#FFFFFF"));
        JScrollPane scrollPane = new JScrollPane(textArea);
        middlePanel.add(scrollPane, BorderLayout.CENTER);
        middlePanel.setPreferredSize(new Dimension(500, 250));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.decode("#DFD4BD"));
        JButton button = new JButton("Calculate");
        button.setBackground(Color.decode("#D6AB4F"));
        button.setForeground(Color.decode("#000000"));
        button.setFont(new Font("Courier New", Font.BOLD, 16));
        button.setFocusable(false);
        bottomPanel.add(button);
        bottomPanel.setPreferredSize(new Dimension(500, 50));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        button.addActionListener(e -> {
            String inputText = textArea.getText();
            Main.matrix = parseAndTransposeMatrix(inputText);
            if (Main.matrix != null) {
                new ResultFrame();
            }
            else {
                JOptionPane.showMessageDialog(frame, "Invalid data input !!!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);

    }

}
