package enumerations;

public enum FieldType {
    NORMAL,
    DW,
    TW,
    DL,
    TL,
    STARTTILE;

    public static FieldType fieldTypeFor(String fieldType){
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
        }
        return null;
    }



}
