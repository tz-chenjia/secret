package cn.tz.chenjia.ui;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.db.JDBCUtils;
import cn.tz.chenjia.db.SecretDAO;
import cn.tz.chenjia.rule.EDBType;
import cn.tz.chenjia.service.CmdSevrice;

import javax.swing.*;
import java.awt.event.*;
import java.util.Properties;

public class LoginForm extends JFrame {
    private static final String SECRET_TABLE_NAME = "DB_Secret";

    public LoginForm() {
        setTitle("Secret");
        setContentPane(mainJPanel);
        setSize(200, 130);
        setLocationRelativeTo(mainJPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(ConfigsUtils.getLogo());
        setResizable(false);
        pack();
        setVisible(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Properties prop = ConfigsUtils.getDBProperties();
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

    private static void fileChooser() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(new JPanel());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("你打开的文件是: " +
                    chooser.getSelectedFile().getPath());
        }
    }

    private void login() {
        String dbIP = dbIPText.getText();
        String dbPort = dbPortText.getText();
        String dbUserName = dbUserNameText.getText();
        String dbName = dbNameText.getText();
        String dbPpassword = String.valueOf(dbPwdText.getPassword());
        String userName = userNameText.getText();
        String pwd = String.valueOf(pwdText.getPassword());
        EDBType dbType = EDBType.toEDBType(dbTypeComboBox.getSelectedItem().toString());
        if (JDBCUtils.isUseDB(dbIP, dbPort, dbName, dbUserName, dbPpassword, dbType, userName)) {
            SecretDAO dao = new SecretDAO();
            if (!dao.tableExists(SECRET_TABLE_NAME)) {
                String tableSql = "create table " + SECRET_TABLE_NAME + " (username varchar(50) not null primary key,"
                        + "secret varchar(3000) not null ); ";
                dao.update(tableSql, new Object[]{});
            }
            int n = Integer.valueOf(nComboBox.getSelectedItem().toString());
            if (CmdSevrice.login(userName, pwd, n)) {
                new MainForm(userName);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null,"请检查你的用户名密码及幸运数字", "登录失败", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null,"数据库连接失败，请检查网络配置", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancel() {
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
