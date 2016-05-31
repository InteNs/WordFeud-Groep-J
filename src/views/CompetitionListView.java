package views;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Competition;

import java.util.Objects;
import java.util.function.Predicate;

public class CompetitionListView extends View {

    @FXML private TextField filterField;
    @FXML private VBox root;
    @FXML private ListView<Competition> competitionList;
    @FXML private TextField myCompetition;
    @FXML private Button createCompetition;
    @FXML private StackPane myCompetitionArea;

    private FilteredList<Competition> filteredCompetitions;

    private Predicate<Competition> filterText, filterOwned;

    @Override
    public void refresh() {
        showMyCompetition();
    }

    @Override
    public void clear() {
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

        showMyCompetition();
        competitionController.getCompetitions().addListener((ListChangeListener<? super Competition>) observable ->
                showMyCompetition());

        myCompetition.setOnMouseClicked(event ->
                selectCompetition(filteredCompetitions.filtered(filterOwned).get(0)));

        competitionList.getSelectionModel().selectedItemProperty()
                .addListener((o, oldValue, newValue) -> {
                    if (!Objects.equals(oldValue, newValue) && newValue != null)
                        selectCompetition(newValue);
                });
    }

    private void showMyCompetition() {
        myCompetitionArea.getChildren().clear();
        if (filteredCompetitions.filtered(filterOwned).isEmpty()) {
            myCompetitionArea.getChildren().add(createCompetition);
        } else {
            myCompetition.setText(filteredCompetitions.filtered(filterOwned).get(0).toString());
            myCompetitionArea.getChildren().add(myCompetition);
        }
    }

    private void selectCompetition(Competition competition) {
        competitionController.setSelectedCompetition(competition);
        parent.setContent(parent.competitionInfoView);
    }

    public void createCompetition() {
        parent.setContent(parent.createCompetitionView);
    }

}
