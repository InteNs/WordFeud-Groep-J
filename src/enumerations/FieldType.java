package enumerations;

public enum FieldType {
    NORMAL,
    DW,
    TW,
    DL,
    TL,
    STARTTILE;

    public static FieldType parse(String fieldType) {
        switch (fieldType){
            case "--":
                return NORMAL;
            case "DW":
                return DW;
            case "TW":
                return DW;
            case "DL":
                return DW;
            case "TL":
                return DW;
            case "*":
                return DW;
        }
        return null;
    }



}
