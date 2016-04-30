package enumerations;

/**
 * Created by Ben on 30-4-2016.
 */
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
