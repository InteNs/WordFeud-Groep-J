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

}
