package cn.tz.chenjia;

import cn.tz.chenjia.rule.EMsg;
import cn.tz.chenjia.service.CmdSevrice;
import cn.tz.chenjia.service.Commands;

public class Entry {

    public static void main(String[] args) {
        System.out.println(EMsg.HELLO);
        while(true){
            CmdSevrice.runCmd(Commands.toCmd(CmdSevrice.input()));
        }
    }

}
