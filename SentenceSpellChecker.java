import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SentenceSpellChecker extends JFrame {
    private JTextPane textPane;
    private Set<String> dictionary;
    private Timer spellCheckTimer;

    public SentenceSpellChecker() {
        // Load full dictionary
        dictionary = createDictionary();

        // Setup JTextPane
        textPane = new JTextPane();
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Spell check delay
        spellCheckTimer = new Timer(300, e -> checkSpelling());
        spellCheckTimer.setRepeats(false);

        // Listen to changes in text
        textPane.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                scheduleSpellCheck();
            }

            public void removeUpdate(DocumentEvent e) {
                scheduleSpellCheck();
            }

            public void changedUpdate(DocumentEvent e) {
                scheduleSpellCheck();
            }
        });

        add(new JScrollPane(textPane));
        setTitle("Sentence Spell Checker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Set<String> createDictionary() {
        Set<String> dict = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dict.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "words.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return dict;
    }

    private void scheduleSpellCheck() {
        spellCheckTimer.restart();
    }

    private void checkSpelling() {
        try {
            StyledDocument doc = textPane.getStyledDocument();
            String text = doc.getText(0, doc.getLength());

            // Clear previous styles
            SimpleAttributeSet defaultAttr = new SimpleAttributeSet();
            StyleConstants.setForeground(defaultAttr, Color.BLACK);
            StyleConstants.setUnderline(defaultAttr, false);
            doc.setCharacterAttributes(0, text.length(), defaultAttr, true);

            int pos = 0;
            while (pos < text.length()) {
                while (pos < text.length() && !Character.isLetter(text.charAt(pos))) {
                    pos++;
                }

                int wordStart = pos;

                while (pos < text.length() && Character.isLetter(text.charAt(pos))) {
                    pos++;
                }
                if (wordStart < pos) {
                    String word = text.substring(wordStart, pos).toLowerCase();
                    if (!dictionary.contains(word)) {
                        SimpleAttributeSet attrs = new SimpleAttributeSet();
                        StyleConstants.setForeground(attrs, Color.RED);
                        StyleConstants.setUnderline(attrs, true);
                        doc.setCharacterAttributes(wordStart, pos - wordStart, attrs, false);
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SentenceSpellChecker());
    }
}