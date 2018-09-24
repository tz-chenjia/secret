package cn.tz.chenjia.utils;

import cn.tz.chenjia.db.SecretDAO;
import cn.tz.chenjia.entity.DB_Secret;
import cn.tz.chenjia.entity.User;

import java.util.List;

public class SecretRWUtils {

    private static SecretDAO secretDAO = new SecretDAO();
    ;

    public static void write(String title, String content) {
        DB_Secret userSecret = secretDAO.findByNameAndTitle(User.getInstance().getName(), title);
        if (userSecret == null) {
            userSecret = new DB_Secret();
            userSecret.setUsername(User.getInstance().getName());
            userSecret.setTitle(EncryptUtils.encrypt(title, User.getInstance().getPwd(), User.getInstance().getN()));
            userSecret.setContent(EncryptUtils.encrypt(content, User.getInstance().getPwd(), User.getInstance().getN()));
            secretDAO.insert(userSecret);
        } else {
            userSecret.setContent(EncryptUtils.encrypt(content, User.getInstance().getPwd(), User.getInstance().getN()));
            secretDAO.update(userSecret);
        }
    }

    public static List<DB_Secret> readSecret(String userName) {
        return  secretDAO.findByName(userName);
    }

    public static DB_Secret readSecretByTitle(String userName, String title) {
        return  secretDAO.findByNameAndTitle(userName, title);
    }
}
