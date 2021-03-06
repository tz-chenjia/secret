package cn.tz.chenjia.rule;

public enum ERegexp {

    CMD_HELP_RE("^(h|help)(\\s+\\S+)?$"), CMD_LOGINOUT_RE("^lo|loginout$"), CMD_OUT_RE("^o|out$"), CMD_CLEAR_RE("^c|clear$"), CMD_PUT_RE("^p|put$"), CMD_REMOVE_RE("^(r|remove)(\\s+\\S+)?$"), CMD_FIND_RE("^(f|find)(\\s+\\S+)?$"),CMD_EDIT_RE("^(e|edit)\\s+\\S+$"), SPACE_RE("\\s+"), CMD_FILEPATH_RE("^fp|filepath$"), CMD_FORMAT_RE("^format$"), CMD_PASSWORD_RE("^password\\s+\\S+\\s+\\d$"), CMD_BACKUPS_RE("^b|backups$"), EMAIL_RE("^[A-Za-z0-9_.-\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"), CMD_COVER_RE("^cover$");

    private String regexp;

    private ERegexp(String regexp) {
        this.regexp = regexp;
    }

    public String toString() {
        return regexp;
    }


}
