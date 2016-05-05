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

    public Turn getLastTurn() {
        if (turns != null && !turns.isEmpty())
            return arrayList.get(arrayList.size()-1);
        else return null;
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
        gameBoard = emptyGameBoard.clone();
    }

    /**
     * set the board to a specific turn that has already completed
     * @param turnToDisplay the last turn to be added to the board
     */
    public void setBoardStateTo(Turn turnToDisplay) {
        gameBoard = emptyGameBoard.clone();
        for (Turn turn : turns) {
            for (Tile tile : turn.getPlacedTiles())
                gameBoard[tile.getY()][tile.getX()].setTile(tile);
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
        allTilesCache = new ArrayList<Tile>();
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
        return 5;
    }

    public boolean checkRow() {
        String rowStr = null;
        for (Field[] aGameBoard : gameBoard) {
            rowStr = Arrays.toString(aGameBoard);
            System.out.println(rowStr);
        }
        return false;
    }

    //TODO: This method receives a row/column from the GameBoard. It then checks if there are any words that can be made.
    //TODO: If words can be made, crosschecks with the words in the WordsList of the Game class --> new Word found? --> add word to the WordList and calculate score.
    //TODO: similar to making a string from the row, we also(or instead) need a Field arraylist so we can calculate score using fieldType and letterValue,
    //TODO: off course we already have the field array, we use it to make the string
    private ArrayList<String> getWords(String word){
    return null;
    }

    public Field[][] getGameBoard() {
        return gameBoard;
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
