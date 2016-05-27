package enumerations;

public enum TurnType {
    BEGIN,
    END,
    PASS,
    RESIGN,
    SWAP,
    WORD,
    UNDEFINED;

    public static TurnType getFor(String turnType) {
        switch (turnType) {
            case "begin": return BEGIN;
            case "end": return END;
            case "pass": return PASS;
            case "resign": return RESIGN;
            case "swap": return SWAP;
            case "word": return WORD;
            default: return UNDEFINED;
        }
    }
    public static String format(TurnType turnType) {
        switch (turnType) {
            case BEGIN:
                return "begin";
            case END:
                return "end";
            case PASS:
                return "pass";
            case RESIGN:
                return "resign";
            case SWAP:
                return "swap";
            case WORD:
                return "word";
            default:
              return "";
        }
    }
}
