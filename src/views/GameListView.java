package views;

import enumerations.GameState;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import models.Game;
import models.User;

public class GameListView extends View {

    @FXML private TitledPane myGamesPane;
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
                        game.toString().toLowerCase().contains(filterField.getText().toLowerCase())
                )
        );

        allGamesList.setItems(filteredGames);

        allFinishedGamesList.setItems(filteredGames.filtered(game ->
                game.getGameState() == GameState.FINISHED)
        );

        session.currentUserProperty().addListener((observable, oldValue, newValue) -> {
            showOwnedGames(newValue);
            accordion.setExpandedPane(myGamesPane);
        });

        myGamesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                gameController.setSelectedGame(newValue);
                parent.showGameBoardView();
            }
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
