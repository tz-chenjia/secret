package cn.tz.chenjia.rule;

import cn.tz.chenjia.utils.SecretRWUtils;

public enum EMsg {

    HELLO("Welcome to here! For help, please enter 'HELP' or 'H'!"), INVALID("命令无效，通过命令help或h查看命令说明，请重新输入"), LOGIN_OK("登录成功"), LOGIN_NO("登录失败"), LOGIN_OUT("你已退出登录"), ONLINE("你已经在线"), OUT("退出成功"), WELCOME("，欢迎您使用 Secret，如有问题请联系：tz_chenjia@qq.com！ "), ERROR_KV("标题内容不能为空"), ERROR_KV2("标题不能为你的邮箱"), ERROR_KV3("标题不能为系统命令"), ERROR_KV4("标题已存在，请换个标题或去修改"), ERROR_KV5("标题不能包含空格"), INFO_NOT("没找到存储信息"), SAVE_OK("保存成功"), REMOVE_OK("删除成功"), REMOVE_FAIL("删除失败，未找到数据"), FORMAT_OK("格式化成功"), FORMAT_FAIL("格式化失败"), HELP_TIPS(ESymbol.BORDER2 + "Tips: 输入HELP *** 或 H ***查看详情\n" + ESymbol.BORDER2), PASSWORD_OK("密码及幸运数重置成功"), BACKUPS_OK("数据已备份成功，文件保存路径" + SecretRWUtils.getExportSQLFile()), BACKUPS_EMAIL_OK("\n备份文件已发至你邮箱，注意查收！"), BACKUPS_EMAIL_FAIL("\n发送邮箱失败！"), OUT_OK("true"), OUT_CANCEL("false"), IMP_TIPS("覆盖数据之前，请确保备份！"), IMP_FAILS("覆盖失败，Secret文件出错"), IMP_OK("覆盖成功，准备重启");

    private String msg;

    private EMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return msg;
    }

    public static void println(Object msg) {
        System.out.println(msg);
    }

}
