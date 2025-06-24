import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class Function_File {
    GUI gui;
    String fileName;
    String fileAddress;

    public Function_File(GUI gui) {
        this.gui = gui;
    }

    public void newFile() {
        gui.getTextPane().setText("");
        gui.window.setTitle("New");
        fileName = null;
        fileAddress = null;
    }

    public void open() {
        FileDialog fd = new FileDialog(gui.window, "Open", FileDialog.LOAD);
        fd.setVisible(true);

        if (fd.getFile() != null) {
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            gui.window.setTitle(fileName);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileAddress + fileName));
            StyledDocument doc = gui.getTextPane().getStyledDocument();
            doc.remove(0, doc.getLength()); // clear existing content

            String line;
            while ((line = br.readLine()) != null) {
                doc.insertString(doc.getLength(), line + "\n", null);
            }
            br.close();
        } catch (IOException | BadLocationException e) {
            System.out.println("File not open");
            e.printStackTrace();
        }
    }

    public void save() {
        if (fileName == null) {
            saveAs();
        } else {
            try {
                FileWriter fw = new FileWriter(fileAddress + fileName);
                fw.write(gui.getTextPane().getText());
                gui.window.setTitle(fileName);
                fw.close();
            } catch (IOException e) {
                System.out.println("File not saved");
                e.printStackTrace();
            }
        }
    }

    public void saveAs() {
        FileDialog fd = new FileDialog(gui.window, "Save", FileDialog.SAVE);
        fd.setVisible(true);

        if (fd.getFile() != null) {
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            gui.window.setTitle(fileName);
        }

        try {
            FileWriter fw = new FileWriter(fileAddress + fileName);
            fw.write(gui.getTextPane().getText());
            fw.close();
        } catch (IOException e) {
            System.out.println("File not saved");
            e.printStackTrace();
        }
    }

    public void exit() {
        System.exit(0);
    }
}