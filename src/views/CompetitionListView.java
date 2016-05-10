package views;

import controllers.CompetitionController;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Competition;
import models.User;

public class CompetitionListView extends View {

    @FXML
    private Accordion accordion;
    @FXML
    private TextField filterField;

    @FXML
    private ListView<Competition> competitionList;
    @FXML
    private ListView<Competition> myCompetition;

    @FXML
    private Button createCompetition;

    private FilteredList<Competition> filteredCompetitions;

    private CompetitionController controller;

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
        controller = controllerFactory.getCompetitionController();

        filteredCompetitions = new FilteredList<>(controller.getCompetitions());

        filterField.textProperty().addListener(observable ->
                filteredCompetitions.setPredicate(competition ->
                        competition.toString().toLowerCase().contains(filterField.getText().toLowerCase())
                )
        );

        competitionList.setItems(filteredCompetitions);

        getSession().currentUserProperty().addListener((observable, oldValue, newValue) -> {
            showOwnedCompetition(newValue);
            // accordion.setExpandedPane(myCompetition);
        });
    }

    private void showOwnedCompetition(User user) {
        myCompetition.setItems(filteredCompetitions.filtered(competition ->
                competition.getPlayers().contains(user)
        ));
    }

    public void createCompetition() {
        //TODO create competition if player doesn't have one already
    }


}
