package cn.tz.chenjia.db;

import cn.tz.chenjia.entity.DB_Secret;

import java.util.List;

public class SecretDAO extends BaseDao {
    public static final String SECRET_TABLE_NAME = "secret";

    public SecretDAO() {

    }

    // 删除
    public void delete(String userName, String title) {
        String sql = "delete from " + SECRET_TABLE_NAME + " where userName=? and title=?";
        Object[] paramsValue = {userName, title};
        super.update(sql, paramsValue);
    }

    // 删除
    public void deleteAllByUser(String userName) {
        String sql = "delete from " + SECRET_TABLE_NAME + " where userName=? and title<>?";
        Object[] paramsValue = {userName, userName};
        super.update(sql, paramsValue);
    }

    // 删除
    public void deleteUser(String userName) {
        String sql = "delete from " + SECRET_TABLE_NAME + " where userName=? ";
        Object[] paramsValue = {userName};
        super.update(sql, paramsValue);
    }

    // 插入
    public void insert(DB_Secret DB_Secret) {
        String sql = "insert into " + SECRET_TABLE_NAME + " (username,title, content) values (?,?,?)";
        Object[] paramsValue = {DB_Secret.getUsername(), DB_Secret.getTitle(), DB_Secret.getContent()};
        super.update(sql, paramsValue);
    }

    // 插入
    public void update(DB_Secret DB_Secret) {
        String sql = "update " + SECRET_TABLE_NAME + " set content=? where username = ? and title=?";
        Object[] paramsValue = {DB_Secret.getContent(), DB_Secret.getUsername(), DB_Secret.getTitle()};
        super.update(sql, paramsValue);
    }

    public List<DB_Secret> findByName(String userName) {
        String sql = "select * from " + SECRET_TABLE_NAME + " where username=?";
        List<DB_Secret> list = super.query(sql, new Object[]{userName}, DB_Secret.class);
        return list;
    }

    public DB_Secret findByNameAndTitle(String userName, String title) {
        String sql = "select * from " + SECRET_TABLE_NAME + " where username=? and title=?";
        List<DB_Secret> list = super.query(sql, new Object[]{userName, title}, DB_Secret.class);
        return list.size() > 0 ? list.get(0) : null;
    }

}
