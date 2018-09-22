package cn.tz.chenjia.service;

import cn.tz.chenjia.entity.Help;
import cn.tz.chenjia.entity.User;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.rule.ESymbol;
import cn.tz.chenjia.ui.KVDialog;
import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.utils.ReadmeToggle;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdSevrice implements ICmdService {

    private Commands command;

    private CmdSevrice(Commands command) {
        this.command = command;
    }

    public static String input() {
        String content = "";
        try {
            do {
                EMsg.println(EMsg.INPUT);
                content = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
            } while (content == null || content.equals(""));
        } catch (IOException e) {
            EMsg.println(e.getMessage());
            System.exit(1);
        }
        return content;
    }

    public static void runCmd(Commands command) {
        if (command == null) {
            runCmd(Commands.toCmd(input()));
            return;
        }
        CmdSevrice cmdSevrice = new CmdSevrice(command);
        cmdSevrice.run();
    }

    public static String runCmdWithJForm(Commands command){
        if (command == null) {
            return EMsg.INVALID.toString();
        }
        CmdSevrice cmdSevrice = new CmdSevrice(command);
        String run = cmdSevrice.run();
        EMsg.println(run);
        return run;
    }

    public String run() {
        String r = "";
        switch (command) {
            case LOGINOUT:
                r = loginOut();
                break;
            case LOGIN:
                r = login();
                break;
            case OUT:
                r = out();
                break;
            case PUT:
                r = put();
                break;
            case REMOVE:
                r = remove();
                break;
            case FIND:
                r = find();
                break;
            case HELP:
                r =  help();
                break;
        }
        return r;
    }

    @Override
    public String loginOut() {
        User.getInstance().out();
        return EMsg.LOGIN_OUT.toString();
    }

    @Override
    public String login() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        String name = strs[1];
        String password = strs[2];
        Integer n = Integer.valueOf(strs[3]);
        name = EncryptUtils.encrypt(name, password, n);
        password = EncryptUtils.encrypt(password, password, n);
        if (User.getInstance().isOnline()) {
            return EMsg.ONLINE.toString();
        } else {
            if (User.getInstance().login(name, password, n)) {
                return EMsg.LOGIN_OK.toString();
            } else {
                return EMsg.LOGIN_NO.toString();
            }
        }
    }

    public static boolean login(String name, String pwd, int n){
        boolean r = false;
        name = EncryptUtils.encrypt(name, pwd, n);
        pwd = EncryptUtils.encrypt(pwd, pwd, n);
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
        KVDialog dialog = new KVDialog();
        dialog.pack();
        dialog.setVisible(true);
        return EMsg.PUT_OK.toString();
    }

    @Override
    public String remove() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        if (strs.length == 2) {
            removeInfo(strs[1]);
        } else {
            removeInfo(null);
        }
       return EMsg.REMOVE_OK.toString();
    }

    private void removeInfo(String code) {
        String readme = ReadmeToggle.read();
        String[] str = readme.split(ERegexp.SEMICOLON_RE.toString());
        String r;
        if (str.length > 3) {
            r = str[3];
            r = EncryptUtils.decrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
            JSONArray ja = JSONObject.parseArray(r);
            if (code == null || code.equals("")) {
                ja.clear();
            } else {
                for (int i = 0; i < ja.size(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String c = jo.getString("code");
                    if (c.equals(code)) {
                        ja.remove(jo);
                        break;
                    }
                }
            }
            r = JSONObject.toJSONString(ja);
            r = str[0] + ESymbol.SEMICOLON.toString() + str[1] + ESymbol.SEMICOLON.toString() + str[2] + ESymbol.SEMICOLON.toString() +  EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        } else {
            JSONArray ja = new JSONArray();
            r = JSONObject.toJSONString(ja);
            r = str[0] + ESymbol.SEMICOLON.toString() + str[1] + ESymbol.SEMICOLON.toString() + str[2] + ESymbol.SEMICOLON.toString() +  EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        }
        ReadmeToggle.write(r);
    }

    public static void updateInfo(String code, String info) {
        boolean ok = false;
        String readme = ReadmeToggle.read();
        String[] str = readme.split(ERegexp.SEMICOLON_RE.toString());
        String r;
        if (str.length > 3) {
            r = str[3];
            r = EncryptUtils.decrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
            JSONArray ja = JSONObject.parseArray(r);
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String c = jo.getString("code");
                if (c.equals(code)) {
                    jo.put("info", info);
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                JSONObject jo = new JSONObject();
                jo.put("code", code);
                jo.put("info", info);
                ja.add(jo);
            }
            r = JSONObject.toJSONString(ja);
            r = str[0] + ESymbol.SEMICOLON.toString() + str[1] + ESymbol.SEMICOLON.toString()+ str[2] + ESymbol.SEMICOLON.toString() + EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        } else {
            JSONArray ja = new JSONArray();
            JSONObject jo = new JSONObject();
            jo.put("code", code);
            jo.put("info", info);
            ja.add(jo);
            r = JSONObject.toJSONString(ja);
            r = str[0] + ESymbol.SEMICOLON.toString() + str[1] + ESymbol.SEMICOLON.toString() + str[2] + ESymbol.SEMICOLON.toString() + EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        }
        ReadmeToggle.write(r);
    }

    @Override
    public String find() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        if (strs.length == 2) {
            return findRecord(strs[1]);
        }else {
            return findRecord(null);
        }
    }

    private String findRecord(String code) {
        String readme = ReadmeToggle.read();
        String[] str = readme.split(ERegexp.SEMICOLON_RE.toString());
        String result = "";
        if (str.length > 3) {
            String r = str[3];
            r = EncryptUtils.decrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
            JSONArray ja = JSONObject.parseArray(r);
            if (code == null || code.equals("")) {
                for (int i = 0; i < ja.size(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String c = jo.getString("code");
                    //String info = EncryptUtils.decrypt(jo.getString("info"), User.getInstance().getPwd(), User.getInstance().getN());
                    result += c + "\n";
                }
            }else{
                for (int i = 0; i < ja.size(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String c = jo.getString("code");
                    if (c.equals(code)) {
                        result = c + ":\n" + jo.getString("info");
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String help() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        if (strs.length == 2) {
            return Help.getInstance().findSingleHelp(strs[1]);
        }else {
            return Help.getInstance().findAllHelp();
        }
    }
}
