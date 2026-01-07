package rentalmobil.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.print.PrinterException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class PrintUtil {

    public static void printText(Component parent, String title, String content) {
        JTextArea area = new JTextArea(content);
        area.setFont(new Font("Monospaced", Font.PLAIN, 10));
        try {
            boolean done = area.print();
            if (!done) {
                JOptionPane.showMessageDialog(parent, "Pencetakan dibatalkan.", title, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Gagal mencetak", JOptionPane.ERROR_MESSAGE);
        }
    }
}

