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

    public void insertTurn(Turn turn, Game game) {
        gameDAO.insertTurn(game, turn);
    }

    public boolean isThirdPass(){
        int counter = 0;
        ObservableList<Turn> turns = getSelectedGame().getTurns();
        for ( int n = turns.size()-1; n > turns.size()-3 ; n--){
            System.out.println(turns.get(n).getId());
            if (turns.get(n).getType() == TurnType.PASS){
                counter++;
            }
        }
        if (counter == 2){
            return true;
        } else {
            return false;
        }
    }
    
    public void swapTiles(ObservableList<FieldTileNode> swapTiles, Game game){
        for(FieldTileNode field: swapTiles){
            game.getTurnBuilder().getCurrentRack().remove(field.getTile());
        } 
        game.getTurnBuilder().fillCurrentRack(game.getPot());
        int turnId = game.getLastTurn().getId() + 1;
        Turn newTurn = game.getTurnBuilder().buildTurn(turnId, getSession().getCurrentUser(), TurnType.SWAP);
        insertTurn(newTurn, game);
        
        
    }

    public boolean challenge(Language language, User requester, User receiver, Competition comp) {
        if (isUserInSelectedComp(requester, comp)) {
            if (!this.playingGame(requester, receiver, comp)) {
                if (validInvite(requester, receiver)) {
                    Game game = new Game(0, 0, comp.getId(), requester, receiver, GameState.REQUEST, BoardType.STANDARD, language);
                    games.add(game);
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
        System.out.println(games);
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
