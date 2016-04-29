package views;

import controllers.GameController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Game;
import models.Message;
import models.User;
import java.net.URL;
import java.util.ResourceBundle;

public class GameList implements Initializable {

    @FXML private ListView<Game> myGamesList;
    @FXML private ListView<Game> allGamesList;

    private GameController gameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO GET THIS OR BE GIVEN THIS
        User currentUser = new User("jager684");
        gameController = new GameController(currentUser);
    }

    public void refresh() {
        myGamesList.setItems(FXCollections.observableArrayList(gameController.getOwnedGames()));
        allGamesList.setItems(FXCollections.observableArrayList(gameController.getGames()));
    }
}
