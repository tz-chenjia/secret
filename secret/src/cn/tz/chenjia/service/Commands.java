package cn.tz.chenjia.service;

import cn.tz.chenjia.entity.User;
import cn.tz.chenjia.rule.ERegexp;

public enum Commands {
    LOGIN, LOGINOUT, OUT, PUT, REMOVE, FIND, CLEAR, FILEPATH, HELP;

    private String input;

    public static Commands toCmd(String input) {
        Commands cmd = resolveCmd(input);
        if (cmd != null) cmd.setInput(input);
        return cmd;
    }

    private static Commands resolveCmd(String input) {
        input = input.trim().toLowerCase();

        if (input.matches(ERegexp.CMD_HELP_RE.toString())) {
            return HELP;
        } else if (input.matches(ERegexp.CMD_LOGINOUT_RE.toString())) {
            return LOGINOUT;
        } else if (input.matches(ERegexp.CMD_LOGIN_RE.toString())) {
            return LOGIN;
        } else if (input.matches(ERegexp.CMD_OUT_RE.toString())) {
            return OUT;
        } else if (input.matches(ERegexp.CMD_PUT_RE.toString())) {
            return onlineCmd(PUT);
        } else if (input.matches(ERegexp.CMD_REMOVE_RE.toString())) {
            return onlineCmd(REMOVE);
        } else if (input.matches(ERegexp.CMD_FIND_RE.toString())) {
            return onlineCmd(FIND);
        }else if (input.matches(ERegexp.CMD_CLEAR_RE.toString())) {
            return CLEAR;
        }else if (input.matches(ERegexp.CMD_FILEPATH_RE.toString())) {
            return FILEPATH;
        }
        return null;
    }

    private static Commands onlineCmd(Commands command) {
        if (User.getInstance().isOnline()) {
            return command;
        } else {
            return null;
        }
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
