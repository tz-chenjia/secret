package cn.tz.chenjia.entity;

import cn.tz.chenjia.utils.SecretRWUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

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
        DB_Secret secrets = SecretRWUtils.readSecretByTitle(userName, userName);
        if(secrets != null){
            String content = secrets.getContent();
            user.setName(userName);
            user.setPwd(password);
            user.setN(n);
            if (secrets.get.equals(userName) && joPassword.equals(password) && joN == n + 1) {
                user.setOnline(true);
                SecretRWUtils.write(userName, password);
                return true;
            } else {
                user.setOnline(false);
                return false;
            }
        }else{

        }


        JSONObject secretJO = null;
        secretJO = SecretRWUtils.readSecret(userName, password, 1);
        if (secretJO != null) {
            String joUserName = secretJO.getString("username");
            String joPassword = secretJO.getString("password");
            String joData = secretJO.getString("data");
            Integer joN = Integer.valueOf(secretJO.getString("n"));
            user.setName(userName);
            user.setPwd(password);
            user.setN(n);
            if (joUserName.equals(userName) && joPassword.equals(password) && joN == n + 1) {
                user.setOnline(true);
                SecretRWUtils.write(userName, password);
                return true;
            } else {
                user.setOnline(false);
                return false;
            }
        } else {
            return false;
        }
    }

    public void out() {
        user.setOnline(false);
    }
}
