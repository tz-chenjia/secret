package cn.tz.chenjia.rule;

public enum EMsg {

    HELLO("*********************************************************************************************************\n" +
            "*********************************************************************************************************\n" +
            "*************************                            WELCOME TO HERE !                           *************************\n" +
            "*************************                                                                                            *************************\n" +
            "*************************               For help, please enter 'HELP' or 'H' !               *************************\n" +
            "*********************************************************************************************************\n" +
            "*********************************************************************************************************"), INVALID("命令无效，通过命令help或h查看命令说明，请重新输入"), LOGIN_OK("登录成功"), LOGIN_NO("登录失败"), LOGIN_OUT("你已退出登录"), ONLINE("你已经在线"), OUT("退出成功"), WELCOME("，欢迎您使用 Secret，如有问题请联系：tz_chenjia@qq.com！ "), ERROR_KV("标题内容不能为空"), INFO_NOT("没找到存储信息"), SAVE_OK("保存成功"), REMOVE_OK("删除成功"), FORMAT_OK("格式化成功"), HELP_TIPS(ESymbol.BORDER2 + "\nTips: 输入HELP *** 或 H ***查看详情\n" + ESymbol.BORDER2);

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
