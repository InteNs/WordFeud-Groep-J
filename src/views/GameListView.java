package views;

import enumerations.GameState;
import enumerations.Role;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Game;

import java.util.function.Predicate;

public class GameListView extends View {

    @FXML private SplitPane lists;
    @FXML private Accordion gameLists;
    @FXML private Accordion compGameLists;
    @FXML private TextField filterField;
    @FXML private ListView<Game> myCompGamesList;
    @FXML private ListView<Game> myGamesList;
    @FXML private ListView<Game> allGamesList;
    @FXML private ListView<Game> allCompGamesList;
    @FXML private TitledPane myCompGamesPane;
    @FXML private TitledPane myGamesPane;
    @FXML private TitledPane allCompGamesPane;
    @FXML private TitledPane allGamesPane;
    @FXML private ChoiceBox<GameState> gameStateBox;
    @FXML private ChoiceBox<Role> userRoleBox;

    private FilteredList<Game> filteredGames;
    private Predicate<Game> filterText, filterComp, filterState, filterUser;
    private Role currentRole;
    private GameState currentState;

    public void refresh() {
        lists.getItems().remove(compGameLists);
        gameStateBox.getSelectionModel().select(GameState.PLAYING);
        userRoleBox.getSelectionModel().select(Role.PLAYER);
    }

    @Override
    public void constructor() {
        filteredGames = new FilteredList<>(gameController.getGames());

        //fill the choiceboxes according to current users roles and gamestates
        gameStateBox.getItems().setAll(GameState.PLAYING, GameState.FINISHED);
        userRoleBox.getItems().setAll(session.getCurrentUser().getRoles().filtered(role ->
                role == Role.OBSERVER || role == Role.PLAYER));

        //define the filters for different lists
        filterText = game ->
                game.toString().toLowerCase().contains(filterField.getText().toLowerCase());
        filterComp = game -> competitionController.getSelectedCompetition() != null
                && competitionController.getSelectedCompetition().getGames().contains(game);
        filterState = game -> game.getGameState() == currentState;
        filterUser = game -> game.getPlayers().contains(session.getCurrentUser());

        //when player changes to observer or player hide and show proper stuff in view
        userRoleBox.setOnAction(event -> {
            currentRole = userRoleBox.getValue();
            gameController.setCurrentRole(currentRole);
            gameController.setSelectedGame(null);
            compGameLists.getPanes().remove(allCompGamesPane);
            gameLists.getPanes().remove(allGamesPane);
            if (currentRole == Role.PLAYER) {
                gameStateBox.getSelectionModel().select(0);
                gameStateBox.setVisible(false);
                gameLists.setExpandedPane(myGamesPane);
                compGameLists.setExpandedPane(myCompGamesPane);
            } else {
                compGameLists.getPanes().add(allCompGamesPane);
                gameLists.getPanes().add(allGamesPane);
                gameStateBox.getSelectionModel().select(1);
                gameStateBox.setVisible(true);
                gameLists.setExpandedPane(allGamesPane);
                compGameLists.setExpandedPane(allCompGamesPane);
            }
        });

        //add listener to gamestatebox that filters the games when changed
        gameStateBox.setOnAction(event -> {
            currentState = gameStateBox.getValue();
            filter();
        });

        //add action to search field and lists
        filterField.textProperty().addListener(observable -> filter());
        myGamesList.setOnMouseClicked(event -> selectGame(myGamesList.getSelectionModel().getSelectedItem()));
        allGamesList.setOnMouseClicked(event -> selectGame(allGamesList.getSelectionModel().getSelectedItem()));
        compGameLists.setOnMouseClicked(event -> selectGame(myCompGamesList.getSelectionModel().getSelectedItem()));
        allCompGamesList.setOnMouseClicked(event -> selectGame(allCompGamesList.getSelectionModel().getSelectedItem()));

        //fill all lists and select default viewing mode
        allGamesList.setItems(filteredGames);
        myGamesList.setItems(filteredGames.filtered(filterUser));
        lists.getItems().remove(compGameLists);
        gameStateBox.getSelectionModel().select(GameState.PLAYING);
        userRoleBox.getSelectionModel().select(Role.PLAYER);

        //when a competition is selected elsewhere(and has games/users, show or hide the lists, and update titles
        competitionController.selectedCompetitionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.getPlayers().isEmpty()) {
                if(!lists.getItems().contains(compGameLists)) lists.getItems().add(compGameLists);
                compGameLists.setExpandedPane(myCompGamesPane);
                allCompGamesPane.setText("Alle spellen binnen: " + newValue.toString());
                allCompGamesList.setItems(filteredGames.filtered(filterComp));
                myCompGamesPane.setText("Mijn spellen binnen: " + newValue.toString());
                myCompGamesList.setItems(filteredGames.filtered(filterComp.and(filterUser))
                );
                lists.setDividerPosition(0, 0.5);
            }
        });

    }

    private void filter() {
        filteredGames.setPredicate(null);
        filteredGames.setPredicate(filterState.and(filterText));
    }

    private void selectGame(Game game) {
        if (game != null) {
            gameController.setSelectedGame(game);
            parent.setContent(parent.gameBoardView);
            parent.setTab(parent.gameControlView);
        }
    }
}
