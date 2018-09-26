package cn.tz.chenjia.db;

import cn.tz.chenjia.rule.EDBType;
import cn.tz.chenjia.utils.ExceptionHandleUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {

    private static final Logger log = Logger.getLogger(
            BaseDao.class
    );

    // 初始化参数
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public final boolean tableExists(String tableName) {
        log.info("检查secret表是否存在");
        boolean flag = false;
        try {
            con = JDBCUtils.getConnection();
            DatabaseMetaData meta = con.getMetaData();
            //String type[] = {"TABLE"};
            rs = meta.getTables(null, null, tableName.toUpperCase(), null);
            flag = rs.next();
            if(flag){
                log.info("secret表已存在");
            }else {
                log.info("secret表不存在，准备创建");
            }
        } catch (SQLException e) {
            ExceptionHandleUtils.handling(e);
        }
        return flag;
    }

    public void createTable(EDBType type, String tableName) {
        log.info("开始创建secret表");
        String sql = "CREATE TABLE " + tableName + " (username varchar(1000) NOT NULL,title varchar(1000) NOT NULL,content varchar(1500) NOT NULL,sectionno int(11) NOT NULL)";
        switch (type) {
            case DB2:
                break;
            case SQLSERVER:
                break;
            case ORACLE:
                sql = "CREATE TABLE " + tableName + " (username varchar(1000) NOT NULL,title varchar(1000) NOT NULL,content varchar(1500) NOT NULL,sectionno NUMBER(11) NOT NULL)";
                break;
            default:
                //mysql
                break;
        }
        int update = update(sql, new Object[]{});
        if(update > 0){
            log.info("secret表创建成功");
        }else {
            log.warn("secret表创建失败");
        }
    }


    /**
     * 查询的通用方法
     *
     * @param sql
     * @param paramsValue
     */
    public <T> List<T> query(String sql, Object[] paramsValue, Class<T> clazz) {

        try {
            // 返回的集合
            List<T> list = new ArrayList<T>();
            // 对象
            T t = null;

            // 1. 获取连接
            con = JDBCUtils.getConnection();
            // 2. 创建stmt对象
            pstmt = con.prepareStatement(sql);
            // 3. 获取占位符参数的个数， 并设置每个参数的值
            int count = pstmt.getParameterMetaData().getParameterCount();
            if (paramsValue != null && paramsValue.length > 0) {
                for (int i = 0; i < paramsValue.length; i++) {
                    pstmt.setObject(i + 1, paramsValue[i]);
                }
            }
            // 4. 执行查询
            rs = pstmt.executeQuery();
            // 5. 获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // ---> 获取列的个数
            int columnCount = rsmd.getColumnCount();

            // 6. 遍历rs
            while (rs.next()) {
                // 要封装的对象
                t = clazz.newInstance();

                // 7. 遍历每一行的每一列, 封装数据
                for (int i = 0; i < columnCount; i++) {
                    // 获取每一列的列名称
                    String columnName = rsmd.getColumnName(i + 1).toLowerCase();
                    // 获取每一列的列名称, 对应的值
                    Object value = rs.getObject(columnName);
                    // 封装： 设置到t对象的属性中  【BeanUtils组件】
                    BeanUtils.copyProperty(t, columnName, value);
                }

                // 把封装完毕的对象，添加到list集合中
                list.add(t);
            }

            return list;
        } catch (Exception e) {
            ExceptionHandleUtils.handling(e);
            return null;
        } finally {
            JDBCUtils.close(con, pstmt, rs);
        }
    }

    /**
     * 更新的通用方法
     *
     * @param sql         更新的sql语句(update/insert/delete)
     * @param paramsValue sql语句中占位符对应的值(如果没有占位符，传入null)
     */
    public int update(String sql, Object[] paramsValue) {

        try {
            // 获取连接
            con = JDBCUtils.getConnection();
            // 创建执行命令的stmt对象
            pstmt = con.prepareStatement(sql);
            // 参数元数据： 得到占位符参数的个数
            int count = pstmt.getParameterMetaData().getParameterCount();

            // 设置占位符参数的值
            if (paramsValue != null && paramsValue.length > 0) {
                // 循环给参数赋值
                for (int i = 0; i < count; i++) {
                    pstmt.setObject(i + 1, paramsValue[i]);
                }
            }
            // 执行更新
            return pstmt.executeUpdate();

        } catch (Exception e) {
            ExceptionHandleUtils.handling(e);
        } finally {
            JDBCUtils.close(con, pstmt, null);
        }
        return -1;
    }
}
