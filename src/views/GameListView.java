package views;

import enumerations.GameState;
import enumerations.Role;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Competition;
import models.Game;
import views.components.GameCell;

import java.util.Objects;
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
    private Predicate<Game> filterText, filterComp, filterUser;

    public void refresh() {
        setChoiceBoxes();
        setViewingMode(false, null);
        applyViewingMode(false);
        showCompGames(competitionController.getSelectedCompetition(), false);
        myGamesList.refresh();
        allCompGamesList.refresh();
        allGamesList.refresh();
        myCompGamesList.refresh();
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        filteredGames = new FilteredList<>(gameController.getGames());

        gameStateBox.getItems().setAll(GameState.PLAYING, GameState.FINISHED, GameState.RESIGNED);
        setChoiceBoxes();

        //define the filters for different lists
        filterText = game ->
                game.toString().toLowerCase().contains(filterField.getText().toLowerCase());
        filterComp = game -> competitionController.getSelectedCompetition() != null
                && competitionController.getSelectedCompetition().getGames().contains(game);
        filterUser = game ->
                game.getPlayers().contains(session.getCurrentUser());

        //when player changes to observer or player hide and show proper stuff in view

        userRoleBox.setOnAction(event -> applyViewingMode(true));

        //add listener to gameStateBox and search field that filters the games when changed
        gameStateBox.setOnAction(event -> filter(gameStateBox.getValue()));
        filterField.textProperty().addListener(observable -> filter(gameStateBox.getValue()));

        //add action listeners to lists
        myGamesList.setOnMouseClicked(event -> selectGame(myGamesList.getSelectionModel().getSelectedItem()));
        allGamesList.setOnMouseClicked(event -> selectGame(allGamesList.getSelectionModel().getSelectedItem()));
        myCompGamesList.setOnMouseClicked(event -> selectGame(myCompGamesList.getSelectionModel().getSelectedItem()));
        allCompGamesList.setOnMouseClicked(event -> selectGame(allCompGamesList.getSelectionModel().getSelectedItem()));

        //fill all lists and select default viewing mode
        allGamesList.setItems(filteredGames);
        myGamesList.setItems(filteredGames.filtered(filterUser));

        applyViewingMode(true);
        setViewingMode(true, null);
        showCompGames(null, false);

        //set custom cell
        allGamesList.setCellFactory(param -> new GameCell(session.getCurrentUser()));
        myGamesList.setCellFactory(param -> new GameCell(session.getCurrentUser()));
        myCompGamesList.setCellFactory(param -> new GameCell(session.getCurrentUser()));
        allCompGamesList.setCellFactory(param -> new GameCell(session.getCurrentUser()));

        //when a competition is selected elsewhere(and has games/users, show or hide the lists, and update titles
        competitionController.selectedCompetitionProperty()
                .addListener((o, oldValue, newValue) -> {
                    if (!Objects.equals(newValue, oldValue)) showCompGames(newValue, true);
                });

    }

    private void applyViewingMode(boolean isNew) {
        gameController.setCurrentRole(userRoleBox.getValue());
        if (isNew)
            showAllPanes(false);
        if (userRoleBox.getValue() == Role.PLAYER) {
            showAllPanes(false);
            gameStateBox.setVisible(false);
            gameStateBox.getSelectionModel().select(GameState.PLAYING);
            gameLists.setExpandedPane(myGamesPane);
            compGameLists.setExpandedPane(myCompGamesPane);
        } else if (userRoleBox.getValue() == Role.OBSERVER && isNew) {
            showAllPanes(true);
            gameStateBox.getSelectionModel().select(GameState.FINISHED);
            gameStateBox.setVisible(true);
            gameLists.setExpandedPane(allGamesPane);
            compGameLists.setExpandedPane(allCompGamesPane);
        }
    }

    private void showAllPanes(boolean showAll) {
        if (showAll) {
            compGameLists.getPanes().add(allCompGamesPane);
            gameLists.getPanes().add(allGamesPane);
        } else {
            compGameLists.getPanes().remove(allCompGamesPane);
            gameLists.getPanes().remove(allGamesPane);
        }
    }

    private void showCompGames(Competition newValue, boolean isNew) {
        if (newValue != null && !newValue.getPlayers().isEmpty()) {
            if (!lists.getItems().contains(compGameLists))
                lists.getItems().add(compGameLists);

            allCompGamesPane.setText("Alle spellen binnen: " + newValue.toString());
            allCompGamesList.setItems(filteredGames.filtered(filterComp));
            myCompGamesPane.setText("Mijn spellen binnen: " + newValue.toString());
            myCompGamesList.setItems(filteredGames.filtered(filterComp.and(filterUser)));
            if (isNew) {
                lists.setDividerPosition(0, 0.5);
                compGameLists.setExpandedPane(myCompGamesPane);
            }
        } else lists.getItems().remove(compGameLists);
    }

    private void setChoiceBoxes() {
        ObservableList<Role> roles = session.getCurrentUser().getRoles().filtered(role ->
                role == Role.OBSERVER || role == Role.PLAYER).sorted();
        if (userRoleBox.getItems().size() != roles.size()) {
            Role previous = userRoleBox.getValue();
            userRoleBox.getItems().setAll(roles);
            setViewingMode(false, previous);
        }
    }

    private void setViewingMode(boolean firstTime, Role select) {
        if (firstTime) {
            if (session.getCurrentUser().hasRole(Role.PLAYER))
                userRoleBox.getSelectionModel().select(Role.PLAYER);
            else
                userRoleBox.getSelectionModel().select(Role.OBSERVER);
        } else {
            if (!session.getCurrentUser().hasRole(Role.OBSERVER))
                userRoleBox.getSelectionModel().select(Role.PLAYER);
            if (!session.getCurrentUser().hasRole(Role.PLAYER))
                userRoleBox.getSelectionModel().select(Role.OBSERVER);
            if (select != null) userRoleBox.getSelectionModel().select(select);
        }

    }

    private void filter(GameState state) {
        filteredGames.setPredicate(filterText.and(game -> game.getGameState() == state));
    }

    private void selectGame(Game game) {
        if (game != null) {
            gameController.setSelectedGame(game);
            gameController.loadGame(gameController.getSelectedGame(),gameController.getCurrentRole());
            gameController.setSelectedTurn(game.getLastTurn());
            parent.reload();
            parent.setContent(parent.gameBoardView);
            parent.setTab(parent.gameControlView);
        }
    }

    public void setVisible(boolean visible){
        this.setVisible(visible);
    }
}
