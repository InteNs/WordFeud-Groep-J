package views;


import controllers.SessionController;
import enumerations.Role;
import enumerations.WordStatus;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.*;
import views.components.ChatCell;
import views.components.FieldTileNode;
import views.subviews.SubmitWordView;
import views.subviews.potView;

import java.util.ArrayList;
import java.util.Objects;


public class gameControlView extends View {

    @FXML private ListView<Message> chatList;
    @FXML private ListView<Turn> turnList;
    @FXML private Button playButton;
    @FXML private Button passButton;
    @FXML private Button swapButton;
    @FXML private Button resignButton;
    @FXML private Button shuffleButton;
    @FXML private Button clearButton;
    @FXML private Button extraFunctionsButton;
    @FXML private Button potButton;
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
        if (gameController.getSelectedGame() != null && !gameController.getSelectedGame().getTurns().isEmpty()) {
            showGame(gameController.getSelectedGame(), false);
        }
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
        turnSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(newGame.getTurns()));
        turnList.setItems(newGame.getTurns());
        if (newGame.getGameMode() == Role.PLAYER) selectTurn(newGame.getLastTurn());
        else if (newGame.getGameMode() == Role.OBSERVER) {
            if (newGame.getTurns().contains(gameController.getSelectedTurn())) {
                selectTurn(gameController.getSelectedTurn());
                //turnList.scrollTo(gameController.getSelectedTurn());
            } else {
                selectTurn(newGame.getLastTurn());
                turnList.scrollTo(newGame.getLastTurn());
            }
        }

        setPotLabel(newGame);
        setTabs(gameController.getCurrentRole(), newGame.getLastTurn().getUser());
        disableChat(gameController.getCurrentRole() == Role.OBSERVER
                || !newGame.getPlayers().contains(session.getCurrentUser()));
        if (newGame.getPot().size() < 7) {
            swapButton.setDisable(true);
        }
        if (gameController.getSelectedGame().getPot().size() < 1) {
            potButton.setDisable(true);
            System.out.println(gameController.getSelectedGame().getPot());
        }
        if (gameController.getSelectedGame().getPot().size() > 0) {
            potButton.setDisable(false);
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
    public void sendMessageAction(Event event) {
        if (event instanceof KeyEvent) {
            if (((KeyEvent) event).getCode() != KeyCode.ENTER) return;
            event.consume();
        }
        if (!chatTextArea.getText().trim().isEmpty()) {
            gameController.sendMessage(
                    gameController.getSelectedGame(),
                    session.getCurrentUser(),
                    chatTextArea.getText());
            chatTextArea.clear();
        }
    }

    @FXML
    public void showJokers() {
        parent.getGameBoardView().showJokers();
    }

    @FXML
    public void shuffle() {
        parent.getGameBoardView().shuffleRack();
    }

    @FXML
    public void clearBoard() {
        parent.getGameBoardView().clearBoard();
    }

    @FXML
    public void showPot() {
        ObservableList<Tile> tiles = gameController.showPot(gameController.getSelectedGame());
        if (gameController.showPot(gameController.getSelectedGame()).size() > 0) {
            if (tiles != null) {
                new potView(tiles, resourceFactory, parent);
            }
        }
    }

    @FXML
    public void playWord() {
        ArrayList<String> words = gameController.playWord(gameController.getSelectedGame());
        ArrayList<String> existingWords;
        ArrayList<Word> submittedWords = new ArrayList<>();
        words = wordController.getWordsList();
        if (words == null) return;
        if (words.isEmpty())
            parent.reload();
        else {
            existingWords  = wordController.existingWords(words);
            SubmitWordView submitWordView = new SubmitWordView(words, existingWords, parent);
            words.clear();
            existingWords.clear();
            for (String w : submitWordView.submitWords())
                submittedWords.add(wordController.createWord(w.toLowerCase(), gameController.getSelectedGame().getLanguage().toString()));
            wordController.submitWords(submittedWords);
        }
    }


    @FXML
    public void pass() {
        clearBoard();
        gameController.passTurn(gameController.getSelectedGame());
        parent.reload();
    }

    @FXML
    public void swapTiles() {
        clearBoard();
        ObservableList<FieldTileNode> selectedTiles = new SwapTileView(parent, resourceFactory)
                .swapTiles(gameController.getSelectedGame().getTurnBuilder().getCurrentRack());
        if (selectedTiles != null) {
            gameController.swapTiles(selectedTiles, gameController.getSelectedGame());
        }
        parent.reload();
    }

    @FXML
    public void resign() {
        clearBoard();
        gameController.resign(gameController.getSelectedGame());
        parent.reload();
    }
}
