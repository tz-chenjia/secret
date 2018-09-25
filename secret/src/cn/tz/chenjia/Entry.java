package cn.tz.chenjia;

import cn.tz.chenjia.ui.LoginForm;

import javax.swing.*;

public class Entry {

    public static void main(String[] args) throws Exception {
        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        UIManager.put("RootPane.setupButtonVisible", false);
        new LoginForm();
    }

}
