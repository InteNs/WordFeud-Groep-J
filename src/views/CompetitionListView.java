package views;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Competition;

public class CompetitionListView extends View {

	@FXML private Accordion accordion;
	@FXML private TextField filterField;
	@FXML private ListView<Competition> competitionList;
	@FXML private Button createCompetition;
	@FXML private ListView<Competition> myCompetition;

    @Override
    public void refresh() {
        competitionController.refresh();
        competitionList.getItems().setAll(competitionController.getCompetitions());
    }

    @Override
    public void constructor() {

    }

    public void createCompetition() {
        //TODO create competition if player doesn't have one already
    }


}
