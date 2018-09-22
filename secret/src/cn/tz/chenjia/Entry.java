package cn.tz.chenjia;

import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.ui.LoginForm;

public class Entry {

    public static void main(String[] args) {
        System.out.println(EMsg.HELLO);
        new LoginForm();
    }

}
