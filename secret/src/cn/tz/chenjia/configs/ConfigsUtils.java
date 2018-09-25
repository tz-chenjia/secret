package cn.tz.chenjia.configs;

import cn.tz.chenjia.utils.EncryptUtils;
import cn.tz.chenjia.utils.FileRWUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class ConfigsUtils {

    private static final String DB_PROPERTIES = "my.properties";

    public static Image getLogo() {
        Image logo = null;
        try {
            logo = ImageIO.read(ConfigsUtils.class.getResource("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logo;
    }

    public static File getSingleConf(String conf) {
        FileRWUtils.exists(getConfPath() + conf);
        return new File(getConfPath() + conf);
    }

    private static String getConfPath() {
        return getRootPath() + "conf" + File.separator;
    }

    public static String getRootPath() {
        return System.getProperty("user.dir") + File.separator;
    }

    public static Properties getDBProperties() {
        File dbPropFile = ConfigsUtils.getSingleConf(DB_PROPERTIES);
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(dbPropFile));
            properties.load(bufferedReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(properties.isEmpty()){
            return null;
        }else{
            properties.setProperty("password", EncryptUtils.decrypt(properties.getProperty("password"), ConfigsUtils.class.getName(), 1));
            return properties;
        }
    }

    public static void setDBProperties(String type, String name, String ip, String port, String driverClass, String userName, String password, String secretName) {

        try {

            File dbPropFile = ConfigsUtils.getSingleConf(DB_PROPERTIES);
            Properties p = new Properties();

            OutputStream outputFile = new FileOutputStream(dbPropFile);

            p.setProperty("type", type);
            p.setProperty("name", name);
            p.setProperty("ip", ip);
            p.setProperty("port", port);
            p.setProperty("driverClass", driverClass);
            p.setProperty("userName", userName);
            p.setProperty("password", EncryptUtils.encrypt(password, ConfigsUtils.class.getName(), 1));
            p.setProperty("secretName", secretName);

            p.store(outputFile, null);

            outputFile.close();

        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }
}
