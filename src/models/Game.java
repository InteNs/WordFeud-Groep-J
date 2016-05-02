package models;

import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Game {

    private int id;
    private ArrayList<Message> messages;
    private GameState gameState;
    private User opponent;
    private User challenger;
    private Language language;
    private BoardType boardType;
    private ArrayList<Turn> turns;
    private Field[][] emptyGameBoard;   // SHOULD NOT BE OVERWRITTEN
    private Field[][] gameBoard;        // USE THIS INSTEAD
    private ArrayList<Field> fieldsChangedThisTurn;
    private ArrayList<Tile> allTilesCache;

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
        if (this.messages != null) diff = messages.size() - this.messages.size();
        this.messages = messages;
        return diff;
    }

    public int setTurns(ArrayList<Turn> turns) {
        int diff = 0;
        if (this.turns != null) diff = turns.size() - this.turns.size();
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

    /**
     * set the initial board for this game
     * @param fields the fields for this board
     */
    public void setBoard(Field[][] fields) {
        this.emptyGameBoard = fields;
        gameBoard = emptyGameBoard;
    }

    /**
     * set the board to a speficic turn that has already completed
     * @param turnToDisplay the last turn to be added to the board
     */
    public void setBoardStateTo(Turn turnToDisplay) {
        gameBoard = emptyGameBoard;
        for (Turn turn : turns) {
            for (Tile tile : turn.getPlacedTiles())
                gameBoard[tile.getX() - 1][tile.getY() - 1].setTile(tile);
            if(turn.equals(turnToDisplay)) {
                break;
            }
        }
    }

    /**
     * fetches all the tiles placed in all the completed turns
     * @return the (cached) tiles
     */
    public ArrayList<Tile> getPlacedTiles() {
        if(allTilesCache != null)
            return allTilesCache;
        turns.stream()
                .filter(turn -> turn.getPlacedTiles() != null)
                .forEach(turn -> allTilesCache.addAll(turn.getPlacedTiles()));
        return allTilesCache;
    }

    /**
     * placed a tile on a field
     * @param tile tile to place
     * @param field field to place the tile in
     * @return false if the field already has a tile
     */
    public boolean placeTile(Tile tile,Field field){
        if (field.getTile()==null){
            field.setTile(tile);
            fieldsChangedThisTurn.add(field);
            return true;
        }
        return false;
    }

    /**
     * check if the current state of the game is a valid turn
     * @return validTurn
     */
    public boolean verifyCurrentTurn() {

        boolean validTurn = true;
        char fixedAxis;

        /* fetch first X and all X coords */
        int x = fieldsChangedThisTurn.get(0).getTile().getX();
        ArrayList<Integer> xValues = fieldsChangedThisTurn.stream()
                .map(field -> field.getTile().getX())
                .collect(Collectors.toCollection(ArrayList<Integer>::new));

        /* fetch first Y and All X coords */
        int y = fieldsChangedThisTurn.get(0).getTile().getY();
        ArrayList<Integer> yValues = fieldsChangedThisTurn.stream()
                .map(field -> field.getTile().getY())
                .collect(Collectors.toCollection(ArrayList<Integer>::new));

        /* check if placed on single axis, if not -> invalidate turn */
        if (Collections.frequency(xValues, x) != xValues.size()) {
            if (Collections.frequency(yValues, y) != yValues.size())
                validTurn = false;  // Y and X are not equal for all tiles

            fixedAxis = 'y';        // Y is equal for all tiles
        } else
            fixedAxis = 'x';        // X is equal for all tiles

        /* check if there are gaps between placed letters */
        if (fixedAxis == 'y') {
            // check if all X coords are not null
            for (x = Collections.min(xValues); x <= Collections.max(xValues); x++)
                if (gameBoard[x][y].getTile() == null && !xValues.contains(x))
                    validTurn = false;
        } else {
            // check if all Y coords are not null
            for (y = Collections.min(yValues); y <= Collections.max(yValues); y++)
                if (gameBoard[x][y].getTile() == null && !xValues.contains(y))
                    validTurn = false;
        }

        return validTurn;
    }

    //TODO:
    public int calculateWordScore(Field field){
        int wordScore=0;
        //Ask which words can be made with the addition of the recently placed tile

        //HORIZONTAL CHECK --RIGHT
        for (int x = field.getTile().getX()-1; x < gameBoard.length ; x++) {
            for (int y = field.getTile().getY()-1; y < gameBoard.length ; y++) {

            }
        }

        //HORIZONTAL CHECK --LEFT
        for (int x=field.getTile().getX()-1; x >=0; x--) {
            for (int y =field.getTile().getY()-1; y >0 ; y--) {

            }
        }

        //VERTICAL CHECK --UP

        //VERTICAL CHECK --DOWN

        //for each word that can be made, calculate the score of that word
        //Add the score to the wordScore


        return wordScore;
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
