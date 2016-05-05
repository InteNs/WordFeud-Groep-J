package views;

import enumerations.GameState;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Game;
import models.User;

public class GameListView extends View {

    @FXML private Accordion accordion;
    @FXML private TextField filterField;
    @FXML private ListView<Game> myFinishedGamesList;
    @FXML private ListView<Game> allFinishedGamesList;
    @FXML private ListView<Game> myGamesList;
    @FXML private ListView<Game> allGamesList;

    private FilteredList<Game> filteredGames;


    public void refresh() {
    }

    @Override
    public void constructor() {
        filteredGames = new FilteredList<>(gameController.getGames());

        filterField.textProperty().addListener(observable ->
                filteredGames.setPredicate(game ->
                        game.toString().contains(filterField.getText())
                )
        );

        allGamesList.setItems(filteredGames);

        allFinishedGamesList.setItems(filteredGames.filtered(game ->
                game.getGameState() == GameState.FINISHED)
        );

        userController.currentUserProperty().addListener((observable, oldValue, newValue) -> {
            showOwnedGames(newValue);
        });
    }

    private void showOwnedGames(User user) {
        myGamesList.setItems(filteredGames.filtered(game ->
                game.getPlayers().contains(user)
        ));
        myFinishedGamesList.setItems(filteredGames.filtered(game ->
                game.getPlayers().contains(user) && game.getGameState() == GameState.FINISHED
        ));
    }
}
