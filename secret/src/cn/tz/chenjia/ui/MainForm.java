package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.service.CmdSevrice;
import cn.tz.chenjia.service.Commands;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainForm extends JFrame {

    public MainForm(String userName) {
        setTitle("Secret");
        setContentPane(mainJPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        //setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(ConfigsUtils.getLogo());
        pack();
        setVisible(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }

            @Override
            public void windowOpened(WindowEvent e) {
                cmdText.requestFocus();
                sessionTextArea.setMargin(new Insets(5, 5, 5, 5));
                sessionTextArea.setText(EMsg.HELLO.toString());
                statusLabel.setText(userName + EMsg.WELCOME);
            }
        });
        cmdText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                    String cmd = cmdText.getText().trim().toLowerCase();
                    sessionTextArea.setText("");
                    if (cmd.matches(ERegexp.CMD_CLEAR_RE.toString())) {
                        sessionTextArea.setText("");
                    } else if (cmd.matches(ERegexp.CMD_OUT_RE.toString())) {
                        dispose();
                        System.exit(1);
                    } else if (cmd.matches(ERegexp.CMD_LOGINOUT_RE.toString()) || cmd.matches(ERegexp.CMD_FORMAT_RE.toString()) || cmd.matches(ERegexp.CMD_PASSWORD_RE.toString())) {
                        String r = CmdSevrice.runCmdWithJForm(Commands.toCmd(cmd));
                        outLogin(r);
                    } else {
                        String r = CmdSevrice.runCmdWithJForm(Commands.toCmd(cmd));
                        if (r != null && !r.equals("")) {
                            sessionTextArea.append(r + "\n");
                        }
                    }
                    cmdText.setText("");
                }
            }
        });
        putBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                println(CmdSevrice.runCmdWithJForm(Commands.toCmd("p")));
            }
        });
        findBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String op = (String)JOptionPane.showInputDialog(null,"","输入要查找的标题",
                        JOptionPane.QUESTION_MESSAGE);
                if(op != null){
                    println(CmdSevrice.runCmdWithJForm(Commands.toCmd("f " + op)));
                }
            }
        });
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String op = (String)JOptionPane.showInputDialog(null,"","输入要删除的标题",
                        JOptionPane.QUESTION_MESSAGE);
                if(op != null) {
                    println(CmdSevrice.runCmdWithJForm(Commands.toCmd("r " + op)));
                }
            }
        });
        pwdBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] n = {0,1,2,3,4,5,6,7,8,9};
                String newPwd = (String)JOptionPane.showInputDialog(null,"","输入新密码",
                        JOptionPane.QUESTION_MESSAGE);
                if(newPwd != null){
                    Object ob = JOptionPane.showInputDialog(null, "", "选择加密数字", JOptionPane.QUESTION_MESSAGE, null, n, n[0]);
                    if(ob != null) {
                        String r = CmdSevrice.runCmdWithJForm(Commands.toCmd("p " + newPwd + " " + Integer.valueOf(ob.toString())));
                        outLogin(r);
                    }
                }
            }
        });
        backupsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                println(CmdSevrice.runCmdWithJForm(Commands.toCmd("b")));
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sessionTextArea.setText("");
            }
        });
        loginOutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outLogin(EMsg.OUT_OK.toString());
            }
        });
        formatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outLogin(CmdSevrice.runCmdWithJForm(Commands.toCmd("format")));
            }
        });
        helpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                println(CmdSevrice.runCmdWithJForm(Commands.toCmd("h")));
            }
        });
    }

    private void outLogin(String r){
        if(Boolean.valueOf(r)){
            CmdSevrice.runCmdWithJForm(Commands.toCmd("lo"));
            new LoginForm();
            dispose();
        }
    }

    private void println(String r){
        sessionTextArea.setText("");
        if (r != null && !r.equals("")) {
            sessionTextArea.append(r + "\n");
        }
    }

    private JPanel mainJPanel;
    private JTextArea sessionTextArea;
    private JTextField cmdText;
    private JScrollPane sessionJScrollPane;
    private JPanel statusJPanel;
    private JLabel statusLabel;
    private JPanel shortcutJPanel;
    private JButton putBtn;
    private JButton findBtn;
    private JButton delBtn;
    private JButton pwdBtn;
    private JButton backupsBtn;
    private JButton clearBtn;
    private JButton loginOutBtn;
    private JButton formatBtn;
    private JButton helpBtn;
}
