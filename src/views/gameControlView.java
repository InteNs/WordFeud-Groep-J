package views;


import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.Game;
import models.Message;
import models.Tile;
import models.Turn;
import views.components.ChatCell;

public class gameControlView extends View {

    @FXML private ListView<Message> chatList;
    @FXML private ListView<Turn> turnList;
    @FXML private Button playButton;
    @FXML private Button extraFunctionsButton;
    @FXML private ContextMenu contextMenu;
    @FXML private Label potLabel;
    @FXML private Spinner<Turn> turnSpinner;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;
    @FXML private TextArea chatTextArea;
    @FXML private Tab chatTab;
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

        gameController.selectedGameProperty().addListener((o, v, newValue) -> {
            if (newValue == null) return;
            chatList.setItems(newValue.getMessages());
            chatList.scrollTo(chatList.getItems().size());
            turnList.setItems(newValue.getTurns());
            turnSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(newValue.getTurns()));
            selectTurn(newValue.getLastTurn());
            turnList.scrollTo(newValue.getLastTurn());
            setButtonDisabled(newValue);
            setPotLabel(newValue);
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

    private void setButtonDisabled(Game newValue) {
        chatTab.setDisable(!newValue.hasPlayer(session.getCurrentUser()));
    }

    @FXML
    public void showPot() {
        ObservableList<Tile> tiles = gameController.showPot(gameController.getSelectedGame());
        if (tiles != null) {
            new potView(tiles, resourceFactory);
        }
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
}
