package controllers;

import com.sun.org.apache.xpath.internal.SourceTree;
import enumerations.GameState;
import enumerations.Language;
import enumerations.Role;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.util.List;

public class GameController extends Controller {

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

    public void loadGame(Game game, Role gameMode) {
        if (game == null)
            return;
        game.setBoard(gameDAO.selectFieldsForBoard(game.getBoardType()));
        game.setTurns(gameDAO.selectTurns(game));
        game.setMessages(gameDAO.selectMessages(game));
        game.setGameMode(gameMode);
    }

    @Override
    public void refresh() {
        if (games.contains(getSelectedGame())) {
            Game game = games.get(games.indexOf(getSelectedGame()));
            loadGame(game, getCurrentRole());
            setSelectedGame(game);

            if (game.getTurns().contains(getSelectedTurn())) {
                Turn turn = game.getTurns().get(game.getTurns().indexOf(getSelectedTurn()));
                setSelectedTurn(turn);
            }
        }
    }

    @Override
    public void refill() {
        games.setAll(gameDAO.selectGames());
    }

    public void setPlayerRack(Game game, List<Tile> tiles) {
        game.getCurrentRack().setAll(tiles);
    }

    public void setBoardState(Game game, Turn turn) {
        game.setBoardStateTo(turn);
    }

    public void placeTile(Game game, Field field, Tile tile) {
        game.addPlacedTile(field, tile);
    }

    public void removeTile(Game game, Field field) {
        game.removePlacedTile(field);
    }

    public void sendMessage(Game game, User user, String text) {
        game.sendMessage(user, text);
        gameDAO.insertMessage(game, user, text);
    }

    public boolean isJokerTile(Tile tile) {
        return tile.getCharacter() == '?';
    }

    public ObservableList<Tile> showPot(Game game) {
        if (game != null)
            return game.getPot();
        return null;
    }

    public boolean challenge(Language language, User requester, User receiver, Competition comp) {

        if (isUserInSelectedComp(requester, comp)) {
            if (!this.playingGame(requester, receiver, comp)) {
                if (validInvite(requester, receiver)) {
                    gameDAO.createGame(comp.getId(), requester.getName(), language, receiver.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validInvite(User requester, User receiver) {
        if (!requester.getName().equals(receiver.getName())) return true;
        return false;
    }

    private boolean isUserInSelectedComp(User requester, Competition comp) {
        if (getCompetitionController().isUserInCompetition(requester, comp)) return true;
        return false;
    }


    public boolean playingGame(User challenger, User opponent, Competition comp) {
        getGames();
        for (Game g : games) {
            if (g.getChallenger().equals(challenger) && g.getOpponent().equals(opponent) || (g.getChallenger().equals(opponent) && g.getOpponent().equals(challenger))) {
                if (g.getGameState() != GameState.FINISHED) {
                    if (g.getCompetitionId() == comp.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
