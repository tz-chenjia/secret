package cn.tz.chenjia.configs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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

}
