package enumerations;

public enum BoardType {
    STANDARD,
    RANDOM,
    UNDEFINED;

    public static BoardType parse(String boardType) {
        switch (boardType){
            case "standard":
                return STANDARD;
            case "random":
                return RANDOM;

            default: return UNDEFINED;
        }
    }
    public static String format(BoardType boardType) {
        switch (boardType) {
            case STANDARD:
               return "standard";
            case RANDOM:
                return "random";
            default: return "";
        }
    }

}
