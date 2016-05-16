package enumerations;

public enum Role {
    PLAYER,
    ADMINISTRATOR,
    MODERATOR,
    OBSERVER,
    UNDEFINED;

    public static Role parse(String role) {
        switch (role) {
            case "player": return PLAYER;
            case "administrator": return ADMINISTRATOR;
            case "moderator": return MODERATOR;
            case "observer": return OBSERVER;
            default: return UNDEFINED;
        }
    }

    public static String format(Role role) {
        switch (role) {
            case PLAYER:        return "player";
            case ADMINISTRATOR: return "administrator";
            case MODERATOR:     return "moderator";
            case OBSERVER:      return "observer";
            default: return "";
        }
    }
}

