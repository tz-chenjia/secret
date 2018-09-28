package cn.tz.chenjia.service;

import cn.tz.chenjia.email.SimpleMailSender;
import cn.tz.chenjia.entity.DB_Secret;
import cn.tz.chenjia.entity.Help;
import cn.tz.chenjia.entity.User;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.rule.ESymbol;
import cn.tz.chenjia.ui.FileChooser;
import cn.tz.chenjia.ui.KVDialog;
import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.utils.SecretRWUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdSevrice implements ICmdService {

    private static final Logger log = Logger.getLogger(CmdSevrice.class);

    private Commands command;

    private CmdSevrice(Commands command) {
        this.command = command;
    }

    public static String runCmdWithJForm(Commands command) {
        if (command == null) {
            return EMsg.INVALID.toString();
        }
        CmdSevrice cmdSevrice = new CmdSevrice(command);
        String run = cmdSevrice.run();
        return run;
    }

    public String run() {
        String r = "";
        switch (command) {
            case LOGINOUT:
                r = loginOut();
                break;
            case OUT:
                r = out();
                break;
            case PUT:
                r = put();
                break;
            case EDIT:
                r = edit();
                break;
            case REMOVE:
                r = remove();
                break;
            case FIND:
                r = find();
                break;
            case FILEPATH:
                r = path();
                break;
            case FORMAT:
                r = format();
                break;
            case BACKUPS:
                r = backups();
                break;
            case COVER:
                r = cover();
                break;
            case PASSWORD:
                r = password();
                break;
            case HELP:
                r = help();
                break;
        }
        return r;
    }

    @Override
    public String loginOut() {
        User.getInstance().out();
        return EMsg.OUT_OK.toString();
    }

    public static boolean login(String name, String pwd, int n) {
        boolean r = false;
        name = EncryptUtils.encrypt(name, CmdSevrice.class.getName(), 1);
        pwd = EncryptUtils.encrypt(pwd, pwd, 1);
        if (User.getInstance().isOnline()) {
            r = true;
        } else {
            if (User.getInstance().login(name, pwd, n)) {
                r = true;
            }
        }
        return r;
    }

    @Override
    public String out() {
        return EMsg.OUT.toString();
    }

    @Override
    public String put() {
        KVDialog dialog = new KVDialog(false,"");
        dialog.pack();
        dialog.setVisible(true);
        if (dialog.isSave()) {
            return EMsg.SAVE_OK.toString();
        } else {
            return "";
        }
    }

    @Override
    public String edit() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        KVDialog dialog = new KVDialog(true,strs[1]);
        dialog.pack();
        dialog.setVisible(true);
        if (dialog.isSave()) {
            return EMsg.SAVE_OK.toString();
        } else {
            return "";
        }
    }

    @Override
    public String remove() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        String code = strs.length == 2 ? strs[1] : null;
        String tips = code == null ? "确定删除全部数据吗？" : "确定删除标题为【" + code + "】的数据吗？";
        int i = JOptionPane.showConfirmDialog(null, tips, "删除数据", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            if (code != null) {
                int remove = SecretRWUtils.remove(code, User.getInstance().getName(), User.getInstance().getPwd(), User.getInstance().getN());
                if(remove > 0){
                    return EMsg.REMOVE_OK.toString();
                }else {
                    return EMsg.REMOVE_FAIL.toString();
                }
            } else {
                SecretRWUtils.removeAll(User.getInstance().getName());
            }
            return EMsg.REMOVE_OK.toString();
        } else {
            return "";
        }
    }

    public static void updateInfo(String code, String info) {
        SecretRWUtils.write(code, info, User.getInstance().getName(), User.getInstance().getPwd(), User.getInstance().getN());
    }

    @Override
    public String find() {
        String cmd = command.getInput();
        String title = "";
        if(cmd.matches(ERegexp.CMD_FIND_RE.toString())){
            String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
            if (strs.length == 2) {
                title = strs[1];
            }
        }else{
            title = cmd;
        }
        return findRecord(title);
    }

    @Override
    public String path() {
        return SecretRWUtils.getExportSQLFile().getPath();
    }

    @Override
    public String format() {
        String tips = "格式化将清空所有数据，确定要格式化吗？";
        int i = JOptionPane.showConfirmDialog(null, tips, "格式化", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            SecretRWUtils.removeUser(User.getInstance().getName());
            return EMsg.OUT_OK.toString();
        } else {
            return EMsg.OUT_CANCEL.toString();
        }
    }

    private String findRecord(String code) {
        String result = "";
        List<DB_Secret> db_secrets = SecretRWUtils.readSecret(User.getInstance().getName(), User.getInstance().getPwd(), User.getInstance().getN());
        for (DB_Secret secret : db_secrets) {
            String title = secret.getTitle();
            String content = secret.getContent();
            if (!title.equals(User.getInstance().getName())) {
                String t = EncryptUtils.decrypt(title, User.getInstance().getPwd(), User.getInstance().getN());
                String c = EncryptUtils.decrypt(content, User.getInstance().getPwd(), User.getInstance().getN());
                if (code == null || code.equals("")) {
                    result = result.equals("") ? result : result + "\n" + ESymbol.BORDER + "\n";
                    result += "【" + t + "】";
                } else if (containsIgnoreCase(t,code)) {
                    result = result.equals("") ? result : result + "\n" + ESymbol.BORDER + "\n";
                    result += "【" + t + "】\n\n" + c;
                }
            }
        }
        result = result.equals("") ? EMsg.INFO_NOT.toString() : result;
        return result;
    }

    @Override
    public String help() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        if (strs.length == 2) {
            return Help.getInstance().findSingleHelp(strs[1]);
        } else {
            return Help.getInstance().findAllHelp();
        }
    }

    @Override
    public String password() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());

        resetPwd(User.getInstance().getName(), strs[1], Integer.valueOf(strs[2]));

        return EMsg.OUT_OK.toString();
    }

    private void resetPwd(String userName, String newPwd, int newN) {
        String pwd = EncryptUtils.encrypt(newPwd, newPwd, 1);
        List<DB_Secret> db_secrets = SecretRWUtils.readSecret(User.getInstance().getName(), User.getInstance().getPwd(), User.getInstance().getN());
        SecretRWUtils.removeUser(userName);
        for (DB_Secret secret : db_secrets) {
            if (!secret.getTitle().equals(secret.getUsername())) {
                String title = EncryptUtils.decrypt(secret.getTitle(), User.getInstance().getPwd(), User.getInstance().getN());
                String content = EncryptUtils.decrypt(secret.getContent(), User.getInstance().getPwd(), User.getInstance().getN());
                secret.setContent(title);
                secret.setContent(content);
                SecretRWUtils.write(title, content, userName, pwd, newN);
            } else {
                JSONObject jo = new JSONObject();
                jo.put("pwd", pwd);
                jo.put("n", Integer.valueOf(newN));
                String content = EncryptUtils.encrypt(jo.toJSONString(), pwd, Integer.valueOf(newN));
                SecretRWUtils.write(userName, content, userName, null, null);
            }

        }
    }

    @Override
    public String backups() {
        SecretRWUtils.exportSQL(User.getInstance().getName(), User.getInstance().getPwd(), User.getInstance().getN());
        Map<String, File> files = new HashMap<String, File>();
        files.put("secret.sql", SecretRWUtils.getExportSQLFile());
        String tips = "是否需要发送到你的邮箱？";
        int i = JOptionPane.showConfirmDialog(null, tips, "备份", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            boolean b = SimpleMailSender.sendMail(EncryptUtils.decrypt(User.getInstance().getName(), CmdSevrice.class.getName(), 1), "【Secret】", "数据备份文件已存放在附件中，请注意查收，祝您生活愉快！", files);
            if (b) {
                return EMsg.BACKUPS_OK + EMsg.BACKUPS_EMAIL_OK.toString();
            }
            return EMsg.BACKUPS_OK.toString() + EMsg.BACKUPS_EMAIL_FAIL;
        }else {
            return EMsg.BACKUPS_OK.toString();
        }
    }

    @Override
    public String cover() {
        JOptionPane.showMessageDialog(null,EMsg.IMP_TIPS);
        File file = FileChooser.fileChooser();
        if(file != null){
            if(SecretRWUtils.importSQL(file)){
                JOptionPane.showMessageDialog(null,EMsg.IMP_OK);
                return EMsg.OUT_OK.toString();
            }else {
                JOptionPane.showMessageDialog(null,EMsg.IMP_FAILS);
                log.warn(EMsg.IMP_FAILS);
                return EMsg.OUT_CANCEL.toString();
            }
        }else {
            return EMsg.OUT_CANCEL.toString();
        }
    }

    private boolean containsIgnoreCase(String v1, String v2){
        boolean r = false;
        v1 = v1.toLowerCase();
        v2 = v2.toLowerCase();
        return v1.contains(v2);
    }

}
