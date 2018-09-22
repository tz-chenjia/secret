package cn.tz.chenjia.rule;

public enum ESymbol {

    SEMICOLON(";"),BORDER("------------------------------------------------  Secret ------------------------------------------------");

    private String symbol;

    private ESymbol(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return symbol;
    }
}
