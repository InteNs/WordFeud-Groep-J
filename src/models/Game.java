package models;

import enumerations.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {

    private int id;
    private int competitionId;
    private ObservableList<Message> messages;
    private GameState gameState;
    private Role gameMode;
    private User opponent;
    private User challenger;
    private Language language;
    private BoardType boardType;
    private ObservableList<Turn> turns;
    private Field[][] emptyGameBoard;   // SHOULD NOT BE OVERWRITTEN
    private Field[][] gameBoard;        // USE THIS INSTEAD
    private ObservableList<Tile> currentRack;
    private ObservableList<Field> fieldsChangedThisTurn;
    private ObservableList<Tile> playingPot;
    private ObservableList<Tile> allTiles;
    private ArrayList<ArrayList<Field>> listOfFieldsWithWordsThisTurn;
    private int scoreThisTurn;

    public Game(int id, int competitionId, User challenger, User opponent, GameState state, BoardType boardType, Language language) {
        this.id = id;
        this.competitionId = competitionId;
        this.challenger = challenger;
        this.opponent = opponent;
        this.gameState = state;
        this.language = language;
        this.boardType = boardType;
        this.fieldsChangedThisTurn = FXCollections.observableArrayList();
        this.currentRack = FXCollections.observableArrayList();
        this.messages = FXCollections.observableArrayList();
        this.turns = FXCollections.observableArrayList();
        this.playingPot = FXCollections.observableArrayList();
        this.allTiles = FXCollections.observableArrayList();
        this.fieldsChangedThisTurn.addListener((ListChangeListener<? super Field>) observable -> {
            verifyAndCalculate();
        });
    }

    private void verifyAndCalculate() {
        listOfFieldsWithWordsThisTurn = null;
        if (!fieldsChangedThisTurn.isEmpty()) {
            Character fixedAxis = verifyCurrentTurn();
            if (fixedAxis != null) {
                listOfFieldsWithWordsThisTurn = resolveWords(fixedAxis);
                scoreThisTurn = calculateTotalScore(listOfFieldsWithWordsThisTurn);
            }
        }
    }

    public int getScoreThisTurn() {
        return scoreThisTurn;
    }

    public ArrayList<Tile> getTilesChangedThisTurn(){
        return fieldsChangedThisTurn.stream()
                .map(Field::getTile)
                .collect(Collectors.toCollection(ArrayList<Tile>::new));
    }

    public ArrayList<ArrayList<Field>> getListOfFieldsWithWordsThisTurn() {
        return listOfFieldsWithWordsThisTurn;
    }

    public ArrayList<String> getWordsFoundThisTurn() {
        ArrayList<String> words = new ArrayList<>();
        listOfFieldsWithWordsThisTurn.forEach(fields -> {
            StringBuilder newWord = new StringBuilder();
            fields.stream()
                    .map(Field::getTile).forEach(tile -> newWord.append(tile.toString()));
            words.add(newWord.toString());
        });
        return words;
    }

    public void setGameMode(Role gameMode) {
        this.gameMode = gameMode;
    }

    public Role getGameMode() {
        return gameMode;
    }

    public boolean isGame() {
        return gameState == GameState.FINISHED || gameState == GameState.PLAYING;
    }

    public int getId() {
        return id;
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }

    public ObservableList<Turn> getTurns() {
        return turns;
    }

    public Turn getLastTurn() {
        if (turns != null && !turns.isEmpty())
            return turns.get(turns.size()-1);
        else return null;
    }

    public int getCompetitionId(){
        return competitionId;
    }

    public ArrayList<User> getPlayers() {
        return new ArrayList<>(Arrays.asList(challenger, opponent));
    }

    public User getOpponent() {
        return opponent;
    }

    public User getChallenger() {
        return challenger;
    }

    public GameState getGameState() {
        return gameState;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public Language getLanguage() {
        return language;
    }

    public ObservableList<Tile> getCurrentRack() {
        return currentRack;
    }

    public ObservableList<Field> getFieldsChangedThisTurn() {
        return fieldsChangedThisTurn;
    }

    public Field[][] getGameBoard() {
        return gameBoard;
    }

    public int setMessages(ArrayList<Message> messages) {
        int diff = 0;
        if (this.messages != null) {
            diff = messages.size() - this.messages.size();
            this.messages.setAll(messages);
        }

        return diff;
    }

    public int setTurns(ArrayList<Turn> turns) {
        int diff = 0;
        if (this.turns != null){
            diff = turns.size() - this.turns.size();
            this.turns.setAll(turns);
        }
        return diff;
    }

    /**
     * set the initial board for this game
     * @param fields the fields for this board
     */
    public void setBoard(Field[][] fields) {
        this.emptyGameBoard = fields;
        gameBoard = cloneGameBoard(emptyGameBoard);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void addPlacedTile(Field field, Tile tile) {
        field.setTile(tile);
        tile.setX(field.getX());
        tile.setY(field.getY());
        fieldsChangedThisTurn.add(field);
    }

    /**
     * set the board to a specific turn that has already completed
     * @param turnToDisplay the last turn to be added to the board
     */
    public void setBoardStateTo(Turn turnToDisplay) {
        gameBoard = cloneGameBoard(emptyGameBoard);
        playingPot.setAll(allTiles);
        for (Turn turn : turns) {
            playingPot.removeAll(turn.getRack());
            playingPot.removeAll(turn.getPlacedTiles());
            for (Tile tile : turn.getPlacedTiles())
                gameBoard[tile.getY()][tile.getX()].setTile(tile);

            if(turn.equals(turnToDisplay)) {
                if(gameMode == Role.OBSERVER)
                    currentRack.setAll(turn.getRack());
                else if(isLastTurn(turn))
                    currentRack.setAll(turns.get(turns.indexOf(turn)-1).getRack());
                return;
            }
        }
    }

    public void setPot(ArrayList<Tile> tilesForPot) {
        allTiles.setAll(tilesForPot);
    }

    public ObservableList<Tile> getPot() {
        return playingPot;
    }

    public void removePlacedTile(Field field) {
        field.getTile().clearCoordinates();
        field.setTile(null);
        fieldsChangedThisTurn.remove(field);
    }

    public boolean hasPlayer(User user) {
        return getPlayers().contains(user);
    }

    public boolean isLastTurn(Turn selectedTurn) {
        return selectedTurn == getLastTurn();
    }

    /**
     * check if the current state of the game is a valid turn
     * @return validTurn
     */
    public Character verifyCurrentTurn() {
        boolean validTurn = true;
        char fixedAxis = 0;

        if (fieldsChangedThisTurn.size() == 0 || startFieldIsEmpty()) {
            validTurn = false;
        }

        if (validTurn) {

        /* fetch first X and all X coords */
            int x = fieldsChangedThisTurn.get(0).getX();
            ArrayList<Integer> xValues = fieldsChangedThisTurn.stream()
                    .map(Field::getX)
                    .collect(Collectors.toCollection(ArrayList<Integer>::new));

        /* fetch first Y and All X coords */
            int y = fieldsChangedThisTurn.get(0).getY();
            ArrayList<Integer> yValues = fieldsChangedThisTurn.stream()
                    .map(Field::getY)
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
                    if (gameBoard[y][x].getTile() == null && !xValues.contains(x))
                        validTurn = false;
            } else {
                // check if all Y coords are not null
                for (y = Collections.min(yValues); y <= Collections.max(yValues); y++)
                    if (gameBoard[y][x].getTile() == null && !yValues.contains(y))
                        validTurn = false;
            }
        }

        if (validTurn) {
            if (fieldsChangedThisTurn.size() == 1 && findWords(checkColumn(fieldsChangedThisTurn.get(0).getX())) == null) {
                if (findWords(new ArrayList<>(Arrays.asList(gameBoard[fieldsChangedThisTurn.get(0).getY()]))) == null) {
                    validTurn = false;
                } else {
                    fixedAxis = 'y';
                }
           }
       }

        if (validTurn) {
            return fixedAxis;
        } else {
            return null;
        }

    }

    public ArrayList<ArrayList<Field>> resolveWords(char fixedAxis) {
        ArrayList<ArrayList<Field>> wordsFound = new ArrayList<>();
        ArrayList<Field> word;
        switch (fixedAxis){
            case 'x':
                int xPos = getFieldsChangedThisTurn().get(0).getX();
                wordsFound.add(findWords(checkColumn(xPos)));
                for (Field field : getFieldsChangedThisTurn()) {
                    word=findWords(new ArrayList<>(Arrays.asList(gameBoard[field.getY()])));
                    if (word != null){
                        wordsFound.add(word);
                    }
                }
                break;
            case 'y':
                int yPos = getFieldsChangedThisTurn().get(0).getY();
               wordsFound.add(findWords(new ArrayList<>(Arrays.asList(gameBoard[yPos]))));
                for (Field field : fieldsChangedThisTurn) {
                    word = findWords(checkColumn(field.getX()));
                    if (word!=null){
                        wordsFound.add(word);
                    }
                }
                break;
            default:
                break;
        }
        return wordsFound;
    }

    private int calculateTotalScore(ArrayList<ArrayList<Field>> wordsFound){
        int totalScore = 0;
        for (ArrayList<Field> word : wordsFound) {
            totalScore += calculateWordScore(word);
        }
        if (fieldsChangedThisTurn.size()==7){
            totalScore += 40;
        }
        return totalScore;
    }

    private int calculateWordScore(ArrayList<Field> word){
        int wordScore = 0;
        for (Field field : word) {
            if (fieldsChangedThisTurn.contains(field) && (field.getFieldType() == FieldType.DL || field.getFieldType() == FieldType.TL)){
                if (field.getFieldType() == FieldType.TL){
                    wordScore += field.getTile().getValue()*3;
                } else {
                    wordScore += field.getTile().getValue()*2;
                }
            } else {
                wordScore += field.getTile().getValue();
            }
        }
        for (Field field : word) {
            if (fieldsChangedThisTurn.contains(field) && (field.getFieldType() == FieldType.DW || field.getFieldType() == FieldType.TW)) {
                if (field.getFieldType() == FieldType.TW){
                    wordScore = wordScore*3;
                } else {
                    wordScore = wordScore*2;
                }
            }
        }
        return wordScore;
    }

    private ArrayList<Field> findWords (ArrayList<Field> turnFields){
        ArrayList<Field> word = new ArrayList<>();
        for (Field field : turnFields) {
            if (field.getTile()!= null){
                word.add(field);
            } else {
                for (Field wordField : word) {
                    if (fieldsChangedThisTurn.contains(wordField) && word.size()>1){
                        return word;
                    }
                }
                word.clear();
            }
        }
        return null;
    }

    private ArrayList<Field> checkColumn(int xPos){
        ArrayList<Field> returnColumn = new ArrayList<>();
        for (int y = 0; y < gameBoard.length; y++) {
            returnColumn.add(gameBoard[y][xPos]);
        }
        return returnColumn;
    }

    private Field[][] cloneGameBoard(Field[][] emptyGameBoard){
        Field[][] clonedGameBoard = new Field[15][15];
        for (int y = 0; y < emptyGameBoard.length; y++) {
            for (int x = 0; x < emptyGameBoard.length; x++) {
                clonedGameBoard[y][x] = new Field(emptyGameBoard[y][x].getFieldType(),x,y);
            }
        }
        return clonedGameBoard;
    }

    private boolean startFieldIsEmpty() {
        for (int y = 0; y < gameBoard.length; y++)
            for (int x = 0; x < gameBoard.length; x++)
                if (gameBoard[y][x].getFieldType() == FieldType.STARTTILE)
                    return gameBoard[y][x].getTile() == null;
        return false;
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

    public void sendMessage(User currentUser, String text) {
        messages.add(new Message(currentUser, text, new Timestamp(System.currentTimeMillis())));
    }

    public void fillCurrentRack() {
        for (int i = 0; i < 8-currentRack.size(); i++) {
            if (!getPot().isEmpty())
            currentRack.add(getPot().get(new Random().nextInt(getPot().size())));
        }
    }
}
