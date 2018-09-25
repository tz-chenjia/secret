package cn.tz.chenjia.entity;

import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.utils.SecretRWUtils;
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
        DB_Secret secrets = SecretRWUtils.readSecretByTitle(userName, userName, null, null);
        JSONObject jo = new JSONObject();
        jo.put("pwd", password);
        jo.put("n", n);
        String contentSecret = EncryptUtils.encrypt(JSONObject.toJSONString(jo), password, n);
        if(secrets != null){
            String contentDBSecret = secrets.getContent();
            if (contentSecret.equals(contentDBSecret)) {
                user.setName(userName);
                user.setPwd(password);
                user.setN(n);
                user.setOnline(true);
                return true;
            } else {
                user.setOnline(false);
                return false;
            }
        }else{
            user.setName(userName);
            user.setPwd(password);
            user.setN(n);
            user.setOnline(true);
            SecretRWUtils.write(userName, contentSecret, userName, null, null);
            return true;
        }
    }

    public void out() {
        user.setOnline(false);
    }
}
