package cn.tz.chenjia.ui.configs;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class InputMaxLength extends PlainDocument {

    int maxChars;

    public InputMaxLength(int max) {
        maxChars = max;
    }

    public void insertString(int offset, String s, AttributeSet a)
            throws BadLocationException {
        if (getLength() + s.length() > maxChars) {
            JOptionPane.showMessageDialog(null, "最多5000字！", "字数上限", JOptionPane.WARNING_MESSAGE);
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        super.insertString(offset, s, a);
    }

}
