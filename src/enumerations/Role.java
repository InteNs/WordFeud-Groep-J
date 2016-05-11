package enumerations;

public enum Role {
    PLAYER,
    ADMINISTRATOR,
    MODERATOR,
    OBSERVER,
    UNDEFINED;

    public static Role parse(String roleString) {
        switch (roleString) {
            case "player": return PLAYER;
            case "administrator": return ADMINISTRATOR;
            case "moderator": return MODERATOR;
            case "observer": return OBSERVER;
            default: return UNDEFINED;
        }
    }
}

