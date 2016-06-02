package enumerations;

public enum ReactionType {
    UNDEFINED,
    UNKNOWN,
    ACCEPTED,
    REJECTED;

    public static ReactionType parse(String reactionType) {
        switch (reactionType) {
            case "unknown": return UNKNOWN;
            case "accepted" : return ACCEPTED;
            case "rejected" : return REJECTED;
            default: return UNDEFINED;
        }
    }

    public static String format(ReactionType reactionType) {
        switch (reactionType) {
            default:
                return "";
            case UNDEFINED:
                return "undefined";
            case ACCEPTED:
                return "accepted";
            case REJECTED:
                return "rejected";
            case UNKNOWN:
                return "unknown";
        }
    }
}
