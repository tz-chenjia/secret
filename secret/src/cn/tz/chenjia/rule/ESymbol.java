package cn.tz.chenjia.rule;

public enum ESymbol {
    BORDER(""), BORDER2("");


    private String symbol;

    private ESymbol(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return symbol;
    }
}
