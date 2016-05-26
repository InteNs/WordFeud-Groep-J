package enumerations;

public enum WordStatus {
    ACCEPTED,
    DENIED,
    PENDING,
    UNDEFINED;

    public static WordStatus parse(String WordStatus) {
        switch (WordStatus) {
            case "accepted":
                return ACCEPTED;
            case "denied":
                return DENIED;
            case "peding":
                return PENDING;
            default:
                return UNDEFINED;
        }
    }

    public static String format(WordStatus WordStatus) {
        switch (WordStatus) {
            case ACCEPTED:
                return "geaccepteerd";
            case DENIED:
                return "afgewezen";
            case PENDING:
                return "in afwachting";

            default:
                return "";
        }
    }
}

