package views;

import enumerations.GameState;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import models.Game;

import java.util.function.Predicate;

public class GameListView extends View {

    @FXML private TitledPane myGamesPane;
    @FXML private Accordion accordion;
    @FXML private TextField filterField;


    @FXML private ListView<Game> myFinishedGamesList;
    @FXML private ListView<Game> allFinishedGamesList;
    @FXML private ListView<Game> myGamesList;
    @FXML private ListView<Game> allGamesList;

    private FilteredList<Game> filteredGames;
    private Predicate<Game> filterText, filterUser, filterOwned, filterFinished;

    public void refresh() {
    }

    @Override
    public void constructor() {
        filteredGames = new FilteredList<>(gameController.getGames());

        filterText      = game -> game.toString().toLowerCase().contains(filterField.getText().toLowerCase());
        filterUser      = game -> userController.getSelectedUser() == null
                                    || game.getPlayers().contains(userController.getSelectedUser());
        filterOwned     = game -> game.getPlayers().contains(session.getCurrentUser());
        filterFinished  = game -> game.getGameState() == GameState.FINISHED;

        allGamesList.setItems           (filteredGames);
        allFinishedGamesList.setItems   (filteredGames.filtered(filterFinished));

        session.currentUserProperty().addListener(e -> {
            myGamesList.setItems        (filteredGames.filtered(filterOwned));
            myFinishedGamesList.setItems(filteredGames.filtered(filterOwned.and(filterFinished)));
            accordion.setExpandedPane(myGamesPane);
        });

        filterField.textProperty().addListener(e -> setGlobalFilter(filteredGames));
        userController.selectedUserProperty().addListener(e -> setGlobalFilter(filteredGames));

        myGamesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectGame(newValue);
        });

        allGamesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectGame(newValue);
        });
    }

    private void selectGame(Game game){
        if (game != null) {
            gameController.setSelectedGame(game);
            parent.showGameBoardView();
            parent.showGameControlView();
        }
    }

    private void setGlobalFilter( FilteredList<Game> list) {
        list.setPredicate(filterText);
    }
}
