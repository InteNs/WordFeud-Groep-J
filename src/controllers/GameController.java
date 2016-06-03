package controllers;

import enumerations.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import models.*;
import views.components.FieldTileNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameController extends Controller {

    private ArrayList<Game> fetchedGames;
    private ArrayList<Turn> fetchedTurns;
    private ArrayList<Message> fetchedMessages;
    private ObservableList<Game> games;
    private ObjectProperty<Game> selectedGame;
    private ObjectProperty<Role> currentRole;
    private ObjectProperty<Turn> selectedTurn;

    public GameController(ControllerFactory controllerFactory) {
        super(controllerFactory);
        games = FXCollections.observableArrayList();
        selectedGame = new SimpleObjectProperty<>();
        selectedTurn = new SimpleObjectProperty<>();
        currentRole = new SimpleObjectProperty<>();
    }

    public ObjectProperty<Role> currentRoleProperty() {
        return currentRole;
    }

    public Role getCurrentRole() {
        return currentRole.get();
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole.set(currentRole);
    }

    public ObjectProperty<Game> selectedGameProperty() {
        return selectedGame;
    }

    public Game getSelectedGame() {
        return selectedGame.get();
    }

    public void setSelectedGame(Game game) {
        selectedGame.set(game);
    }

    public Turn getSelectedTurn() {
        return selectedTurn.get();
    }

    public void setSelectedTurn(Turn selectedTurn) {
        this.selectedTurn.set(selectedTurn);
    }

    public ObjectProperty<Turn> selectedTurnProperty() {
        return selectedTurn;
    }

    public ObservableList<Game> getGames() {
        return games.filtered(Game::isGame);
    }

    public ObservableList<Game> getGames(Competition competition) {
        return games.filtered(game -> game.getCompetitionId() == competition.getId());
    }

    public ObservableList<Game> getOutgoingChallenges(User challenger) {
        return games.filtered(game ->
                game.getGameState() == GameState.REQUEST
                        && game.getChallenger().equals(challenger));
    }

    public ObservableList<Game> getIncomingChallenges(User opponent) {
        return games.filtered(game ->
                game.getGameState() == GameState.REQUEST
                        && game.getOpponent().equals(opponent));
    }

    public void loadGame(Game game, Role gameMode) {
        if (game == null) return;
        fetch();
        if (game.getEmptyGameBoard() == null)
            game.setBoard(gameDAO.selectFieldsForBoard(game.getBoardType()));
        game.setTurns(fetchedTurns);
        game.setMessages(fetchedMessages);
        game.setGameMode(gameMode);
    }

    @Override
    public void refresh() {
        if (games.contains(getSelectedGame())) {
            Game game = games.get(games.indexOf(getSelectedGame()));
            TurnBuilder previousTurnBuilder = getSelectedGame().getTurnBuilder();
            Field[][] previousBoard = getSelectedGame().getEmptyGameBoard();
            game.setBoard(previousBoard);
            loadGame(game, getCurrentRole());
            setSelectedGame(game);
            if (game.getTurns().contains(getSelectedTurn())) {
                Turn turn = game.getTurns().get(game.getTurns().indexOf(getSelectedTurn()));
                game.setBoardStateTo(turn, getSessionController().getCurrentUser());
                game.setTurnBuilder(previousTurnBuilder);
                if (getCurrentRole() == Role.PLAYER) {
                    setSelectedTurn(null);
                    setSelectedTurn(game.getLastTurn());
                }
                else setSelectedTurn(turn);
            }
        }
        getOutgoingChallenges(getSessionController().getCurrentUser())
                .stream()
                .filter(game -> game.getReactionType() == ReactionType.ACCEPTED
                        && game.getGameState() == GameState.REQUEST)
                .forEach(game -> {
                    game.setGameState(GameState.PLAYING);
                    createBeginTurns(game);
                });
    }

    @Override
    public void refill() {
        if (!games.equals(fetchedGames) || !games.stream().allMatch(game ->
                game.deepEquals(fetchedGames.get(fetchedGames.indexOf(game)))))
            games.setAll(fetchedGames);
    }

    @Override
    public void fetch() {
        fetchedGames = gameDAO.selectGames();
        if (getSelectedGame() != null) {
            fetchedMessages = gameDAO.selectMessages(getSelectedGame());
            fetchedTurns = gameDAO.selectTurns(getSelectedGame());

        }
    }

    public void setPlayerRack(Game game, List<Tile> tiles) {
        game.getTurnBuilder().getCurrentRack().setAll(tiles);
    }

    public void setBoardState(Game game, Turn turn) {
        game.setBoardStateTo(turn, getSessionController().getCurrentUser());
    }

    public void placeTile(Game game, Field field, Tile tile) {
        game.getTurnBuilder().addPlacedTile(field, tile);
    }

    public void removeTile(Game game, Field field) {
        game.getTurnBuilder().removePlacedTile(field);
    }

    public ObservableList<Field> getFieldsChanged(Game game) {
        return game.getTurnBuilder().getFieldsChanged();
    }

    public void sendMessage(Game game, User user, String text) {
        game.sendMessage(user, text);
        gameDAO.insertMessage(game, user, text);
    }

    public boolean isJokerTile(Tile tile) {
        return tile.getCharacter() == '?';
    }

    public ArrayList<String> playWord(Game selectedGame) {
        if (selectedGame.getTurnBuilder().verifyCurrentTurn() == null)
            return null;
        ArrayList<String> wordsNotInDictionary =
                gameDAO.selectWords(selectedGame, selectedGame.getTurnBuilder().getWordsFoundThisTurn())
                        .stream()
                        .filter(pair -> !pair.getValue())
                        .map(Pair::getKey)
                        .collect(Collectors.toCollection(ArrayList::new));

        if (wordsNotInDictionary.size() == 0) {
            selectedGame.getTurnBuilder().fillCurrentRack(selectedGame.getPot());
            insertTurn(selectedGame, TurnType.WORD);
        }
        return wordsNotInDictionary;
    }

    private void checkForEndGame(Game selectedGame) {
        switch (selectedGame.getLastTurn().getType()) {
            case PASS:
                if (isThirdPass(selectedGame)) {
                    buildEndTurns(selectedGame);
                    gameDAO.updateGameState(GameState.FINISHED, selectedGame);
                }
                break;
            case RESIGN:
                buildEndTurns(selectedGame);
                gameDAO.updateGameState(GameState.RESIGNED, selectedGame);
                break;
            case WORD:
                if (selectedGame.getLastTurn().getRack().isEmpty()
                        && selectedGame.getPot().isEmpty()) {
                    buildEndTurns(selectedGame);
                    gameDAO.updateGameState(GameState.FINISHED, selectedGame);
                }
                break;
            default:
                break;
        }
    }

    private void buildEndTurns(Game selectedGame) {
        for (Turn turn : selectedGame.getTurnBuilder().buildEndTurns(
                selectedGame.getLastTurn(),
                selectedGame.getTurns().get(selectedGame.getTurns().size() - 2))) {
            gameDAO.insertTurn(selectedGame, turn);
            selectedGame.addTurn(turn);
        }
    }

    public ObservableList<Tile> showPot(Game game) {
        if (game != null)
            return game.getPot();
        return null;
    }

    public void passTurn(Game selectedGame) {
        insertTurn(selectedGame, TurnType.PASS);
    }

    public void resign(Game selectedGame) {
        insertTurn(selectedGame, TurnType.RESIGN);
    }

    private void insertTurn(Game selectedGame, TurnType turnType) {
        Turn newTurn = selectedGame.getTurnBuilder().buildTurn(
                selectedGame.getLastTurn().getId() + 1,
                getSessionController().getCurrentUser(), turnType
        );
        gameDAO.insertTurn(selectedGame, newTurn);
        selectedGame.addTurn(newTurn);
        checkForEndGame(selectedGame);
    }

    private boolean isThirdPass(Game selectedGame) {
        return selectedGame.getTurns().get(selectedGame.getTurns().size() - 2).getType() == TurnType.PASS
                && selectedGame.getTurns().get(selectedGame.getTurns().size() - 3).getType() == TurnType.PASS;
    }

    public void swapTiles(ObservableList<FieldTileNode> swapTiles, Game selectedGame) {
        for (FieldTileNode field : swapTiles) {
            selectedGame.getTurnBuilder().getCurrentRack().remove(field.getTile());
        }
        selectedGame.getTurnBuilder().fillCurrentRack(selectedGame.getPot());
        insertTurn(selectedGame, TurnType.SWAP);
    }

    public int challenge(Language language, User requester, User receiver, Competition comp) {
        if (isUserInSelectedComp(requester, comp)) {
            if (!this.playingGame(requester, receiver, comp)) {
                if (validInvite(requester, receiver)) {
                    Game game = new Game(
                            0, 0, comp.getId(),
                            requester,
                            receiver,
                            GameState.REQUEST,
                            BoardType.STANDARD,
                            language,
                            ReactionType.UNKNOWN);
                    games.add(game);
                    gameDAO.createGame(comp.getId(), requester.getName(), language, receiver.getName());
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }

    private boolean validInvite(User requester, User receiver) {
        return !requester.getName().equals(receiver.getName());
    }

    public void acceptInvite(Game selectedGame) {
        gameDAO.updateReactionType(ReactionType.ACCEPTED, selectedGame);
    }

    public void rejectInvite(Game selectedGame) {
        gameDAO.updateReactionType(ReactionType.REJECTED, selectedGame);
    }

    private void createBeginTurns(Game selectedGame) {
        gameDAO.updateGameState(GameState.PLAYING, selectedGame);
        gameDAO.createPot(selectedGame);
        selectedGame.setPot(gameDAO.selectLettersForPot(selectedGame));
        for (Turn turn : new TurnBuilder().buildBeginTurns(selectedGame)) {
            for (int i = 0; i < 7; i++) {
                int letterFromPot = new Random().nextInt(selectedGame.getPot().size());
                turn.addRackTile(selectedGame.getPot().get(letterFromPot));
                selectedGame.getPot().remove(letterFromPot);
            }
            gameDAO.insertTurn(selectedGame, turn);
        }
    }

    private boolean isUserInSelectedComp(User requester, Competition comp) {
        return getCompetitionController().isUserInCompetition(requester, comp);
    }

    private boolean playingGame(User challenger, User opponent, Competition comp) {
        for (Game g : games)
            if (g.getChallenger().equals(challenger) && g.getOpponent().equals(opponent)
                    || (g.getChallenger().equals(opponent) && g.getOpponent().equals(challenger)))
                if (g.getGameState() != GameState.FINISHED || g.getGameState()!= GameState.RESIGNED)
                    if (g.getCompetitionId() == comp.getId())
                        return true;
        return false;
    }
}
