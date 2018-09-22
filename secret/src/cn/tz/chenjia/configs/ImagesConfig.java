package cn.tz.chenjia.configs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ImagesConfig {

    public static Image getLogo(){
        Image logo= null;
        try {
            logo = ImageIO.read(ImagesConfig.class.getResource("../resource/logo.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logo;
    }

}
