package views;

import enumerations.GameState;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Game;

public class GameListView extends View {

    @FXML private Accordion accordion;
    @FXML private TextField filterField;
    @FXML private ListView<Game> myFinishedGamesList;
    @FXML private ListView<Game> allFinishedGamesList;
    @FXML private ListView<Game> myGamesList;
    @FXML private ListView<Game> allGamesList;

    public void refresh() {
        gameController.refresh(); // touches database
        myGamesList.getItems().setAll(gameController.getOwnedGames());
        myFinishedGamesList.getItems().setAll(gameController.getOwnedGames(GameState.FINISHED));
        allGamesList.getItems().setAll(gameController.getGames());
        allFinishedGamesList.getItems().setAll(gameController.getGames(GameState.FINISHED));
    }

    @Override
    public void constructor() {

    }
}
