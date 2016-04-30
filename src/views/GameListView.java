package views;

import com.sun.javafx.scene.control.skin.TitledPaneSkin;
import controllers.GameController;
import enumerations.GameState;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import models.Game;
import models.User;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameListView extends View implements Initializable {

    @FXML private Accordion accordion;
    @FXML private TextField filterField;
    @FXML private ListView<Game> myFinishedGamesList;
    @FXML private ListView<Game> allFinishedGamesList;
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
        myGamesList.getItems().setAll(gameController.getOwnedGames());
        myFinishedGamesList.getItems().setAll(gameController.getOwnedGames(GameState.FINISHED));
        allGamesList.getItems().setAll(gameController.getGames());
        allFinishedGamesList.getItems().setAll(gameController.getGames(GameState.FINISHED));
    }
}
