package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.service.CmdSevrice;

import javax.swing.*;
import java.awt.event.*;

public class RemoveDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel label;
    private String code;

    public RemoveDialog(String code) {
        String title = code == null ? "确定删除全部数据？" : "确定删除 【" + code + "】？";
        setTitle("Secret");
        label.setText(title);
        setContentPane(contentPane);
        setModal(true);
        setIconImage(ConfigsUtils.getLogo());
        getRootPane().setDefaultButton(buttonOK);
        setSize(400,300);
        setLocationRelativeTo(contentPane);
        setResizable(false);

        buttonOK.addActionListener(new ActionListener() {
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
        CmdSevrice.removeInfo(code);
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
