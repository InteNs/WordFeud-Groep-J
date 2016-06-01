package enumerations;

public enum GameState {
    UNDEFINED,
    FINISHED,
    REQUEST,
    PLAYING,
    RESIGNED;

    public static GameState parse(String gameState) {
        switch (gameState) {
            case "finished": return FINISHED;
            case "request" : return REQUEST;
            case "playing" : return PLAYING;
            case "resigned" : return RESIGNED;
            default: return UNDEFINED;
        }
    }

    public static String format(GameState gameState) {
        switch (gameState) {
            default:
                return "";
            case FINISHED:
                return "finished";
            case REQUEST:
                return "request";
            case PLAYING:
                return "playing";
            case RESIGNED:
                return "resigned";
        }
    }
}
