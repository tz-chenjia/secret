package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.entity.DB_Secret;
import cn.tz.chenjia.entity.User;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.service.CmdSevrice;
import cn.tz.chenjia.service.Commands;
import cn.tz.chenjia.ui.configs.InputMaxLength;
import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.utils.SecretRWUtils;

import javax.swing.*;
import java.awt.*;
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
    private JLabel inputNumLabel;
    private boolean save;
    private boolean isEdit;

    public KVDialog(boolean isEdit, String title) {
        this.isEdit = isEdit;
        setTitle("Secret");
        setContentPane(contentPane);
        setSize(800, 800);
        setLocationRelativeTo(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveBtn);
        setIconImage(ConfigsUtils.getLogo());
        setResizable(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                contentTextArea.setMargin(new Insets(5, 5, 5, 5));
                contentTextArea.setDocument(new InputMaxLength(4999));
                titleText.setDocument(new InputMaxLength(100));
                if(isEdit){
                    titleText.setText(title);
                    titleText.setEditable(false);
                    DB_Secret secret = SecretRWUtils.readSecretByTitle(title, User.getInstance().getName(), User.getInstance().getPwd(), User.getInstance().getN());
                    String content = EncryptUtils.decrypt(secret.getContent(),User.getInstance().getPwd(), User.getInstance().getN());
                    contentTextArea.setText(content);
                }
            }
        });




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

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int len = contentTextArea.getText().length() + 1;
                inputNumLabel.setText(len  + "/5000");
            }
        });
    }

    private void onOK() {
        String title = titleText.getText().trim();
        String content = contentTextArea.getText().trim();

        if (title.equals("") || content.equals("")) {
            statusLabel.setText(EMsg.ERROR_KV.toString());
        } else if(title.indexOf(" ") != -1){
            statusLabel.setText(EMsg.ERROR_KV5.toString());
        }else if (EncryptUtils.encrypt(title, CmdSevrice.class.getName(), 1).equalsIgnoreCase(User.getInstance().getName())) {
            statusLabel.setText(EMsg.ERROR_KV2.toString());
        } else if(checkTitleAsCmds(title)){
            statusLabel.setText(EMsg.ERROR_KV3.toString());
        }else if(!isEdit && checkTitleExists(title)){
            statusLabel.setText(EMsg.ERROR_KV4.toString());
        }else {
            CmdSevrice.updateInfo(title, content);
            save = true;
            dispose();
        }
    }

    private boolean checkTitleExists(String title){
        boolean r = false;
        String[] titles = SecretRWUtils.readUserTitles(User.getInstance().getName(), User.getInstance().getPwd(), User.getInstance().getN(), null);
        for(String t : titles){
            if(t.equalsIgnoreCase(title)){
                r = true;
            }
        }
        return r;
    }

    private boolean checkTitleAsCmds(String title){
        boolean r = false;
        for(String cmd : Commands.cmds){
            if(cmd.equalsIgnoreCase(title)){
                r = true;
                break;
            }
        }
        return  r;
    }

    private void onCancel() {
        save = false;
        dispose();
    }

    public boolean isSave() {
        return save;
    }
}
