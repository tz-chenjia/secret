package cn.tz.chenjia.utils;

import cn.tz.chenjia.db.SecretDAO;
import cn.tz.chenjia.entity.DB_Secret;

import java.io.File;
import java.util.List;

public class SecretRWUtils {

    private static SecretDAO secretDAO = new SecretDAO();
    ;

    public static void write(String title, String content, String userName, String pwd, Integer n) {
        title = title.equals(userName) ? title : EncryptUtils.encrypt(title, pwd, n);
        content = title.equals(userName) ? content : EncryptUtils.encrypt(content, pwd, n);
        DB_Secret userSecret = secretDAO.findByNameAndTitle(userName, title);
        if (userSecret == null) {
            userSecret = new DB_Secret();
            userSecret.setUsername(userName);
            userSecret.setTitle(title);
            userSecret.setContent(content);
            secretDAO.insert(userSecret);
        } else {
            userSecret.setContent(content);
            secretDAO.update(userSecret);
        }
    }

    public static List<DB_Secret> readSecret(String userName, String pwd, int n) {
        return secretDAO.findByName(userName);
    }

    public static DB_Secret readSecretByTitle(String title, String userName, String pwd, Integer n) {
        title = title.equals(userName) ? title : EncryptUtils.encrypt(title, pwd, n);
        DB_Secret secret = secretDAO.findByNameAndTitle(userName, title);
        return secret;
    }

    public static void remove(String title, String userName, String pwd, int n) {
        title = EncryptUtils.encrypt(title, pwd, n);
        secretDAO.delete(userName, title);
    }

    public static void removeAll(String userName) {
        secretDAO.deleteAllByUser(userName);
    }

    public static void removeUser(String userName) {
        secretDAO.deleteUser(userName);
    }

    public static void exportSQL(String userName, String pwd, int n) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from " + SecretDAO.SECRET_TABLE_NAME +";");
        List<DB_Secret> db_secrets = readSecret(userName, pwd, n);
        for (DB_Secret secret : db_secrets) {
            sb.append("insert into " + SecretDAO.SECRET_TABLE_NAME +
                    " (username,title, content) values ('" + secret.getUsername() + "','" + secret.getTitle() + "','" + secret.getContent() + "');");
        }

        FileRWUtils.write(getExportSQLFile(), sb.toString());
    }

    public static File getExportSQLFile(){
        File f = new File(System.getProperty("user.dir") + File.separator + "secret.sql");
        return f;
    }

}
