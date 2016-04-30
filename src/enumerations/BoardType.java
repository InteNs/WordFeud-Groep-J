package enumerations;

/**
 * Created by Ben on 30-4-2016.
 */
public enum BoardType {
    STANDARD,
    RANDOM,
    UNDEFINED;

    public static BoardType boardTypeFor(String boardType){
        switch (boardType){
            case "standard":
                return STANDARD;
            case "random":
                return RANDOM;

            default: return UNDEFINED;
        }
    }

}
