package enumerations;

public enum GameState {
    UNDEFINED,
    FINISHED,
    REQUEST,
    PLAYING;

    public static GameState parse(String gameState) {
        switch (gameState) {
            case "finished": return FINISHED;
            case "request" : return REQUEST;
            case "playing" : return PLAYING;
            default: return UNDEFINED;
        }
    }
}
