package cn.tz.chenjia.db;

import cn.tz.chenjia.entity.DB_Secret;

import java.util.*;

public class SecretDAO extends BaseDao {
    public static final String SECRET_TABLE_NAME = "secret";

    public SecretDAO() {

    }

    // 删除一条记录
    public int delete(String userName, String title) {
        String sql = "delete from " + SECRET_TABLE_NAME + " where userName=? and title=?";
        Object[] paramsValue = {userName, title};
        return super.update(sql, paramsValue);
    }

    // 删除该用户所有记录
    public int deleteAllByUser(String userName) {
        String sql = "delete from " + SECRET_TABLE_NAME + " where userName=? and title<>?";
        Object[] paramsValue = {userName, userName};
        return super.update(sql, paramsValue);
    }

    // 删除用户及记录
    public int deleteUser(String userName) {
        String sql = "delete from " + SECRET_TABLE_NAME + " where userName=? ";
        Object[] paramsValue = {userName};
        return super.update(sql, paramsValue);
    }

    // 插入
    public void insert(DB_Secret DB_Secret) {
        List<String> section = section(DB_Secret.getContent(), 1000);
        for(int i=0; i<section.size(); i++){
            String sql = "insert into " + SECRET_TABLE_NAME + " (username,title, content, sectionno) values (?,?,?,?)";
            Object[] paramsValue = {DB_Secret.getUsername(), DB_Secret.getTitle(), section.get(i), i};
            super.update(sql, paramsValue);
        }
    }

    // 更新
    public void update(DB_Secret DB_Secret) {
        delete(DB_Secret.getUsername(), DB_Secret.getTitle());
        insert(DB_Secret);
    }

    public List<DB_Secret> findExportSQL(String userName) {
        String sql = "select * from " + SECRET_TABLE_NAME + " where username=?";
        List<DB_Secret> list = super.query(sql, new Object[]{userName}, DB_Secret.class);
        return list;
    }

    public List<DB_Secret> findByName(String userName) {
        String sql = "select * from " + SECRET_TABLE_NAME + " where username=?";
        List<DB_Secret> list = super.query(sql, new Object[]{userName}, DB_Secret.class);
        Map<String, List<DB_Secret>> map = new HashMap<String, List<DB_Secret>>();
        for(DB_Secret s : list){
            String title = s.getTitle();
            List<DB_Secret> sections = map.get(title) == null ? new ArrayList<DB_Secret>() : map.get(title);
            sections.add(s);
            map.put(title, sections);
        }
        list.clear();
        Set<Map.Entry<String, List<DB_Secret>>> entries = map.entrySet();
        for(Map.Entry<String, List<DB_Secret>> e : entries){
            List<DB_Secret> value = e.getValue();
            DB_Secret secret = null;
            if(value.size() > 0){
                Map<Integer, String> sections = new HashMap<Integer, String>();
                for(DB_Secret s : value){
                    String content = s.getContent();
                    int sectionno = s.getSectionno();
                    sections.put(sectionno, content);
                }
                String merge = merge(sections);
                secret = value.get(0);
                secret.setContent(merge);
                list.add(secret);
            }
        }
        return list;
    }

    public DB_Secret findByNameAndTitle(String userName, String title) {
        DB_Secret secret = null;
        String sql = "select * from " + SECRET_TABLE_NAME + " where username=? and title=?";
        List<DB_Secret> list = super.query(sql, new Object[]{userName, title}, DB_Secret.class);
        if(list.size() > 0){
            Map<Integer, String> sections = new HashMap<Integer, String>();
            for(DB_Secret s : list){
                String content = s.getContent();
                int sectionno = s.getSectionno();
                sections.put(sectionno, content);
            }
            String merge = merge(sections);
            secret = list.get(0);
            secret.setContent(merge);
        }
        return secret;
    }

    private static List<String> section(String content, int len){
        List<String> strList = new ArrayList<String>();
        if(content != null && !content.equals("")){
            do{
                len = len > content.length() ? content.length() : len;
                String section = content.substring(0, len);
                strList.add(section);
            }while ((content=content.substring(len)).length() > 0);
        }
        return strList;
    }

    private static String merge(Map<Integer, String> sections){
        StringBuffer sb = new StringBuffer();
        if(sections != null || !sections.isEmpty()){
            TreeMap<Integer, String> treeMap = new TreeMap<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if (o1 > 02) {
                        return -1;
                    }
                    return 1;
                }
            });
            treeMap.putAll(sections);
            for(Map.Entry<Integer, String> e : sections.entrySet()){
                sb.append(e.getValue());
            }
        }
        return sb.toString();
    }

}
