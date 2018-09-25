package cn.tz.chenjia.ui.configs;

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
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        super.insertString(offset, s, a);
    }

}
