package cn.tz.chenjia.db;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.rule.EDBType;

import java.sql.*;
import java.util.Properties;

public class JDBCUtils {

    // 驱动包名和数据库url
    private static String url = null;
    private static String driverClass = null;
    // 数据库用户名和密码
    private static String userName = null;
    private static String password = null;

    public static boolean isUseDB(String ip, String port, String name, String userName, String pwd, EDBType dbType, String secretName) {
        String url = buildDBUrl(EDBType.toString(dbType), ip, port, name);
        if (JDBCUtils.testConnection(url, dbType.getDriverClass(), userName, pwd) == null) {
            System.out.println("数据库不能用");
            return false;
        } else {
            System.out.println("数据库能用");
            ConfigsUtils.setDBProperties(EDBType.toString(dbType), name, ip, port, dbType.getDriverClass(), userName, pwd, secretName);
            return true;
        }
    }

    private static String buildDBUrl(String type, String ip, String port, String name) {
        String url;
        switch (type) {
            case "db2":
                url = "jdbc:db2://" + ip + "[:" + port + "]/" + name;
                break;
            case "sqlserver":
                url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + name;
                break;
            case "oracle":
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + name;
                break;
            default:
                //mysql
                url = "jdbc:mysql://" + ip + ":" + port + "/" + name + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
                break;
        }
        return url;
    }

    public static Connection testConnection(String url, String driverClass, String userName, String password) {
        Connection conn = null;
        try {
            //注册驱动程序
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 打开数据库驱动连接
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Properties prop = ConfigsUtils.getDBProperties();
            if(prop != null){
                driverClass = prop.getProperty("driverClass");
                userName = prop.getProperty("userName");
                password = prop.getProperty("password");
                String type = prop.getProperty("type");
                String ip = prop.getProperty("ip");
                String port = prop.getProperty("port");
                String name = prop.getProperty("name");
                url = buildDBUrl(type, ip, port, name);
            }

            //注册驱动程序
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 清理环境，关闭连接(顺序:后打开的先关闭)
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new RuntimeException(e1);
            }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
