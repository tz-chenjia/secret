package cn.tz.chenjia.entity;

import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.utils.ReadmeToggle;
import com.alibaba.fastjson.JSONObject;

public class User {

    private volatile static User user;

    private User() {

    }

    public static User getInstance() {
        if (user == null) {
            synchronized (User.class) {
                if (user == null) {
                    user = new User();
                }
            }
        }
        return user;
    }

    private String name;

    private String pwd;

    private int n;

    private boolean online;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean checkLogin() {
        return user.isOnline();
    }

    public boolean login(String userName, String password, int n) {
        JSONObject secretJO = null;
        if (ReadmeToggle.exists()) {
            secretJO = ReadmeToggle.readSecret(password, 1);
            if (secretJO != null) {
                String joUserName = secretJO.getString("userName");
                String joPassword = secretJO.getString("password");
                Integer joN = Integer.valueOf(secretJO.getString("n"));
                user.setName(userName);
                user.setPwd(password);
                user.setN(n);
                if (joUserName.equals(userName) && joPassword.equals(password) && joN == n) {
                    user.setOnline(true);
                    return true;
                } else {
                    user.setOnline(false);
                    return false;
                }
            }
        }
        secretJO = new JSONObject();
        secretJO.put("userName", userName);
        secretJO.put("password", password);
        secretJO.put("n", n);
        secretJO.put("data", "");
        ReadmeToggle.write(EncryptUtils.encrypt(JSONObject.toJSONString(secretJO), password, n));
        user.setName(userName);
        user.setPwd(password);
        user.setN(n);
        user.setOnline(true);
        return true;
    }

    public void out() {
        user.setOnline(false);
    }
}
