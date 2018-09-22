package cn.tz.chenjia.rule;

public enum ERegexp {

    CMD_HELP_RE("^(h|help)(\\s+\\S+)?$"), CMD_LOGIN_RE("^(l|login)\\s+\\S+\\s+\\S+\\s+\\d$"), CMD_LOGINOUT_RE("^lo|loginout$"), CMD_OUT_RE("^o|out&"), CMD_CLEAR_RE("^c|clear&"),CMD_PUT_RE("^p|put$"), CMD_REMOVE_RE("^(r|remove)(\\s+\\S+)?$"), CMD_FIND_RE("^(f|find)(\\s+\\S+)?$"), SEMICOLON_RE("&&&&"), SPACE_RE("\\s+");

    private String regexp;

    private ERegexp(String regexp) {
        this.regexp = regexp;
    }

    public String toString() {
        return regexp;
    }


}
