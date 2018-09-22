package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.rule.ESymbol;
import cn.tz.chenjia.service.CmdSevrice;
import cn.tz.chenjia.service.Commands;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainForm extends JFrame{

    public MainForm(String userName){
        setTitle("Secret");
        setContentPane(mainJPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(580, 540);
        setResizable(false);
        setLocationRelativeTo(mainJPanel);
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
                sessionTextArea.setText(EMsg.HELLO.toString());
                statusLabel.setText( userName + EMsg.WELCOME);
            }
        });
        cmdText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(KeyEvent.VK_ENTER == e.getKeyCode()){
                    String cmd = cmdText.getText().trim().toLowerCase();
                    if(cmd.matches(ERegexp.CMD_CLEAR_RE.toString())){
                        sessionTextArea.setText("");
                    }if(cmd.matches(ERegexp.CMD_OUT_RE.toString())){
                        dispose();
                        System.exit(1);
                    }else if(cmd.matches(ERegexp.CMD_LOGINOUT_RE.toString())){
                        CmdSevrice.runCmdWithJForm(Commands.toCmd(cmd));
                        new LoginForm();
                        dispose();
                    }else{
                        String r = CmdSevrice.runCmdWithJForm(Commands.toCmd(cmd));
                        if(r != null && !r.equals("")) {
                            sessionTextArea.append("\n" + ESymbol.SESSION_BORDER + "\n" + r + "\n");
                        }
                    }
                    cmdText.setText("");
                }
            }
        });
    }

    private JPanel mainJPanel;
    private JTextArea sessionTextArea;
    private JTextField cmdText;
    private JScrollPane sessionJScrollPane;
    private JPanel statusJPanel;
    private JLabel statusLabel;
}
