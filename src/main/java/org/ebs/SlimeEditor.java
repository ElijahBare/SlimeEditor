package org.ebs;

import org.ebs.scripting.ScriptingAPI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SlimeEditor extends JFrame {

    public static SlimeEditor INSTANCE;
    public JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem runMenuItem;

    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;

    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;

    private JMenuItem upFontSize;
    private JMenuItem lowerFontSize;


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
        runMenuItem = new JMenuItem("Run");
        fileMenu.add(runMenuItem);

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
        runMenuItem.addActionListener(new RunMenuItemListener());
        cutMenuItem.addActionListener(new CutMenuItemListener());
        copyMenuItem.addActionListener(new CopyMenuItemListener());
        pasteMenuItem.addActionListener(new PasteMenuItemListener());
        undoMenuItem.addActionListener(new UndoItemListener());
        redoMenuItem.addActionListener(new RedoItemListener());
        aboutMenuItem.addActionListener(new AboutMenuItemListener());
        upFontSize.addActionListener(new FontSizeChangeListenerPlus());
        lowerFontSize.addActionListener(new FontSizeChangeListenerMinus());

        // Add key listeners for the menu items (Windows/Linux)
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


        // Add key listeners for the menu items (Mac)
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.META_MASK));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_MASK));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_MASK));

        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_MASK));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.META_MASK));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.META_MASK));

        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.META_MASK));
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.META_MASK));


        upFontSize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.META_MASK));
        lowerFontSize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.META_MASK));


        //Undo checker thread
        Thread generalPurposeThread = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted()) {
                    HashMap<String, Color> wordsToColors = new HashMap<>();
                    wordsToColors.put("if", new Color(0xA9FF7600, true));
                    wordsToColors.put("else", new Color(0xA9FF7600, true));
                    wordsToColors.put("is", new Color(0xA9FF7600, true));
                    wordsToColors.put("in", new Color(0xA9FF7600, true));
                    wordsToColors.put("sendPacket", new Color(0x6C324BC9, true));
                    wordsToColors.put("world", new Color(0x8E6444FF, true));
                    wordsToColors.put("player", new Color(0x8E6444FF, true));
                    wordsToColors.put("\"", new Color(0xA863AF63, true));

                    highlightWords(wordsToColors);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
        }, "General-Thread");

        generalPurposeThread.start();



        //Undo checker thread
        Thread undoCheckThread = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted()) {
                if (!undoState.toString().contains(textArea.getText())) {
                    undoState.add(textArea.getText()); //set it to the current state
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "Undo-Checker-Thread");

        undoCheckThread.start();

        Thread autoSave = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    if (fileName != "" && fileName != null) {

                        // Write the contents of the text area to the file
                        FileWriter writer = new FileWriter(fileName);
                        writer.write(textArea.getText());
                        writer.close();

                        System.out.println("AutoSave Running...");
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                try {
                    Thread.sleep(90000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "Auto-Save-Thread");

        autoSave.start();


    }

    // Inner class for the New menu item
    class NewMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Clear the text area
            textArea.setText("");
            fileName = null;
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

                    textArea.setText("");

                    FileReader fileReader = new FileReader(fileName);
                    int i;
                    while ((i = fileReader.read()) != -1) {
                        textArea.append(String.valueOf((char) i));

                    }
                }


            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            //Append file to title
            if (fileName != "" && fileName != null) {
                setTitle("Slime Editor | Editing " + fileName);
            }

        }
    }

    // Inner class for the Save menu item
    class SaveMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String text = textArea.getText();

            if (fileName == null || fileName == "") {
                undoState.add(text); //set it to the current state

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File("saved_file_name.txt"));
                int returnValue = fileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    fileName = selectedFile.getAbsolutePath();


                }


            }

            try {
                // Write the contents of the text area to the file
                FileWriter writer = new FileWriter(fileName);
                writer.write(text);
                writer.close();


                System.out.println(fileName + " Saved...");
            } catch (IOException ex) {
                ex.printStackTrace();

            }

            //Append file to title
            if(fileName != "" && fileName !=null){
                setTitle("Slime Editor | Editing " + fileName);
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

    class RunMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FileReader scriptReader = null;

            try {
                scriptReader = new FileReader(fileName);
                // Create a map to hold the variables
                Map<String, Object> variables = new HashMap<>();

                // Run the script
                ScriptingAPI.runScript(scriptReader, variables);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    // Inner class for the About menu item
    class AboutMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(SlimeEditor.this,
                    "Made for fun by EBS");
        }
    }

    private void highlightWords(Map<String, Color> wordsToColors) {
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();
        String text = textArea.getText();
        for (Map.Entry<String, Color> entry : wordsToColors.entrySet()) {
            String word = entry.getKey();
            Color color = entry.getValue();
            int index = text.indexOf(word);
            while (index >= 0) {
                try {
                    highlighter.addHighlight(index, index + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
                    index = text.indexOf(word, index + word.length());
                } catch (BadLocationException e) {
                    // do nothing
                }
            }
        }
    }

    public static void main(String[] args) {
        INSTANCE = new SlimeEditor();
    }
}
