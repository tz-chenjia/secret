package cn.tz.chenjia.rule;

public enum EMsg {
    HELLO("欢迎来到这里"), INPUT("主公请指示："), INVALID("命令无效，通过命令help或h查看命令说明，请重新输入"), TIPS_LOGIN("未登录"), LOGIN_OK("登录成功"), LOGIN_NO("登录失败"), LOGIN_OUT("你已退出登录"), ONLINE("你已经在线"),
    OUT("退出成功"), BYE("再见"), IN_CODE("请先输入code，方便以后查找、修改、删除"), IN_K_V("请输入key value,退出并保存输入save，退出不保存输入esc"), IN_F_ERROR("输入格式有误，请重新输入"), PUT_OK("添加成功"), UPDATE_OK("更新成功"), REMOVE_OK("删除成功");

    private String msg;

    private EMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return msg;
    }

    public static void print(Object msg) {
        System.out.print(msg);
    }

    public static void println(Object msg) {
        System.out.println(msg);
    }

}
