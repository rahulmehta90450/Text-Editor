import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class GUI implements ActionListener {
    private static final Color MAIN_BG_COLOR = new Color(248, 248, 248);
    private static final Color BUTTON_BLUE = new Color(0, 120, 215);
    private static final Color BUTTON_HOVER = new Color(0, 140, 235);
    private static final Color BUTTON_PRESSED = new Color(0, 100, 195);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 18);

    public JTextPane textPane;
    public JScrollPane scrollPane;
    public JMenuBar menuBar;
    public JFrame window;
    public JMenu menuFile, menuEdit, menuFormat, menuColor, menuAbout;
    public JMenuItem iNew, iOpen, iSave, iSaveAs, iExit;
    public JMenuItem iCut, iCopy, iPaste, iSelectAll;
    public JCheckBoxMenuItem iWrap;
    public JMenuItem iColorText, iColorBackground;
    public JMenuItem iAbout;
    public JButton speakButton;
    public final Function_File fileHandler;
    public final TextToVoice textToSpeech;
    private Set<String> dictionary;
    private Timer spellCheckTimer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }

    public GUI() {
        createWindow();
        createTextArea();
        this.fileHandler = new Function_File(this);
        this.textToSpeech = new TextToVoice();
        initializeUI();
    }

    public void initializeUI() {
        createMenuBar();
        createFileMenu();
        createEditMenu();
        createFormatMenu();
        createColorMenu();
        createAboutMenu();
        window.setVisible(true);
    }

    public void createWindow() {
        window = new JFrame("Enhanced Text Editor");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/logo3.jpg"));
            window.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Could not load window icon");
        }
        window.setSize(900, 650);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setLocationRelativeTo(null);
    }

    public void createTextArea() {
        textPane = new JTextPane();
        textPane.setFont(TEXT_FONT);
        textPane.setMargin(new Insets(15, 15, 15, 15));
        textPane.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.add(scrollPane, BorderLayout.CENTER);

        loadDictionary();
        setupSpellChecker();
        createActionButtons();
    }

    private void loadDictionary() {
        dictionary = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(window, "words.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupSpellChecker() {
        spellCheckTimer = new Timer(300, e -> checkSpelling());
        spellCheckTimer.setRepeats(false);

        textPane.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                spellCheckTimer.restart();
            }

            public void removeUpdate(DocumentEvent e) {
                spellCheckTimer.restart();
            }

            public void changedUpdate(DocumentEvent e) {
                spellCheckTimer.restart();
            }
        });
    }

    private void checkSpelling() {
        try {
            StyledDocument doc = textPane.getStyledDocument();
            String text = doc.getText(0, doc.getLength());

            SimpleAttributeSet defaultAttr = new SimpleAttributeSet();
            StyleConstants.setForeground(defaultAttr, Color.BLACK);
            StyleConstants.setUnderline(defaultAttr, false);
            doc.setCharacterAttributes(0, text.length(), defaultAttr, true);

            int pos = 0;
            while (pos < text.length()) {
                while (pos < text.length() && !Character.isLetter(text.charAt(pos)))
                    pos++;
                int wordStart = pos;
                while (pos < text.length() && Character.isLetter(text.charAt(pos)))
                    pos++;
                if (wordStart < pos) {
                    String word = text.substring(wordStart, pos).toLowerCase();
                    if (!dictionary.contains(word)) {
                        SimpleAttributeSet misspelled = new SimpleAttributeSet();
                        StyleConstants.setForeground(misspelled, Color.RED);
                        StyleConstants.setUnderline(misspelled, true);
                        doc.setCharacterAttributes(wordStart, pos - wordStart, misspelled, false);
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void createActionButtons() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        speakButton = new JButton("Speak Text");
        styleButton(speakButton);
        speakButton.setPreferredSize(new Dimension(0, 40));
        speakButton.addActionListener(e -> handleTextToSpeech());

        buttonPanel.add(speakButton, BorderLayout.CENTER);
        window.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void handleTextToSpeech() {
        String text = textPane.getText().trim();
        if (text.isEmpty()) {
            showMessage("No text to speak!", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            textToSpeech.speak(text);
        }
    }

    public void styleButton(JButton button) {
        button.setFont(MAIN_FONT.deriveFont(Font.BOLD));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_BLUE);
        button.setFocusPainted(false);
        button.setMargin(new Insets(5, 10, 5, 10));

        Border innerBorder = BorderFactory.createEmptyBorder(8, 15, 8, 15);
        Border outerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 80, 175), 1),
                BorderFactory.createLineBorder(new Color(100, 170, 255, 100), 2));
        button.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_BLUE);
            }

            public void mousePressed(MouseEvent e) {
                button.setBackground(BUTTON_PRESSED);
            }

            public void mouseReleased(MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
            }
        });
    }

    public void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(MAIN_BG_COLOR);
        menuBar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                new EmptyBorder(5, 5, 5, 5)));

        window.setJMenuBar(menuBar);

        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuFormat = new JMenu("Format");
        menuColor = new JMenu("Color");
        menuAbout = new JMenu("About");

        menuFile.setFont(MENU_FONT);
        menuEdit.setFont(MENU_FONT);
        menuFormat.setFont(MENU_FONT);
        menuColor.setFont(MENU_FONT);
        menuAbout.setFont(MENU_FONT);

        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuFormat);
        menuBar.add(menuColor);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuAbout);
    }

    public void createFileMenu() {
        iNew = new JMenuItem("New");
        iNew.addActionListener(e -> fileHandler.newFile());
        iNew.setFont(MENU_FONT);

        iOpen = new JMenuItem("Open");
        iOpen.addActionListener(e -> fileHandler.open());
        iOpen.setFont(MENU_FONT);

        iSave = new JMenuItem("Save");
        iSave.addActionListener(e -> fileHandler.save());
        iSave.setFont(MENU_FONT);

        iSaveAs = new JMenuItem("Save As");
        iSaveAs.addActionListener(e -> fileHandler.saveAs());
        iSaveAs.setFont(MENU_FONT);

        iExit = new JMenuItem("Exit");
        iExit.addActionListener(e -> fileHandler.exit());
        iExit.setFont(MENU_FONT);

        menuFile.add(iNew);
        menuFile.add(iOpen);
        menuFile.add(iSave);
        menuFile.add(iSaveAs);
        menuFile.addSeparator();
        menuFile.add(iExit);
    }

    public void createEditMenu() {
        iCut = new JMenuItem("Cut");
        iCut.setAction(new DefaultEditorKit.CutAction());
        iCut.setFont(MENU_FONT);

        iCopy = new JMenuItem("Copy");
        iCopy.setAction(new DefaultEditorKit.CopyAction());
        iCopy.setFont(MENU_FONT);

        iPaste = new JMenuItem("Paste");
        iPaste.setAction(new DefaultEditorKit.PasteAction());
        iPaste.setFont(MENU_FONT);

        iSelectAll = new JMenuItem("Select All");
        iSelectAll.addActionListener(e -> textPane.selectAll());
        iSelectAll.setFont(MENU_FONT);

        menuEdit.add(iCut);
        menuEdit.add(iCopy);
        menuEdit.add(iPaste);
        menuEdit.addSeparator();
        menuEdit.add(iSelectAll);
    }

    public void createFormatMenu() {
        iWrap = new JCheckBoxMenuItem("Word Wrap");
        iWrap.setFont(MENU_FONT);
        iWrap.addActionListener(e -> textPane.setEditorKit(new WrapEditorKit(iWrap.isSelected())));
        menuFormat.add(iWrap);
    }

    public void createColorMenu() {
        iColorText = new JMenuItem("Text Color");
        iColorText.addActionListener(e -> {
            Color color = JColorChooser.showDialog(window, "Choose Text Color", textPane.getForeground());
            if (color != null) {
                textPane.setForeground(color);
            }
        });
        iColorText.setFont(MENU_FONT);

        iColorBackground = new JMenuItem("Background Color");
        iColorBackground.addActionListener(e -> {
            Color color = JColorChooser.showDialog(window, "Choose Background Color", textPane.getBackground());
            if (color != null) {
                textPane.setBackground(color);
                scrollPane.getViewport().setBackground(color);
            }
        });
        iColorBackground.setFont(MENU_FONT);

        menuColor.add(iColorText);
        menuColor.add(iColorBackground);
    }

    public void createAboutMenu() {
        iAbout = new JMenuItem("About");
        iAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(window,
                    "<html><div style='text-align: center;'>" +
                            "<h2 style='color: #0078D7;'>Enhanced Text Editor</h2>" +
                            "<p>Created by Rahul , Sachin , Prakash And Gagan</p>" +
                            "<p style='color: #666; font-size: 11px;'>Version 2.0</p></div></html>",
                    "About",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        iAbout.setFont(MENU_FONT);
        menuAbout.add(iAbout);
    }

    public JMenuItem createStyledMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(MENU_FONT);
        item.addActionListener(listener);
        return item;
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(window, message, title, messageType);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public JFrame getWindow() {
        return window;
    }
}

class WrapEditorKit extends StyledEditorKit {

    private final boolean wrap;

    public WrapEditorKit(boolean wrap) {
        this.wrap = wrap;
    }

    @Override
    public ViewFactory getViewFactory() {
        return wrap ? new WrapColumnFactory() : super.getViewFactory();
    }

    static class WrapColumnFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new WrapLabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }

    static class WrapLabelView extends LabelView {

        public WrapLabelView(Element elem) {
            super(elem);
        }

        public float getMinimumSpan(int axis) {
            return axis == View.X_AXIS ? 0 : super.getMinimumSpan(axis);
        }
    }
}