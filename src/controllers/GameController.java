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
        if (fetchedTurns == null || fetchedMessages == null) fetch();
        if (game.getEmptyGameBoard() == null)
            game.setBoard(gameDAO.selectFieldsForBoard(game.getBoardType()));
        if (!game.getTurns().equals(fetchedTurns))
            game.setTurns(fetchedTurns);
        if (!(game.getMessages().size() == fetchedMessages.size()))
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
                setSelectedTurn(turn);
                game.setBoardStateTo(turn, getSessionController().getCurrentUser());
            }
            game.setTurnBuilder(previousTurnBuilder);
        }
    }

    @Override
    public void refill() {
        if (!games.equals(fetchedGames))
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
        ArrayList<String> wordsNotInDictionary =
                gameDAO.selectWords(selectedGame, selectedGame.getTurnBuilder().getWordsFoundThisTurn())
                        .stream()
                        .filter(pair -> !pair.getValue())
                        .map(Pair::getKey)
                        .collect(Collectors.toCollection(ArrayList::new));

        if (wordsNotInDictionary.size() == 0) {
            selectedGame.getTurnBuilder().fillCurrentRack(selectedGame.getPot());
            insertTurn(selectedGame,TurnType.WORD);
        }
        return wordsNotInDictionary;
    }

    private void checkForEndGame(Game selectedGame){
        switch (selectedGame.getLastTurn().getType()){
            case PASS:
                if (isThirdPass(selectedGame)){
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
                        && selectedGame.getPot().isEmpty()){
                    buildEndTurns(selectedGame);
                    gameDAO.updateGameState(GameState.FINISHED, selectedGame);
                }
                break;
            default: break;
        }
    }

    private void buildEndTurns(Game selectedGame){
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

    public void passTurn(Game selectedGame){
        insertTurn(selectedGame,TurnType.PASS);
    }

    public void resign(Game selectedGame) {
       insertTurn(selectedGame,TurnType.RESIGN);
    }

    private void insertTurn(Game selectedGame, TurnType turnType) {
        Turn newTurn = selectedGame.getTurnBuilder().buildTurn(
                selectedGame.getLastTurnNumber(),
                getSessionController().getCurrentUser(), turnType
        );
        gameDAO.insertTurn(selectedGame, newTurn);
        selectedGame.addTurn(newTurn);
        checkForEndGame(selectedGame);
    }

    private boolean isThirdPass(Game selectedGame){
        return selectedGame.getTurns().get(selectedGame.getTurns().size() - 2).getType() == TurnType.PASS
                && selectedGame.getTurns().get(selectedGame.getTurns().size() - 3).getType() == TurnType.PASS;
    }
    
    public void swapTiles(ObservableList<FieldTileNode> swapTiles, Game selectedGame){
        for(FieldTileNode field: swapTiles){
            selectedGame.getTurnBuilder().getCurrentRack().remove(field.getTile());
        }
        selectedGame.getTurnBuilder().fillCurrentRack(selectedGame.getPot());
        insertTurn(selectedGame,TurnType.SWAP);
    }

    public boolean challenge(Language language, User requester, User receiver, Competition comp) {
        if (isUserInSelectedComp(requester, comp)) {
            if (!this.playingGame(requester, receiver, comp)) {
                if (validInvite(requester, receiver)) {
                    Game game = new Game(
                            0, 0, comp.getId(),
                            requester,
                            receiver,
                            GameState.REQUEST,
                            BoardType.STANDARD,
                            language
                    );
                    games.add(game);
                    gameDAO.createGame(comp.getId(), requester.getName(), language, receiver.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validInvite(User requester, User receiver) {
        return !requester.getName().equals(receiver.getName());
    }

    private boolean isUserInSelectedComp(User requester, Competition comp) {
        return getCompetitionController().isUserInCompetition(requester, comp);
    }

    private boolean playingGame(User challenger, User opponent, Competition comp) {
        for (Game g : games)
            if (g.getChallenger().equals(challenger) && g.getOpponent().equals(opponent)
                    || (g.getChallenger().equals(opponent) && g.getOpponent().equals(challenger)))
                if (g.getGameState() != GameState.FINISHED)
                    if (g.getCompetitionId() == comp.getId())
                        return true;
        return false;
    }
}
