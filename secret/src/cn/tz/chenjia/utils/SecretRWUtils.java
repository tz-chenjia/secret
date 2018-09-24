package cn.tz.chenjia.utils;

import cn.tz.chenjia.configs.ConfigsUtils;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Properties;

public class SecretRWUtils {

    private static final String SECRET_FILE_NAME = "secret";

    public static boolean exists() {
        return FileRWUtils.exists(getSecretPath());
    }

    public static void write(String content) {
        FileRWUtils.write(new File(getSecretPath()), content);
    }

    public static String read() {
        return FileRWUtils.read(new File(getSecretPath()));
    }

    public static JSONObject readSecret(String password, int n) {
        String secret = read();
        JSONObject jo = null;
        if (!secret.equals("")) {
            String str = EncryptUtils.decrypt(secret, password, n);
            jo = JSONObject.parseObject(str);
        }
        String str = EncryptUtils.decrypt(secret, password, n);
        return jo;
    }

    public static String getSecretPath() {
        File confProp = ConfigsUtils.getSingleConf("conf.properties");
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = null;
        String secretPath = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(confProp));
            properties.load(bufferedReader);
            // 获取key对应的value值
            secretPath = properties.getProperty("SECRET-PATH");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (secretPath == null || secretPath.equals("")) {
            secretPath = System.getProperty("user.home") + File.separator + "Secret" + File.separator + SECRET_FILE_NAME;
        }
        System.out.println(secretPath);
        return secretPath;
    }
}
