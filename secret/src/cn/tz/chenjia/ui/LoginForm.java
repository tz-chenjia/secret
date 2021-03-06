package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.db.JDBCUtils;
import cn.tz.chenjia.db.SecretDAO;
import cn.tz.chenjia.rule.EDBType;
import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.service.CmdSevrice;
import cn.tz.chenjia.ui.configs.InputMaxLength;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.util.Properties;

public class LoginForm extends JFrame {
    private Logger logger = Logger.getLogger(LoginForm.class);

    public LoginForm() {
        setTitle("Secret");
        setContentPane(mainJPanel);
        setSize(500, 500);
        setLocationRelativeTo(mainJPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(ConfigsUtils.getLogo());
        setResizable(false);
        pack();
        setVisible(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                userNameText.setDocument(new InputMaxLength(100));
                pwdText.setDocument(new InputMaxLength(20));

                pwdText.requestFocus();
                Properties prop = ConfigsUtils.getDBProperties();
                if(prop != null){
                    //读取信息
                    String ip = prop.getProperty("ip");
                    String port = prop.getProperty("port");
                    String name = prop.getProperty("name");
                    String userName = prop.getProperty("userName");
                    String password = prop.getProperty("password");
                    String type = prop.getProperty("type");
                    String secretName = prop.getProperty("secretName");
                    dbIPText.setText(ip);
                    dbPortText.setText(port);
                    dbNameText.setText(name);
                    dbUserNameText.setText(userName);
                    dbPwdText.setText(password);
                    dbTypeComboBox.setSelectedItem(type);
                    userNameText.setText(secretName);
                }
            }

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
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        cancelBtn.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        nComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode()) {
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

    private void login() {
        String dbIP = dbIPText.getText();
        String dbPort = dbPortText.getText();
        String dbUserName = dbUserNameText.getText();
        String dbName = dbNameText.getText();
        String dbPassword = String.valueOf(dbPwdText.getPassword());
        String userName = userNameText.getText();
        String pwd = String.valueOf(pwdText.getPassword());

        if (!userName.matches(ERegexp.EMAIL_RE.toString())) {
            JOptionPane.showMessageDialog(null, "账号请使用你的任意邮箱作为账号", "登录失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EDBType dbType = EDBType.toEDBType(dbTypeComboBox.getSelectedItem().toString());
        if (JDBCUtils.isUseDB(dbIP, dbPort, dbName, dbUserName, dbPassword, dbType, userName)) {
            SecretDAO dao = new SecretDAO();
            if (!dao.tableExists(SecretDAO.SECRET_TABLE_NAME)) {
                dao.createTable(dbType, SecretDAO.SECRET_TABLE_NAME);
            }
            int n = Integer.valueOf(nComboBox.getSelectedItem().toString());
            if (CmdSevrice.login(userName, pwd, n)) {
                new MainForm(userName);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "请检查你的用户名密码及加密数字", "登录失败", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络配置", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancel() {
        dispose();
        System.exit(1);
    }

    private JPanel mainJPanel;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JButton cancelBtn;
    private JButton loginBtn;
    private JTextField userNameText;
    private JLabel userNameLabel;
    private JLabel pwdLabel;
    private JLabel nLabel;
    private JPasswordField pwdText;
    private JComboBox nComboBox;
    private JTextField dbUserNameText;
    private JPasswordField dbPwdText;
    private JTextField dbIPText;
    private JLabel dbIPLable;
    private JLabel dbUserNameLable;
    private JLabel dbPwdLabel;
    private JComboBox dbTypeComboBox;
    private JLabel dbTypeLabel;
    private JTextField dbPortText;
    private JLabel dbPortLabel;
    private JTextField dbNameText;
    private JLabel dbNameLabel;
    private JButton fileChooserBtn;
}
