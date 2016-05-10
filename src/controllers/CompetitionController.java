package controllers;

import database.access.CompetitionDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Competition;
import models.User;

import java.util.ArrayList;

public class CompetitionController extends Controller {

    private ArrayList<Competition> competitions;
    private ObjectProperty<Competition> selectedCompetition = new SimpleObjectProperty<>();

    public CompetitionController(ControllerFactory factory) {
        super(factory);
    }

    public Competition getSelectedCompetition() {
        return selectedCompetition.get();
    }

    public ObjectProperty<Competition> selectedCompetitionProperty() {
        return selectedCompetition;
    }

    public void setSelectedCompetition(Competition competition) {
        selectedCompetition.set(competition);
    }

    public ArrayList<User> getUser(Competition comp) {
        return comp.getPlayers();
    }

    public ObservableList<Competition> getCompetitions() {
        return FXCollections.observableArrayList(competitions);
    }

    public ObservableList<String> getCompetitions(User user) {
        ArrayList<String> competitionList = new ArrayList<>();
        for (Competition c : getCompetitions()) {
            if (c.containsUser(user)) {
                competitionList.add(c.getName());
            }
        }
        return FXCollections.observableArrayList(competitionList);
    }

    @Override
    public void refresh() {
        this.competitions = CompetitionDAO.selectCompetition();
    }
}
