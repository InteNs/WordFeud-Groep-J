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
        showGame(gameController.getSelectedGame());
    }

    @Override
    public void constructor() {
        chatList.setCellFactory(param -> new ChatCell(param, session.getCurrentUser()));
        chatList.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);


        turnSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        turnList.getSelectionModel().selectedItemProperty().addListener((o, v, newValue) ->
                selectTurn(newValue)
        );

        extraFunctionsButton.setOnMouseClicked(event ->
                contextMenu.show(extraFunctionsButton, event.getScreenX(), event.getScreenY())
        );

        turnSpinner.valueProperty().addListener((o, v, newValue) ->
                selectTurn(newValue)
        );

        turnSpinner.setOnMousePressed(event -> turnList.scrollTo(turnSpinner.getValue()));

        gameController.currentRoleProperty().addListener((observable, oldValue, newValue) -> {
            if (gameController.getSelectedTurn() != null)
                setTabs(newValue, gameController.getSelectedTurn().getUser());
        });

        gameController.selectedGameProperty().addListener((o, v, newValue) ->
                showGame(newValue)
        );
    }

    private void showGame(Game game) {
        if (game == null) return;
        chatList.setItems(game.getMessages());
        chatList.scrollTo(chatList.getItems().size());
        turnList.setItems(game.getTurns());
        turnSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(game.getTurns()));
        selectTurn(game.getLastTurn());
        turnList.scrollTo(game.getLastTurn());
        setPotLabel(game);
        setTabs(gameController.getCurrentRole(), game.getLastTurn().getUser());
        disableChat(!game.getPlayers().contains(session.getCurrentUser()));
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
            new potView(tiles, resourceFactory);
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

    public void clear() {
        parent.getGameBoardView().clear();
    }

    public void playWord() {
        boolean aap = gameController.playWord(gameController.getSelectedGame()).isEmpty();
        if (aap) {
            gameController.loadGame(gameController.getSelectedGame(), gameController.getCurrentRole());
        }
        gameController.setBoardState(gameController.getSelectedGame(), gameController.getSelectedGame().getLastTurn());
        parent.getGameBoardView().displayGameBoard(gameController.getSelectedGame(), gameController.getSelectedGame().getLastTurn());
        parent.getGameBoardView().displayPlayerRack(gameController.getSelectedGame(), gameController.getSelectedGame().getLastTurn());
        selectTurn(gameController.getSelectedGame().getLastTurn());
    }
}
