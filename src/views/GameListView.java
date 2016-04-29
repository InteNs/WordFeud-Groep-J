package views;

import controllers.GameController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Game;
import models.User;
import java.net.URL;
import java.util.ResourceBundle;

public class GameListView extends View implements Initializable {

    @FXML public TextField filterField;
    @FXML private ListView<Game> myGamesList;
    @FXML private ListView<Game> allGamesList;

    private GameController gameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameController = new GameController();
    }

    public void refresh() {
        gameController.setCurrentUser(parent.getCurrentUser());
        gameController.refresh(); // touches database
        myGamesList.setItems(FXCollections.observableArrayList(gameController.getOwnedGames()));
        allGamesList.setItems(FXCollections.observableArrayList(gameController.getGames()));
    }
}
