package views;


import controllers.ControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Game;
import models.Message;
import models.Turn;

public class gameControlView extends View {

    @FXML
    private Tab root;
    @FXML
    private ListView<Message> chatList;
    @FXML
    private ListView<Turn> turnList;
    @FXML
    private Button playButton;
    @FXML
    private Button extraFunctionsButton;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private Label potLabel;
    @FXML
    private Spinner<Turn> turnSpinner;
    @FXML
    private Label player1ScoreLabel;
    @FXML
    private Label player2ScoreLabel;
    @FXML
    private TextField chatTextField;
    @FXML
    private Button sendMessage;
    @FXML
    private Tab chatTab;


    @Override
    public void refresh() {

    }

    @Override
    public void constructor() {
        turnList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            gameController.setSelectedTurn(newValue);
            turnSpinner.getValueFactory().setValue(newValue);
        });

        extraFunctionsButton.setOnMouseClicked(event -> {
            contextMenu.show(extraFunctionsButton, event.getScreenX(), event.getScreenY());
        });

        gameController.selectedGameProperty().addListener((observable, oldValue, newValue) -> {
            chatList.setItems(newValue.getMessages());
            turnList.setItems(newValue.getTurns());
            turnSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(newValue.getTurns()));
            setButtonDisabled(newValue);
        });

        turnSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            gameController.setSelectedTurn(newValue);
        });

        sendMessage.setOnMouseClicked(event -> {
            if (!chatTextField.getText().trim().isEmpty()) {
                gameController.sendMessage(gameController.getSelectedGame(),userController.getSession().getCurrentUser(), chatTextField.getText());
                chatTextField.setText("");
            }
        });

    }

    private void setButtonDisabled(Game newValue) {
        if (!newValue.hasPlayer(session.getCurrentUser())){
            chatTab.setDisable(true);
        }
    }
}
