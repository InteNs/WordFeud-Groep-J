package models;

import enumerations.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
    private User opponent;
    private int opponentScore;
    private int challengerScore;
    private User challenger;
    private Language language;
    private BoardType boardType;
    private ObservableList<Turn> turns;
    private Field[][] emptyGameBoard;   // SHOULD NOT BE OVERWRITTEN
    private ObservableList<Tile> allTiles;
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
        reactionType = reaktie_type;
        this.turnBuilder = new TurnBuilder();

        turns.addListener((ListChangeListener<? super Turn>) observable ->
            this.lastTurnNumber = getLastTurn().getId()
        );
    }

    public void setOpponentScore(int score) {
        opponentScore = score;
    }

    public void setChallengerScore(int score) {
        challengerScore = score;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public int getChallengerScore() {
        return challengerScore;
    }

    public boolean isGame() {
        return gameState == GameState.FINISHED
                || gameState == GameState.PLAYING
                || gameState == GameState.RESIGNED;
    }

    public boolean isActive() {
        return gameState == GameState.PLAYING || (gameState == GameState.REQUEST && reactionType != ReactionType.REJECTED);
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
    public void setBoardStateTo(Turn turnToDisplay, User watcher, Role watchRole) {
        Field[][] gameBoard = cloneGameBoard(emptyGameBoard);
        for (Turn turn : turns) {
            // save fields for building turn word
            ArrayList<Field> changed = new ArrayList<>();
            //place tile
            for (Tile tile : turn.getPlacedTiles()) {
                gameBoard[tile.getY()][tile.getX()].setTile(tile);
                changed.add(gameBoard[tile.getY()][tile.getX()]);
            }
            //save amount of tiles swapped
            if (turn.getType() == TurnType.SWAP) {
                ArrayList<Tile> difference = new ArrayList<>(turns.get(turns.indexOf(turn) - 2).getRack());
                difference.removeAll(turn.getRack());
                turn.setAmountSwapped(difference.size());
            }
            //set turn word
            turn.setWord(new TurnBuilder().getTurnWord(gameBoard, FXCollections.observableArrayList(changed)));

            if (turn.equals(turnToDisplay)) {
                ArrayList<Tile> rack = new ArrayList<>();
                if (watchRole == Role.OBSERVER || watcher.equals(turn.getUser())) {
                    rack = turn.getRack();
                } else if (!watcher.equals(turn.getUser()) && turn.getId() != 1) {
                    rack = turns.get(turns.indexOf(turn) - 1).getRack();
                }
                turnBuilder = new TurnBuilder(gameBoard, FXCollections.observableArrayList(rack));
                //turnBuilder.setPot(pot);
                break;
            }

        }
    }

    public ObservableList<Tile> getPot() {
        if (turnBuilder == null) return null;
        return turnBuilder.getPot();
    }

    public TurnBuilder getTurnBuilder() {
        return turnBuilder;
    }

    public void setTurnBuilder(TurnBuilder turnBuilder) {
        this.turnBuilder = turnBuilder;
    }

    public boolean hasPlayer(User user) {
        return getPlayers().contains(user);
    }

    public boolean isLastTurn(Turn selectedTurn) {
        return selectedTurn.equals(getLastTurn());
    }

    public User getWinner() {
        return (opponentScore > challengerScore) ? opponent : challenger;
    }

    public int getWinnerScore() {
       return  (opponentScore > challengerScore) ? opponentScore : challengerScore;
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

    public User getNextUser() {
        if ((lastTurnNumber & 1) == 0)
            return challenger;
        else
            return opponent;
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

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
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
        if (lastTurnNumber != game.lastTurnNumber) return false;
        return reactionType == game.reactionType;
    }

    public int deepHashCode() {
        int result = id;
        result = 31 * result + competitionId;
        result = 31 * result + (gameState != null ? gameState.hashCode() : 0);
        result = 31 * result + (reactionType != null ? reactionType.hashCode() : 0);
        return result;
    }

    public void setAllTiles(ArrayList<Tile> allTiles) {
        this.allTiles.setAll(allTiles);
    }
}
