package cn.tz.chenjia.entity;

import cn.tz.chenjia.rule.ERegexp;
import cn.tz.chenjia.utils.ReadmeToggle;

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

    public boolean login(String name, String pwd, int n) {
        if (ReadmeToggle.exists()) {
            String readme = ReadmeToggle.read();
            String[] strs = readme.split(ERegexp.SEMICOLON_RE.toString());
            if (strs.length > 2) {
                user.setName(name);
                user.setPwd(pwd);
                user.setN(n);
                if (strs[0].equals(name) && strs[1].equals(pwd) && Integer.valueOf(strs[2]) == n) {
                    user.setOnline(true);
                    return true;
                } else {
                    user.setOnline(false);
                    return false;
                }
            } else {
                user.setOnline(false);
                return false;
            }
        } else {
            ReadmeToggle.write(name + ERegexp.SEMICOLON_RE.toString() + pwd + ERegexp.SEMICOLON_RE.toString()+ n + ERegexp.SEMICOLON_RE.toString());
            user.setName(name);
            user.setPwd(pwd);
            user.setN(n);
            user.setOnline(true);
            return true;
        }
    }

    public void out() {
        user.setOnline(false);
    }
}
