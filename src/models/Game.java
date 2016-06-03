package models;

import enumerations.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Game {

    private int id;
    private int competitionId;
    private int lastTurnNumber;
    private ObservableList<Message> messages;
    private GameState gameState;
    private Role gameMode;
    private User opponent;
    private User challenger;
    private Language language;
    private BoardType boardType;
    private ObservableList<Turn> turns;
    private Field[][] emptyGameBoard;   // SHOULD NOT BE OVERWRITTEN
    private ObservableList<Tile> allTiles;
    private ObservableList<Tile> playingPot;
    private ObservableList<Tile> currentRack;
    private TurnBuilder turnBuilder;
    private ReactionType reactionType;

    public Game(int id, int lastTurnNumber, int competitionId, User challenger, User opponent, GameState state, BoardType boardType, Language language, ReactionType reaktie_type) {
        this.id = id;
        this.lastTurnNumber = lastTurnNumber;
        this.competitionId = competitionId;
        this.challenger = challenger;
        this.opponent = opponent;
        this.gameState = state;
        this.language = language;
        this.boardType = boardType;
        this.messages = FXCollections.observableArrayList();
        this.turns = FXCollections.observableArrayList();
        this.allTiles = FXCollections.observableArrayList();
        this.currentRack = FXCollections.observableArrayList();
        this.playingPot = FXCollections.observableArrayList();
        reactionType = reaktie_type;
    }

    public int getLastTurnNumber() {
        return lastTurnNumber;
    }

    public Role getGameMode() {
        return gameMode;
    }

    public void setGameMode(Role gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isGame() {
        return gameState == GameState.FINISHED
                || gameState == GameState.PLAYING
                || gameState == GameState.RESIGNED;
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

    public void setTurnBuilder(TurnBuilder turnBuilder) {
        this.turnBuilder = turnBuilder;
    }

    public void setTurns(ArrayList<Turn> turns) {
        this.turns.setAll(turns);
        this.turns.removeIf(Objects::isNull);
    }

    public Turn getLastTurn() {
        if (turns != null && !turns.isEmpty())
            return turns.get(turns.size() - 1);
        else return null;
    }

    public int getScore(User user, Turn selectedTurn) {
        int score = 0;
        for (Turn turn : turns) {
            if (turn.getUser().equals(user)) {
                score += turn.getScore();
            }
            if (turn.equals(selectedTurn)) {
                break;
            }
        }
        return score;
    }

    public int getCompetitionId() {
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

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public Language getLanguage() {
        return language;
    }


    public int setMessages(ArrayList<Message> messages) {
        int diff = 0;
        if (this.messages != null) {
            diff = messages.size() - this.messages.size();
            this.messages.setAll(messages);
        }

        return diff;
    }

    /**
     * set the initial board for this game
     *
     * @param fields the fields for this board
     */
    public void setBoard(Field[][] fields) {
        this.emptyGameBoard = fields;
    }

    public Field[][] getEmptyGameBoard() {
        return emptyGameBoard;
    }

    /**
     * set the board to a specific turn that has already completed
     *
     * @param turnToDisplay the last turn to be added to the board
     */
    public void setBoardStateTo(Turn turnToDisplay, User watcher) {
        Field[][] gameBoard = cloneGameBoard(emptyGameBoard);
        playingPot.setAll(allTiles);
        for (Turn turn : turns) {
            playingPot.removeAll(turn.getRack());
            playingPot.removeAll(turn.getPlacedTiles());
            ObservableList<Field> fieldsChanged = FXCollections.observableArrayList();
            for (Tile tile : turn.getPlacedTiles()) {
                gameBoard[tile.getY()][tile.getX()].setTile(tile);
                fieldsChanged.add(gameBoard[tile.getY()][tile.getX()]);
            }
            turn.setWord(new TurnBuilder().getTurnWord(gameBoard, fieldsChanged));

            if (turn.equals(turnToDisplay)) {

                if (gameMode == Role.OBSERVER || turn.getUser().equals(watcher))
                    currentRack.setAll(turn.getRack());
                else if (isLastTurn(turn)) {
                    currentRack.setAll(turns.get(turns.indexOf(turn) - 1).getRack());
                }
                turnBuilder = new TurnBuilder(gameBoard, currentRack);
                return;
            }
        }
    }

    public ObservableList<Tile> getPot() {
        return playingPot;
    }

    public void setPot(ArrayList<Tile> tilesForPot) {
        allTiles.setAll(tilesForPot);
        playingPot.setAll(tilesForPot);
    }

    public TurnBuilder getTurnBuilder() {
        return turnBuilder;
    }


    public boolean hasPlayer(User user) {
        return getPlayers().contains(user);
    }

    public boolean isLastTurn(Turn selectedTurn) {
        return selectedTurn.equals(getLastTurn());
    }

    private Field[][] cloneGameBoard(Field[][] emptyGameBoard) {
        Field[][] clonedGameBoard = new Field[15][15];
        for (int y = 0; y < emptyGameBoard.length; y++) {
            for (int x = 0; x < emptyGameBoard.length; x++) {
                clonedGameBoard[y][x] = new Field(emptyGameBoard[y][x].getFieldType(), x, y);
            }
        }
        return clonedGameBoard;
    }

    public String getNextUser() {
        if (!(this.getGameState() == GameState.FINISHED || this.getGameState() == GameState.RESIGNED)) {
            if ((lastTurnNumber & 1) == 0)
                return "currentUser";
            else
                return "opponent";
        }
        return "afgelopen";
    }

    @Override
    public String toString() {
        return "[" + id + "][" + language + "] " + boardType.toString().toLowerCase()
                + " spel tussen " + challenger + " en " + opponent;
    }


    public void sendMessage(User currentUser, String text) {
        messages.add(new Message(currentUser, text, new Timestamp(System.currentTimeMillis())));
    }


    public void addTurn(Turn newTurn) {
        turns.add(newTurn);
    }

    public ReactionType getReactionType() {
        return reactionType;
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

    public boolean deepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (id != game.id) return false;
        if (competitionId != game.competitionId) return false;
        if (gameState != game.gameState) return false;
        return reactionType == game.reactionType;
    }

    public int deepHashCode() {
        int result = id;
        result = 31 * result + competitionId;
        result = 31 * result + (gameState != null ? gameState.hashCode() : 0);
        result = 31 * result + (reactionType != null ? reactionType.hashCode() : 0);
        return result;
    }
}
