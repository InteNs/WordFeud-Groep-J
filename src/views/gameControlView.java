package views;


import enumerations.Role;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.*;
import views.components.ChatCell;
import views.components.FieldTileNode;
import views.subviews.potView;
import java.util.Objects;


public class gameControlView extends View {

    @FXML private ListView<Message> chatList;
    @FXML private ListView<Turn> turnList;
    @FXML private HBox buttonBox;
    @FXML private Button playButton;
    @FXML private Button passButton;
    @FXML private Button swapButton;
    @FXML private Button resignButton;
    @FXML private Button shuffleButton;
    @FXML private Button clearButton;
    @FXML private Button extraFunctionsButton;
    @FXML private ContextMenu contextMenu;
    @FXML private Label potLabel;
    @FXML private Spinner<Turn> turnSpinner;
    @FXML private Label challengerLabel;
    @FXML private Label opponentLabel;
    @FXML private TextArea chatTextArea;
    @FXML private VBox chatBox;
    @FXML private StackPane turnChat;

    @Override
    public void refresh() {
        if (gameController.getSelectedGame() != null && !gameController.getSelectedGame().getTurns().isEmpty())
            showGame(gameController.getSelectedGame(), false);
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        chatList.setCellFactory(param -> new ChatCell(param, session.getCurrentUser()));
        chatList.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);


        turnSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        turnList.getSelectionModel().selectedItemProperty()
                .addListener((o, oldValue, newValue) -> {
                    if (!Objects.equals(oldValue, newValue) && newValue != null)
                        selectTurn(newValue);
                });

        extraFunctionsButton.setOnMouseClicked(event ->
                contextMenu.show(extraFunctionsButton, event.getScreenX(), event.getScreenY())
        );

        turnSpinner.valueProperty().addListener((o, oldValue, newValue) -> {
            if (newValue != null && oldValue != newValue && !newValue.toString().equals("")) selectTurn(newValue);
        });

        turnSpinner.setOnMousePressed(event -> turnList.scrollTo(turnSpinner.getValue()));

        gameController.currentRoleProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (gameController.getSelectedTurn() != null)
                        setTabs(newValue, gameController.getSelectedTurn().getUser());
                });

        gameController.selectedGameProperty()
                .addListener((o, oldValue, newValue) -> {
                    if (!Objects.equals(oldValue, newValue) && newValue != null)
                        gameController.loadGame(newValue, gameController.getCurrentRole());
                        showGame(newValue, true);
                });
    }

    private void showGame(Game newGame, boolean isNew) {
        chatList.setItems(newGame.getMessages());
        if (isNew) chatList.scrollTo(chatList.getItems().size());
        turnList.setItems(newGame.getTurns());
        turnSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(newGame.getTurns()));
        if (newGame.getGameMode() == Role.PLAYER) selectTurn(newGame.getLastTurn());
        else if (newGame.getGameMode() == Role.OBSERVER) {
            if (newGame.getTurns().contains(gameController.getSelectedTurn())) {
                selectTurn(gameController.getSelectedTurn());
                //turnList.scrollTo(gameController.getSelectedTurn());
            }
            else {
                selectTurn(newGame.getLastTurn());
                turnList.scrollTo(newGame.getLastTurn());
            }
        }

        setPotLabel(newGame);
        setTabs(gameController.getCurrentRole(), newGame.getLastTurn().getUser());
        disableChat(!newGame.getPlayers().contains(session.getCurrentUser()));
        if (newGame.getPot().size() < 7) {
            swapButton.setDisable(true);
        }
    }

    private void selectTurn(Turn newValue) {
        if (newValue == null) return;
        gameController.setSelectedTurn(newValue);
        setPotLabel(gameController.getSelectedGame());
        turnSpinner.getValueFactory().setValue(newValue);
        turnList.getSelectionModel().select(newValue);
        Game game = gameController.getSelectedGame();
        challengerLabel.setText(game.getChallenger() + ": " + game.getScore(game.getChallenger(), newValue));
        opponentLabel.setText(game.getOpponent() + ": " + game.getScore(game.getOpponent(), newValue));
        if (game.getOpponent().equals(newValue.getUser())) {
            if (gameController.getCurrentRole() == Role.PLAYER)
                highLight(challengerLabel);
            if (gameController.getCurrentRole() == Role.OBSERVER)
                highLight(opponentLabel);
        } else {
            if (gameController.getCurrentRole() == Role.PLAYER)
                highLight(opponentLabel);
            if (gameController.getCurrentRole() == Role.OBSERVER)
                highLight(challengerLabel);
        }
    }

    private void highLight(Label label) {
        if (label == challengerLabel) {
            challengerLabel.getStyleClass().setAll("current-player-label");
            opponentLabel.getStyleClass().clear();
        } else if (label == opponentLabel) {
            opponentLabel.getStyleClass().setAll("current-player-label");
            challengerLabel.getStyleClass().clear();
        }
    }

    private void setPotLabel(Game newValue) {
        potLabel.setText("Aantal letters in pot: " + newValue.getPot().size());
    }

    private void setTabs(Role gameMode, User currentTurnUser) {
        if (gameMode == Role.PLAYER) {
            disableTurnControls(true);
            disableGameControls(currentTurnUser.equals(session.getCurrentUser()), false);
        } else if (gameMode == Role.OBSERVER) {
            disableTurnControls(false);
            disableGameControls(true, true);
        }
    }

    @FXML
    public void showPot() {
        ObservableList<Tile> tiles = gameController.showPot(gameController.getSelectedGame());
        if (tiles != null) {
            new potView(tiles, resourceFactory, parent);
        }
    }

    private void disableGameControls(boolean disable, boolean includeRackControls) {
        playButton.setDisable(disable);
        passButton.setDisable(disable);
        swapButton.setDisable(disable);
        resignButton.setDisable(disable);
        shuffleButton.setDisable(includeRackControls);
        clearButton.setDisable(includeRackControls);
    }

    private void disableChat(boolean disable) {
        turnChat.getChildren().remove(chatBox);
        if (!disable) turnChat.getChildren().add(chatBox);
    }

    private void disableTurnControls(boolean disable) {
        turnChat.getChildren().remove(turnList);
        if (!disable) turnChat.getChildren().add(turnList);
        turnSpinner.setDisable(disable);
    }

    @FXML
    public void sendMessage() {
        if (!chatTextArea.getText().trim().isEmpty()) {
            gameController.sendMessage(gameController.getSelectedGame(), session.getCurrentUser(), chatTextArea.getText());
            chatTextArea.clear();
        }
    }

    public void showJokers() {
        parent.getGameBoardView().showJokers();
    }

    public void shuffle() {
        parent.getGameBoardView().shuffleRack();
    }

    public void clearBoard() {
        parent.getGameBoardView().clearBoard();
    }

    public void playWord() {
        boolean aap = gameController.playWord(gameController.getSelectedGame()).isEmpty();
        if (aap) {
            gameController.loadGame(gameController.getSelectedGame(), gameController.getCurrentRole());
        }
        selectTurn(gameController.getSelectedGame().getLastTurn());
    }

    @FXML
    public void pass() {
        clearBoard();
        gameController.passTurn(gameController.getSelectedGame());
        selectTurn(gameController.getSelectedGame().getLastTurn());
    }
    
    public void swapTiles() {
         clearBoard();
         ObservableList<Tile> currentRack = gameController.getSelectedGame().getTurnBuilder().getCurrentRack();
         SwapTileView swapTileView = new SwapTileView(resourceFactory);
         ObservableList<FieldTileNode> selectedTiles = swapTileView.swapTiles(currentRack);
         if(selectedTiles != null){
            gameController.swapTiles(selectedTiles, gameController.getSelectedGame());
            selectTurn(gameController.getSelectedGame().getLastTurn());
         }
    }


    public void resign(){
        clearBoard();
        gameController.resign(gameController.getSelectedGame());
        selectTurn(gameController.getSelectedGame().getLastTurn());
    }
}
