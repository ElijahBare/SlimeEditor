package org.ebs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

public class SlimeEditor extends JFrame {

    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem upFontSize;
    private JMenuItem lowerFontSize;
    private JMenuItem pasteMenuItem;
    private JMenuItem aboutMenuItem;

    private String fileName = "";
    private ArrayList<String> undoState = new ArrayList<String>();

    int currentState = 0;

    public SlimeEditor() {
        // Set the title and default close operation
        setTitle("Slime Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the text area and add it to the center of the frame
        textArea = new JTextArea();
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.BLUE.darker().darker());
        textArea.setSelectionColor(Color.CYAN);

        add(new JScrollPane(textArea), BorderLayout.CENTER);


        // Create the menu bar
        menuBar = new JMenuBar();

        // Create the File menu and add it to the menu bar
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Create the Edit menu and add it to the menu bar
        editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        // Create the Help menu and add it to the menu bar
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        undoMenuItem = new JMenuItem("Undo");
        redoMenuItem = new JMenuItem("Redo");

        upFontSize = new JMenuItem("Increase Font Size");
        lowerFontSize = new JMenuItem("Decrease Font Size");


        // Create the File menu items and add them to the File menu
        newMenuItem = new JMenuItem("New");
        fileMenu.add(newMenuItem);
        openMenuItem = new JMenuItem("Open");
        fileMenu.add(openMenuItem);
        saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);

        // Create the Edit menu items and add them to the Edit menu
        cutMenuItem = new JMenuItem("Cut");
        editMenu.add(cutMenuItem);
        copyMenuItem = new JMenuItem("Copy");
        editMenu.add(copyMenuItem);
        pasteMenuItem = new JMenuItem("Paste");
        editMenu.add(pasteMenuItem);
        pasteMenuItem = new JMenuItem("Undo");

        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);

        editMenu.add(upFontSize);
        editMenu.add(lowerFontSize);

        // Create the Help menu item and add it to the Help menu
        aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);


        // Set the menu bar for the frame
        setJMenuBar(menuBar);

        // Set the size of the frame and make it visible
        setSize(600, 400);
        setVisible(true);

        // Add action listeners for the menu items
        newMenuItem.addActionListener(new NewMenuItemListener());
        openMenuItem.addActionListener(new OpenMenuItemListener());
        saveMenuItem.addActionListener(new SaveMenuItemListener());
        exitMenuItem.addActionListener(new ExitMenuItemListener());
        cutMenuItem.addActionListener(new CutMenuItemListener());
        copyMenuItem.addActionListener(new CopyMenuItemListener());
        pasteMenuItem.addActionListener(new PasteMenuItemListener());
        undoMenuItem.addActionListener(new UndoItemListener());
        redoMenuItem.addActionListener(new RedoItemListener());
        aboutMenuItem.addActionListener(new AboutMenuItemListener());
        upFontSize.addActionListener(new FontSizeChangeListenerPlus());
        lowerFontSize.addActionListener(new FontSizeChangeListenerMinus());

        // Add key listeners for the menu items
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));

        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));

        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));


        upFontSize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_MASK));
        lowerFontSize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK));


        //Undo checker thread
        Thread thread = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted()) {
                if (!undoState.toString().contains(textArea.getText())) {
                    undoState.add(textArea.getText()); //set it to the current state
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "Undo-Checker-Thread");

        thread.start();


    }

    // Inner class for the New menu item
    class NewMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Clear the text area
            textArea.setText("");
            fileName = "";
        }
    }

    // Inner class for the Open menu item
    class OpenMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {

                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    fileName = selectedFile.getAbsolutePath();

                    FileReader fileReader = new FileReader(fileName);
                    int i;
                    while ((i = fileReader.read()) != -1) {
                        textArea.append(String.valueOf((char) i));

                    }
                }


            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    // Inner class for the Save menu item
    class SaveMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String text = textArea.getText();

            if (!undoState.toString().contains(text) && fileName == null && fileName == "") {
                undoState.add(text); //set it to the current state
                try {
                    JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());

                    int returnValue = fileChooser.showOpenDialog(null);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();

                        fileName = selectedFile.getAbsolutePath();

                        // Write the contents of the text area to the file
                        FileWriter writer = new FileWriter(fileName);
                        writer.write(text);
                        writer.close();


                        System.out.println(fileName);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }


    // Inner class for the Exit menu item
    static class ExitMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    // inner class for the Cut menu item
    class CutMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.cut();
        }
    }

    // Inner class for the Copy menu item
    class CopyMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.copy();
        }
    }

    // Inner class for the Paste menu item
    class PasteMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.paste();
        }
    }

    // Inner class for the Undo functionality
    class UndoItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.setText(undoState.get(currentState));
            currentState += 1;

            if (undoState.size() - 1 < currentState) {
                currentState = undoState.size() - 1;
            }
        }
    }

    // Inner class for the redo functionality
    class RedoItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.setText(undoState.get(currentState));
            currentState -= 1;
            if (currentState < 0) {
                currentState = 0;
            }
        }
    }

    class FontSizeChangeListenerPlus implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.setFont(new Font(textArea.getFont().getFontName(), Font.PLAIN, textArea.getFont().getSize() + 1));
        }
    }

    class FontSizeChangeListenerMinus implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.setFont(new Font(textArea.getFont().getFontName(), Font.PLAIN, textArea.getFont().getSize() - 1));
        }
    }


    // Inner class for the About menu item
    class AboutMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(SlimeEditor.this,
                    "Made for fun by EBS");
        }
    }

    public static void main(String[] args) {
        new SlimeEditor();
    }
}
