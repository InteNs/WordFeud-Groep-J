package views;


import enumerations.Role;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import models.Game;
import models.Message;
import models.Tile;
import models.Turn;
import views.components.ChatCell;

public class gameControlView extends View {

    @FXML private ListView<Message> chatList;
    @FXML private ListView<Turn> turnList;
    @FXML private HBox buttonBox;
    @FXML private Button playButton;
    @FXML private Button extraFunctionsButton;
    @FXML private ContextMenu contextMenu;
    @FXML private Label potLabel;
    @FXML private Spinner<Turn> turnSpinner;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;
    @FXML private TextArea chatTextArea;
    @FXML private Tab chatTab;
    @FXML private Tab turnTab;
    @FXML private TabPane gameTabs;

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
        chatList.setCellFactory(param -> new ChatCell(param, session.getCurrentUser()));
        chatList.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);

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

        gameController.currentRoleProperty().addListener((observable, oldValue, newValue) ->
                setTabs(newValue)
        );

        gameController.selectedGameProperty().addListener((o, v, newValue) -> {
            if (newValue == null) return;
            chatList.setItems(newValue.getMessages());
            chatList.scrollTo(chatList.getItems().size());
            turnList.setItems(newValue.getTurns());
            turnSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(newValue.getTurns()));
            selectTurn(newValue.getLastTurn());
            turnList.scrollTo(newValue.getLastTurn());
            setPotLabel(newValue);
            setTabs(gameController.getCurrentRole());
        });

        gameTabs.widthProperty().addListener((o, v, newValue) ->
                gameTabs.setTabMinWidth((double) newValue / 2 - 23));
    }

    private void selectTurn(Turn newValue) {
        gameController.setSelectedTurn(newValue);
        setPotLabel(gameController.getSelectedGame());
        turnSpinner.getValueFactory().setValue(newValue);
        turnList.getSelectionModel().select(newValue);
    }

    private void setPotLabel(Game newValue) {
        potLabel.setText("Aantal letters in pot: " + newValue.getPot().size());
    }

    private void setTabs(Role gameMode) {
        if(gameMode == Role.PLAYER) {
            disableTurnControls(true);
            disableGameControls(false);
            gameTabs.getSelectionModel().select(chatTab);
        } else if(gameMode == Role.OBSERVER) {
            disableTurnControls(false);
            disableGameControls(true);
            gameTabs.getSelectionModel().select(turnTab);
        }
    }

    @FXML
    public void showPot() {
        ObservableList<Tile> tiles = gameController.showPot(gameController.getSelectedGame());
        if (tiles != null) {
            new potView(tiles, resourceFactory);
        }
    }

    private void disableGameControls(boolean disable) {
        buttonBox.setDisable(disable);
        chatTab.setDisable(disable);

    }

    private void disableTurnControls(boolean disable) {
        turnTab.setDisable(disable);
        turnSpinner.setDisable(disable);
    }

    @FXML
    public void sendMessage() {
        if (!chatTextArea.getText().trim().isEmpty()) {
            gameController.sendMessage(gameController.getSelectedGame(), session.getCurrentUser(), chatTextArea.getText());
            chatTextArea.clear();
        }
    }
    public void showJokers( ) {
        parent.getGameBoardView().showJokers();
    }
    
    public void shuffle(){
        parent.getGameBoardView().shuffleRack();
    }

    public void playWord() {
        boolean aap = gameController.playWord(gameController.getSelectedGame()).isEmpty();
        if (aap) {
            gameController.loadGame(gameController.getSelectedGame(), gameController.getCurrentRole());
        }
        gameController.setBoardState(gameController.getSelectedGame(), gameController.getSelectedGame().getLastTurn());
        parent.getGameBoardView().displayGameBoard(gameController.getSelectedGame(), gameController.getSelectedGame().getLastTurn());
        parent.getGameBoardView().displayPlayerRack(gameController.getSelectedGame(), gameController.getSelectedGame().getLastTurn());
    }
}
