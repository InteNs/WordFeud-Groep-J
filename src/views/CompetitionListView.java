package views;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Competition;
import models.User;

import java.util.Collections;

public class CompetitionListView extends View {

    @FXML
    private TextField filterField;
    @FXML
    private VBox root;
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
        competitionController.getCompetitions().addListener((ListChangeListener<? super Competition>) observable ->
                showOwnedCompetition(session.getCurrentUser())
        );
    }

    private void showOwnedCompetition(User user) {
        if (user == null) {
            return;
        }
        myCompetition.setItems(filteredCompetitions.filtered(competition ->
                competition.getOwner().equals(user))
        );
        if (myCompetition.getItems().size() > 0)
            showMyCompetition(true);
        else
            showMyCompetition(false);
    }

    private void showMyCompetition(Boolean visible) {
        if (visible) {
            root.getChildren().remove(myCompetition);
            Collections.replaceAll(root.getChildren(), createCompetition, myCompetition);
        } else {
            root.getChildren().remove(createCompetition);
            Collections.replaceAll(root.getChildren(), myCompetition, createCompetition);
        }
    }

    public void createCompetition() {
        parent.showCreateCompetition();
    }


}
