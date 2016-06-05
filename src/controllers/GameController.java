package controllers;

import enumerations.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import models.*;
import views.components.FieldTileNode;

import java.util.*;
import java.util.stream.Collectors;

public class GameController extends Controller {

    private ArrayList<Game> fetchedGames;
    private ArrayList<Turn> fetchedTurns;
    private ArrayList<Tile> fetchedTiles;
    private ArrayList<Tile> fetchedPot;
    private ArrayList<Message> fetchedMessages;

    private ObservableList<Game> games;
    private ObjectProperty<Game> selectedGame;
    private ObjectProperty<Role> currentRole;
    private ObjectProperty<Turn> selectedTurn;

    public GameController() {
        super();
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
                        && game.getOpponent().equals(opponent) && game.getReactionType() == ReactionType.UNKNOWN);
    }

    public void loadGame(Game game, Role gameMode) {
        if (game == null) return;
        if (!Objects.deepEquals(game.getTurns(), fetchedTurns)) fetch();

        if (game.getEmptyGameBoard() == null)
            game.setBoard(gameDAO.selectFieldsForBoard(game.getBoardType()));
        game.setAllTiles(fetchedTiles);
        ArrayList<Turn> difference = new ArrayList<>(fetchedTurns);
        difference.removeAll(game.getTurns());
        game.getTurns().addAll(difference);
        game.setMessages(fetchedMessages);
        game.setGameMode(gameMode);
    }

    @Override
    public void refresh() {

        if (games.contains(getSelectedGame())) {
            Game previousGame = getSelectedGame();
            if (previousGame.getTurns().size() != fetchedTurns.size()) {
                Game game = games.get(games.indexOf(previousGame));
                loadGame(game, getCurrentRole());
                checkForEndGame(game);
                setSelectedGame(game);
                setSelectedTurn(game.getLastTurn());
            }
            getSelectedGame().getTurnBuilder().setPot(fetchedPot);
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
            fetchedPot = gameDAO.selectPot(getSelectedGame());
            fetchedTiles = gameDAO.selectLettersForPot(getSelectedGame());
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
        if (!selectedGame.getLastTurn().getUser().equals(getSessionController().getCurrentUser()))
            return;

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
                selectedGame.getTurns().get(selectedGame.getTurns().size() - 2),
                selectedGame)) {
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
        selectedGame.setReactionType(ReactionType.ACCEPTED);
        gameDAO.updateReactionType(ReactionType.ACCEPTED, selectedGame);
    }

    public void rejectInvite(Game selectedGame) {
        selectedGame.setReactionType(ReactionType.REJECTED);
        gameDAO.updateReactionType(ReactionType.REJECTED, selectedGame);
    }

    private void createBeginTurns(Game selectedGame) {
        gameDAO.updateGameState(GameState.PLAYING, selectedGame);
        gameDAO.createPot(selectedGame);
        ArrayList<Tile> pot = gameDAO.selectLettersForPot(selectedGame);
        for (Turn turn : new TurnBuilder().buildBeginTurns(selectedGame)) {
            for (int i = 0; i < 7; i++) {
                int letterFromPot = new Random().nextInt(pot.size());
                turn.addRackTile(pot.get(letterFromPot));
                pot.remove(letterFromPot);
            }
            gameDAO.insertTurn(selectedGame, turn);
        }
    }

    private boolean isUserInSelectedComp(User requester, Competition comp) {
        return getCompetitionController().isUserInCompetition(requester, comp);
    }

    private boolean playingGame(User challenger, User opponent, Competition comp) {
        return comp.getGames().stream().anyMatch(game ->
                game.getPlayers().containsAll(Arrays.asList(challenger, opponent))
                        && game.isActive());
    }
}
