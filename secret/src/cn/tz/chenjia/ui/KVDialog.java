package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ImagesConfig;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.service.CmdSevrice;

import javax.swing.*;
import java.awt.event.*;

public class KVDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveBtn;
    private JButton buttonCancel;
    private JTextField titleText;
    private JTextArea contentTextArea;
    private JPanel jPanel1;
    private JPanel mainJPanel;
    private JScrollPane jScrollPane;
    private JPanel statusJPanel;
    private JLabel statusLabel;

    public KVDialog() {
        setTitle("Secret");
        setContentPane(contentPane);
        setLocationRelativeTo(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveBtn);
        setIconImage(ImagesConfig.getLogo());
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String title = titleText.getText().trim();
        String content = contentTextArea.getText().trim();

        if(title.equals("") || content.equals("")){
            statusLabel.setText(EMsg.ERROR_KV.toString());
        }else{
            CmdSevrice.updateInfo(title, content);
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
