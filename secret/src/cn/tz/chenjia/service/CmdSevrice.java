package cn.tz.chenjia.service;

import cn.tz.chenjia.entity.User;
import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.utils.ReadmeToggle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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

    public void run() {
        switch (command) {
            case LOGINOUT:
                loginOut();
                break;
            case LOGIN:
                login();
                break;
            case OUT:
                out();
                break;
            case PUT:
                put();
                break;
            case REMOVE:
                remove();
                break;
            case FIND:
                find();
                break;
            case HELP:
                help();
                break;
        }
    }

    @Override
    public void loginOut() {
        User.getInstance().out();
        EMsg.println(EMsg.LOGIN_OUT);
    }

    @Override
    public void login() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        String name = strs[1];
        String password = strs[2];
        Integer n = Integer.valueOf(strs[3]);
        name = EncryptUtils.encrypt(name, password, n);
        password = EncryptUtils.encrypt(password, password, n);
        if (User.getInstance().isOnline()) {
            EMsg.println(EMsg.ONLINE);
        } else {
            if (User.getInstance().login(name, password, n)) {
                EMsg.println(EMsg.LOGIN_OK);
            } else {
                EMsg.println(EMsg.LOGIN_NO);
            }
        }
    }

    @Override
    public void out() {
        EMsg.println(EMsg.BYE);
        EMsg.println(EMsg.OUT);
        System.exit(1);
    }

    @Override
    public void put() {
        Map<String, String> infoMap = new HashMap<String, String>();
        String code;
        String input;
        EMsg.println(EMsg.IN_CODE);
        code = input();
        do {
            EMsg.println(EMsg.IN_K_V);
            input = input();
            if (input.matches(ERegexp.KV_RE.toString())) {
                String[] strs = input.split(ERegexp.SPACE_RE.toString());
                String key = strs[0];
                String value = strs[1];
                infoMap.put(key, value);
            } else if (input.equals("save")) {
                String info = EncryptUtils.encrypt(JSON.toJSONString(infoMap), User.getInstance().getPwd(), User.getInstance().getN());
                updateInfo(code, info);
                infoMap.clear();
                EMsg.println(EMsg.PUT_OK);
            } else if (input.equals("esc")) {
                infoMap.clear();
            } else {
                EMsg.println(EMsg.IN_F_ERROR);
            }
        } while (input.matches(ERegexp.KV_RE.toString()) || !input.matches(ERegexp.ESC_RE.toString()));
    }

    @Override
    public void remove() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        if (strs.length == 2) {
            removeInfo(strs[1]);
        } else {
            removeInfo(null);
        }
        EMsg.println(EMsg.REMOVE_OK);
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
            r = str[0] + ERegexp.SEMICOLON_RE.toString() + str[1] + ERegexp.SEMICOLON_RE.toString() + str[2] + ERegexp.SEMICOLON_RE.toString() +  EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        } else {
            JSONArray ja = new JSONArray();
            r = JSONObject.toJSONString(ja);
            r = str[0] + ERegexp.SEMICOLON_RE.toString() + str[1] + ERegexp.SEMICOLON_RE.toString() + str[2] + ERegexp.SEMICOLON_RE.toString() +  EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        }
        ReadmeToggle.write(r);
    }

    private void updateInfo(String code, String info) {
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
            r = str[0] + ERegexp.SEMICOLON_RE.toString() + str[1] + ERegexp.SEMICOLON_RE.toString()+ str[2] + ERegexp.SEMICOLON_RE.toString() + EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        } else {
            JSONArray ja = new JSONArray();
            JSONObject jo = new JSONObject();
            jo.put("code", code);
            jo.put("info", info);
            ja.add(jo);
            r = JSONObject.toJSONString(ja);
            r = str[0] + ERegexp.SEMICOLON_RE.toString() + str[1] + ERegexp.SEMICOLON_RE.toString() + str[2] + ERegexp.SEMICOLON_RE.toString() + EncryptUtils.encrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
        }
        ReadmeToggle.write(r);
    }

    @Override
    public void find() {
        String cmd = command.getInput();
        String[] strs = cmd.split(ERegexp.SPACE_RE.toString());
        if (strs.length == 2) {
            EMsg.println(findRecord(strs[1]));
        }
        EMsg.println(findRecord(null));
    }

    private String findRecord(String code) {
        String readme = ReadmeToggle.read();
        String[] str = readme.split(ERegexp.SEMICOLON_RE.toString());
        String result = "[]";
        if (str.length > 3) {
            String r = str[3];
            r = EncryptUtils.decrypt(r, User.getInstance().getPwd(), User.getInstance().getN());
            JSONArray ja = JSONObject.parseArray(r);
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (code == null || code.equals("")) {
                    jo.put("info", EncryptUtils.decrypt(jo.getString("info"), User.getInstance().getPwd(), User.getInstance().getN()));
                } else {
                    String c = jo.getString("code");
                    if (c.equals(code)) {
                        jo.put("info", EncryptUtils.decrypt(jo.getString("info"), User.getInstance().getPwd(), User.getInstance().getN()));
                        result = JSONObject.toJSONString(jo);
                        break;
                    }
                }
            }
            if (code == null || code.equals("")) {
                result = JSONObject.toJSONString(ja);
            }
        }
        return result;
    }

    @Override
    public void help() {
        return;
    }
}
