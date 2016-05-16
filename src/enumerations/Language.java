package enumerations;

public enum  Language {

    NL,
    EN,
    UNDEFINED;

    public static Language parse(String language) {
        switch (language){
            case "NL":
                return NL;
            case "EN":
                return EN;

            default:return UNDEFINED;
        }
    }
}
