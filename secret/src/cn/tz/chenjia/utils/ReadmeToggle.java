package cn.tz.chenjia.utils;

import cn.tz.chenjia.configs.ConfigsUtils;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

public class ReadmeToggle {

    private static final String SECRET_FILE_NAME = "secret";

    public static boolean exists() {
        return FileRWUtils.exists(getSecret());
    }

    public static void write(String content) {
        FileRWUtils.write(new File(getSecret()), content);
    }

    public static String read() {
        return FileRWUtils.read(new File(getSecret()));
    }

    public static JSONObject readSecret(String password, int n){
        String secret = read();
        JSONObject jo = null;
        if(!secret.equals("")){
            String str = EncryptUtils.decrypt(secret, password, n);
            jo = JSONObject.parseObject(str);
        }
        String str = EncryptUtils.decrypt(secret, password, n);
        return jo;
    }

    private static String getSecret() {
        return ConfigsUtils.getRootPath() + SECRET_FILE_NAME;
    }
}
