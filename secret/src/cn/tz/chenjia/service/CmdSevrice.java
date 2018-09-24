package cn.tz.chenjia.service;

import cn.tz.chenjia.entity.Help;
import cn.tz.chenjia.entity.User;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.rule.ESymbol;
import cn.tz.chenjia.ui.KVDialog;
import cn.tz.chenjia.ui.RemoveDialog;
import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.utils.SecretRWUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CmdSevrice implements ICmdService {

    private Commands command;

    private CmdSevrice(Commands command) {
        this.command = command;
    }

    public static String runCmdWithJForm(Commands command){
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
            case FILEPATH:
                r =  path();
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
        if(dialog.isSave()){
            return EMsg.SAVE_OK.toString();
        }else {
            return "";
        }
    }

    @Override
    public String remove() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        String code = strs.length == 2 ? strs[1] : null;
        RemoveDialog dialog = new RemoveDialog(code);
        dialog.pack();
        dialog.setVisible(true);
        if(dialog.isRemove()){
            removeInfo(code);
            return EMsg.REMOVE_OK.toString();
        }else {
            return "";
        }
    }

    public static void removeInfo(String code) {
        JSONObject secretJO = SecretRWUtils.readSecret(User.getInstance().getPwd(), User.getInstance().getN());
        String data = secretJO.getString("data");
        if(!data.equals("")){
            data = EncryptUtils.decrypt(data, User.getInstance().getPwd(), User.getInstance().getN());
            JSONArray ja = JSONObject.parseArray(data);
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
            data = ja.toJSONString();
        }else {
            JSONArray ja = new JSONArray();
            data = ja.toJSONString();
        }
        secretJO.put("data", EncryptUtils.encrypt(data,  User.getInstance().getPwd(), User.getInstance().getN()));
        SecretRWUtils.write(EncryptUtils.encrypt(secretJO.toJSONString(), User.getInstance().getPwd(), User.getInstance().getN()));
    }

    public static void updateInfo(String code, String info) {
        boolean ok = false;
        JSONObject secretJO = SecretRWUtils.readSecret(User.getInstance().getPwd(), User.getInstance().getN());
        String data = secretJO.getString("data");
        if(!data.equals("")){
            data = EncryptUtils.decrypt(data, User.getInstance().getPwd(), User.getInstance().getN());
            JSONArray ja = JSONObject.parseArray(data);
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
            data = ja.toJSONString();
        }else{
            JSONArray ja = new JSONArray();
            JSONObject jo = new JSONObject();
            jo.put("code", code);
            jo.put("info", info);
            ja.add(jo);
            data = ja.toJSONString();
        }
        secretJO.put("data", EncryptUtils.encrypt(data, User.getInstance().getPwd(), User.getInstance().getN()));
        SecretRWUtils.write(EncryptUtils.encrypt(secretJO.toJSONString(), User.getInstance().getPwd(), User.getInstance().getN()));
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

    @Override
    public String path() {
        return SecretRWUtils.getSecretPath();
    }

    private String findRecord(String code) {
        String result = "";
        JSONObject secretJO = SecretRWUtils.readSecret(User.getInstance().getPwd(), User.getInstance().getN());
        String data = secretJO.getString("data");
        if(!data.equals("")){
            data = EncryptUtils.decrypt(data, User.getInstance().getPwd(), User.getInstance().getN());
            JSONArray ja = JSONObject.parseArray(data);
            if (code == null || code.equals("")) {
                for (int i = 0; i < ja.size(); i++) {
                    result = i == 0 ? result : result + "\n" + ESymbol.BORDER + "\n";
                    JSONObject jo = ja.getJSONObject(i);
                    String c = jo.getString("code");
                    String info = jo.getString("info");
                    result +=  "【" + c + "】";
                }
            }else{
                for (int i = 0; i < ja.size(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String c = jo.getString("code");
                    if (c.toLowerCase().contains(code.toLowerCase())) {
                        String info = jo.getString("info");
                        result = result.equals("") ? result : result + "\n" + ESymbol.BORDER + "\n";
                        result += "【" + c + "】\n" + ESymbol.BORDER2 + "\n" + info;
                    }
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
        }else {
            return Help.getInstance().findAllHelp();
        }
    }
}
