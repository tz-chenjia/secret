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

    public static String[] readUserTitles(String userName, String pwd, int n, String allName){
        List<String> titles = secretDAO.findTitles(userName);
        String [] titleArr = null;
        if(allName != null){
            titleArr = new String[titles.size() + 1];
            titleArr[0] = allName;
            for(int i = 0; i<titles.size(); i++){
                titleArr[i+1] = EncryptUtils.decrypt(titles.get(i), pwd, n);
            }
        }else {
            titleArr = new String[titles.size()];
            for(int i = 0; i<titles.size(); i++){
                titleArr[i] = EncryptUtils.decrypt(titles.get(i), pwd, n);
            }
        }

        return titleArr;
    }

    public static List<DB_Secret> readSecret(String userName, String pwd, int n) {
        return secretDAO.findByName(userName);
    }

    public static List<DB_Secret> readExportSQL(String userName, String pwd, int n) {
        return secretDAO.findExportSQL(userName);
    }

    public static DB_Secret readSecretByTitle(String title, String userName, String pwd, Integer n) {
        title = title.equals(userName) ? title : EncryptUtils.encrypt(title, pwd, n);
        DB_Secret secret = secretDAO.findByNameAndTitle(userName, title);
        return secret;
    }

    public static int remove(String title, String userName, String pwd, int n) {
        title = EncryptUtils.encrypt(title, pwd, n);
        return secretDAO.delete(userName, title);
    }

    public static void removeAll(String userName) {
        secretDAO.deleteAllByUser(userName);
    }

    public static void removeUser(String userName) {
        secretDAO.deleteUser(userName);
    }

    public static void exportSQL(String userName, String pwd, int n) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from " + SecretDAO.SECRET_TABLE_NAME + " where username = '"+userName+"';\n");
        List<DB_Secret> db_secrets = readExportSQL(userName, pwd, n);
        for (DB_Secret secret : db_secrets) {
            sb.append("insert into " + SecretDAO.SECRET_TABLE_NAME +
                    " (username,title, content, sectionno) values ('" + secret.getUsername() + "','" + secret.getTitle() + "','" + secret.getContent() + "','" + secret.getSectionno() + "');\n");
        }
        FileRWUtils.write(getExportSQLFile(), sb.toString());
    }

    public static boolean importSQL(File file){
        boolean r;
        if (file != null) {
            String sqlStr = FileRWUtils.read(file);
            String[] sqls = sqlStr.split(";");
            if(simpleCheckSql(sqls)){
                for(String sql : sqls){
                    secretDAO.update(sql, new Object[]{});
                }
                r = true;
            }else {
                r = false;
            }
        }else {
            r = false;
        }
        return r;
    }

    private static boolean simpleCheckSql(String[] sqls) {
        boolean r = true;
        if (sqls.length > 0) {
            for (String sql : sqls) {
                if (!sql.contains(SecretDAO.SECRET_TABLE_NAME)) {
                    r = false;
                    break;
                }
            }
        }else {
            r = false;
        }
        return r;
    }

    public static File getExportSQLFile() {
        File f = new File(System.getProperty("user.dir") + File.separator + "secret.sql");
        return f;
    }

}
