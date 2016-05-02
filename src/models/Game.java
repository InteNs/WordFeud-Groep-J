package models;

import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    private int id;
    private ArrayList<Message> messages;
    private GameState gameState;
    private User opponent;
    private User challenger;
    private Language language;
    private BoardType boardType;
    private ArrayList<Turn> turns; //TODO apply these turns to fields when building board
//  private ArrayList<Field> fields;
    private Field[][] fields;

    public Game(int id, User challenger, User opponent, GameState state, BoardType boardType, Language language) {
        this.id = id;
        this.challenger = challenger;
        this.opponent = opponent;
        this.gameState = state;
        this.language = language;
        this.boardType = boardType;
    }

    public int getId() {
        return id;
    }

    public int setMessages(ArrayList<Message> messages) {
        int diff = 0;
        if(this.messages != null) diff = messages.size() - this.messages.size();
        this.messages = messages;
        return diff;
    }

    public int setTurns(ArrayList<Turn> turns) {
        int diff = 0;
        if(this.turns != null) diff = turns.size() - this.turns.size();
        this.turns = turns;
        return diff;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public ArrayList<User> getPlayers() {
        return new ArrayList<>(Arrays.asList(challenger, opponent));
    }

    public boolean hasPlayer(User user) {
        return getPlayers().contains(user);
    }

    public GameState getGameState() {
        return gameState;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    
    public void setBoard(Field[][] fields){
        this.fields = fields;
        for (int i = 0; i <this.fields.length ; i++) {
            for (int j = 0; j < this.fields.length; j++) {
                System.out.println("x="+i+" " +
                        "y="+j+" " +
                        "TileType="+fields[i][j].getFieldType());
            }
        }
    }


    public boolean verifyTilePlacement(Tile tile,Field field){
        if (field.getTile()==null){
            field.setTile(tile);
            return true;
        }
        return false;
    }

    public int calculateWordScore(Field field){
        return 5;
    }

    @Override
    public String toString() {
        return "[" + id + "]["+language+"] " +boardType.toString().toLowerCase()
                +" spel tussen " + challenger + " en " + opponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return id == game.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
