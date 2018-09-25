package cn.tz.chenjia.configs;

import cn.tz.chenjia.utils.EncryptUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class ConfigsUtils {

    public static Image getLogo(){
        Image logo= null;
        try {
            logo = ImageIO.read(getSingleConf("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logo;
    }

    public static File getSingleConf(String conf){
        System.out.println(getConfPath() + conf);
        File file = new File(getConfPath() + conf);
        if(file.exists()){
            return  file;
        }else{
            throw new RuntimeException("配置文件丢失");
        }
    }

    private static String getConfPath(){
        return getRootPath() + "conf" + File.separator;
    }

    public static String getRootPath(){
        return System.getProperty("user.dir") + File.separator;
    }

    public static Properties getDBProperties(){
        File dbPropFile = ConfigsUtils.getSingleConf("DB.properties");
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
        properties.setProperty("password",EncryptUtils.decrypt(properties.getProperty("password"), ConfigsUtils.class.getName(), 1));
        return  properties;
    }

    public static void setDBProperties(String type, String name, String ip , String port, String driverClass, String userName, String password, String secretName) {

        try{

            File dbPropFile = ConfigsUtils.getSingleConf("DB.properties");
            Properties p = new Properties();

            OutputStream outputFile = new FileOutputStream(dbPropFile); //true表示追加（写入时 有重复key的 就覆盖原来的 没有就创建一个key-value）

            p.setProperty("type",type);
            p.setProperty("name",name);
            p.setProperty("ip",ip);
            p.setProperty("port",port);
            p.setProperty("driverClass",driverClass);
            p.setProperty("userName",userName);
            p.setProperty("password", EncryptUtils.encrypt(password, ConfigsUtils.class.getName(), 1));
            p.setProperty("secretName", secretName);

            p.store(outputFile,null);

            outputFile.close();

        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

}
