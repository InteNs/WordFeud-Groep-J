package enumerations;

/**
 * Created by Ben on 30-4-2016.
 */
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
