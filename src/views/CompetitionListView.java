package views;

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

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
        filteredCompetitions = new FilteredList<>(competitionController.getCompetitions());

        filterField.textProperty().addListener(observable ->
                filteredCompetitions.setPredicate(competition ->
                        competition.toString().toLowerCase().contains(filterField.getText().toLowerCase())
                )
        );

        competitionList.setItems(filteredCompetitions);

        session.currentUserProperty().addListener((observable, oldValue, newValue) -> {
            showOwnedCompetition(newValue);
        });
    }

    private void showOwnedCompetition(User user) {
        myCompetition.setItems(filteredCompetitions.filtered(competition ->
                competition.getPlayers().contains(user)
        ));
    }

    public void createCompetition() {
        parent.showCreateCompetition();
    }


}
