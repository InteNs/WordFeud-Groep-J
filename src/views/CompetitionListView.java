package views;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Competition;

import java.util.Collections;
import java.util.function.Predicate;

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

    private Predicate<Competition> filterText, filterOwned;

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
        filteredCompetitions = new FilteredList<>(competitionController.getCompetitions());

        filterText = competition -> competition.toString().toLowerCase().contains(filterField.getText().toLowerCase());
        filterOwned = competition -> competition.getOwner().equals(session.getCurrentUser());

        filterField.textProperty().addListener(observable ->
                filteredCompetitions.setPredicate(filterText)
        );

        competitionList.setItems(filteredCompetitions);
        myCompetition.setItems(filteredCompetitions.filtered(filterOwned));

        showMyCompetition();
        myCompetition.getItems().addListener((ListChangeListener<? super Competition>) observable ->
                showMyCompetition());

        competitionList.getSelectionModel().selectedItemProperty().addListener((ig1, ig2, newValue) ->
                selectCompetition(newValue));
        myCompetition.setOnMouseClicked(e -> selectCompetition(competitionController.getSelectedCompetition()));
    }

    private void selectCompetition(Competition competition){
        competitionController.setSelectedCompetition(competition);
        parent.showCompetitionInfoView();
    }

    private void showMyCompetition() {
        if (myCompetition.getItems().size() > 0) {
            if(root.getChildren().contains(createCompetition)) {
                root.getChildren().remove(myCompetition);
                Collections.replaceAll(root.getChildren(), createCompetition, myCompetition);
            }
        } else {
            root.getChildren().remove(createCompetition);
            Collections.replaceAll(root.getChildren(), myCompetition, createCompetition);
        }
    }

    public void createCompetition() {
        parent.showCreateCompetition();
    }

}
