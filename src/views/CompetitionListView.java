package views;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.CompetitionController;
import controllers.UserController;
import enumerations.GameState;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Competition;

public class CompetitionListView extends View implements Initializable{

	@FXML private Accordion accordion;
	@FXML private TextField filterField;
	@FXML private ListView<Competition> competitionList;
	@FXML private Button createCompetition;
	@FXML private ListView<Competition> myCompetition;
	
	private CompetitionController competitionController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		competitionController = new CompetitionController();
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            search(oldValue, newValue);
        });
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		  competitionController.setCurrentUser(parent.getCurrentUser());
	      competitionController.refresh(); // touches database
	      competitionList.getItems().setAll(competitionController.getCompetitions());
	}
	
    private void search(String oldVal, String newVal) {
        if (oldVal != null && (newVal.length() < oldVal.length())) {
            //actor has deleted a character, so reset the search
            competitionList.getItems().clear();
            competitionList.getItems().addAll(competitionController.getCompetitions());
        }
        competitionList.getItems().clear();
        //add an item if any item that exists contains any value that has been searched for
        competitionController.getCompetitions().stream()
                .filter(entry -> entry.getName().contains(newVal))
                .forEach(entry -> competitionList.getItems().add(entry));
    }
	
	public void createCompetition(){
		// create competition if player doesn't have one already
	}
	

}
