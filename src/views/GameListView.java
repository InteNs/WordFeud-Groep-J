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

    @FXML
    private TitledPane myGamesPane;
    @FXML
    private Accordion gameLists;
    @FXML
    private TextField filterField;


    @FXML
    private ListView<Game> myFinishedGamesList;
    @FXML
    private ListView<Game> allFinishedGamesList;
    @FXML
    private ListView<Game> myGamesList;
    @FXML
    private ListView<Game> allGamesList;
    @FXML
    private ListView<Game> compGamesList;

    @FXML
    private TitledPane compGamesPane;

    private FilteredList<Game> filteredGames;

    public void refresh() {
    }

    @Override
    public void constructor() {
        filteredGames = new FilteredList<>(gameController.getGames());

        Predicate<Game> filterText = game -> game.toString().toLowerCase().contains(filterField.getText().toLowerCase());
        Predicate<Game> filterComp = game -> competitionController.getSelectedCompetition() != null
                && competitionController.getSelectedCompetition().getGames().contains(game);
        Predicate<Game> filterOwned = game -> game.getPlayers().contains(session.getCurrentUser());
        Predicate<Game> filterFinished = game -> game.getGameState() == GameState.FINISHED;

        allGamesList.setItems(filteredGames);
        allFinishedGamesList.setItems(filteredGames.filtered(filterFinished));
        myGamesList.setItems(filteredGames.filtered(filterOwned));
        myFinishedGamesList.setItems(filteredGames.filtered(filterOwned.and(filterFinished)));

        gameLists.setExpandedPane(myGamesPane);

        competitionController.selectedCompetitionProperty().addListener((observable, oldValue, newValue) -> {
            gameLists.getPanes().remove(compGamesPane);
            if (newValue != null && !newValue.getPlayers().isEmpty()) {
                gameLists.getPanes().add(compGamesPane);
                gameLists.setExpandedPane(compGamesPane);
                compGamesPane.setText("Spellen binnen: " + newValue.toString());
                compGamesList.setItems((filteredGames.filtered(filterComp)));
            }
        });

        filterField.textProperty().addListener(observable ->
                filteredGames.setPredicate(filterText));

        myGamesList.getSelectionModel().selectedItemProperty().addListener((o1, o2, newValue) -> {
            selectGame(newValue);
        });

        allGamesList.getSelectionModel().selectedItemProperty().addListener((o1, o2, newValue) -> {
            selectGame(newValue);
        });
    }

    private void selectGame(Game game) {
        if (game != null) {
            gameController.setSelectedGame(game);
            parent.setContent(parent.gameBoardView);
            parent.setTab(parent.gameControlView);
        }
    }
}
