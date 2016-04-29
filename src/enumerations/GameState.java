package enumerations;

/**
 * Created by markhavekes on 29/04/16.
 */
public enum GameState {
    UNDEFINED,
    FINISHED,
    REQUEST,
    PLAYING;

    public static GameState stateFor(String state) {
        switch (state) {
            case "finished": return FINISHED;
            case "request" : return REQUEST;
            case "playing" : return PLAYING;
            default: return UNDEFINED;
        }
    }
}
