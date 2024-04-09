package MVC;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TextEditorView implements ITextEditorView {
    private TextEditorController controller;
    private JFrame frame;
    private JTextArea textArea;

    public TextEditorView() {
        createView();
    }

    private void createView() {
        frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        textArea = new JTextArea();
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        openItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    List<String> textList = Files.lines(Paths.get(chooser.getSelectedFile().getPath()))
                                                .collect(Collectors.toList());
                    controller.loadTextList(textList);
                    displayTextList();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error opening file: " + ex.getMessage());
                }
            }
        });

        saveItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    Files.write(Paths.get(chooser.getSelectedFile().getPath()),
                                controller.getTextList().stream()
                                              .collect(Collectors.joining("\n"))
                                              .getBytes());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
                }
            }
        });

        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    public void setController(TextEditorController controller) {
        this.controller = controller;
    }

    public void displayTextList() {
        textArea.setText("");
        for (String text : controller.getTextList()) {
            textArea.append(text);
            textArea.append("\n");
        }
    }
}