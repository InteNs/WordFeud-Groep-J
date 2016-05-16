package enumerations;

public enum FieldType {
    NORMAL,
    DW,
    TW,
    DL,
    TL,
    STARTTILE,
    UNDEFINED;

    public static FieldType parse(String fieldType) {
        switch (fieldType){
            case "--":
                return NORMAL;
            case "DW":
                return DW;
            case "TW":
                return TW;
            case "DL":
                return DL;
            case "TL":
                return TL;
            case "*":
                return STARTTILE;
            default:return UNDEFINED;
        }
    }



}
