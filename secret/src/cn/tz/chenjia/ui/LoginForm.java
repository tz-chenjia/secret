package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ImagesConfig;
import cn.tz.chenjia.service.CmdSevrice;

import javax.swing.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    public LoginForm() {
        setTitle("Secret");
        setContentPane(mainJPanel);
        setSize(200,100);
        setLocationRelativeTo(mainJPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(ImagesConfig.getLogo());
        setResizable(false);
        pack();
        setVisible(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        loginBtn.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        },KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cancelBtn.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        },KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        nComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(KeyEvent.VK_ENTER == e.getKeyCode()){
                    loginBtn.doClick();
                }
            }
        });
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
    }

    private void login(){
        String userName = userNameText.getText();
        String pwd = String.valueOf(pwdText.getPassword());
        int n = Integer.valueOf(nComboBox.getSelectedItem().toString()) ;
        if(CmdSevrice.login(userName, pwd, n)){
            new MainForm(userName);
            dispose();
        }else{
            statusLabel.setText("登录失败，请重新登录");
        }
    }

    private void cancel(){
        dispose();
        System.exit(1);
    }

    private JPanel mainJPanel;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JLabel statusLabel;
    private JButton cancelBtn;
    private JButton loginBtn;
    private JTextField userNameText;
    private JLabel userNameLabel;
    private JLabel pwdLabel;
    private JLabel nLabel;
    private JPasswordField pwdText;
    private JComboBox nComboBox;
}
