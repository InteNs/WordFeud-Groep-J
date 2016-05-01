package enumerations;

public enum  Language {

    NL,
    EN,
    UNDEFINED;

    public static Language languageFor(String language){
        switch (language){
            case "NL":
                return NL;
            case "EN":
                return EN;

            default:return UNDEFINED;
        }
    }
}
