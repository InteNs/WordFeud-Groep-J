package controllers;

import enumerations.Role;
import enumerations.TurnType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameController extends Controller {

    private ArrayList<Game> fetched;
    private ObservableList<Game> games;
    private ObjectProperty<Game> selectedGame;
    private ObjectProperty<Role> currentRole;
    private ObjectProperty<Turn> selectedTurn;

    public GameController(ControllerFactory factory) {
        super(factory);
        games = FXCollections.observableArrayList();
        selectedGame = new SimpleObjectProperty<>();
        selectedTurn = new SimpleObjectProperty<>();
        currentRole = new SimpleObjectProperty<>();
        selectedGameProperty().addListener((o, oV, nV) -> loadGame(nV, getCurrentRole()));
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole.set(currentRole);
    }

    public ObjectProperty<Role> currentRoleProperty() {
        return currentRole;
    }

    public Role getCurrentRole() {
        return currentRole.get();
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

    public void loadGame(Game game, Role gameMode) {
        if (game == null) return;
        game.setBoard(gameDAO.selectFieldsForBoard(game.getBoardType()));
        game.setTurns(gameDAO.selectTurns(game));
        game.setMessages(gameDAO.selectMessages(game));
        game.setGameMode(gameMode);
    }

    @Override
    public void refresh() {
        if (games.contains(getSelectedGame())) {
            Game game = games.get(games.indexOf(getSelectedGame()));
            TurnBuilder previous = getSelectedGame().getTurnBuilder();
            loadGame(game, getCurrentRole());

            setSelectedGame(game);

            if (game.getTurns().contains(getSelectedTurn())) {
                Turn turn = game.getTurns().get(game.getTurns().indexOf(getSelectedTurn()));
                setSelectedTurn(turn);
                game.setBoardStateTo(turn, getSession().getCurrentUser());
            }
            if(getSelectedGame().getTurns().size() == game.getTurns().size())
                game.setTurnBuilder(previous);
        }
    }

    @Override
    public void refill() {
        games.setAll(fetched);
    }

    @Override
    public void fetch() {
        fetched = gameDAO.selectGames();
    }

    public void setPlayerRack(Game game, List<Tile> tiles) {
        game.getTurnBuilder().getCurrentRack().setAll(tiles);
    }

    public void setBoardState(Game game, Turn turn) {
        game.setBoardStateTo(turn, getSession().getCurrentUser());
    }

    public void placeTile(Game game, Field field, Tile tile) {
        game.getTurnBuilder().addPlacedTile(field, tile);
    }

    public void removeTile(Game game, Field field) {
        game.getTurnBuilder().removePlacedTile(field);
    }

    public void sendMessage(Game game, User user, String text) {
        game.sendMessage(user, text);
        gameDAO.insertMessage(game, user, text);
    }

    public boolean isJokerTile(Tile tile) {
        return tile.getCharacter() == '?';
    }

    public ArrayList<String> playWord(Game selectedGame) {
        ArrayList<String> wordsNotInDictionary =
                gameDAO.selectWords(selectedGame, selectedGame.getTurnBuilder().getWordsFoundThisTurn())
                        .stream()
                        .filter(pair -> !pair.getValue())
                        .map(Pair::getKey)
                        .collect(Collectors.toCollection(ArrayList::new));

        if (wordsNotInDictionary.size() == 0) {
            selectedGame.getTurnBuilder().fillCurrentRack(selectedGame.getPot());
            Turn newTurn =   selectedGame.getTurnBuilder().buildTurn(
                    selectedGame.getLastTurn().getId() + 1,
                    getSession().getCurrentUser(),
                    TurnType.WORD);
            gameDAO.insertTurn(selectedGame,newTurn);
            selectedGame.addTurn(newTurn);
        }
        return wordsNotInDictionary;
    }

    public ObservableList<Tile> showPot(Game game) {
        if (game != null)
            return game.getPot();
        return null;
    }
}
