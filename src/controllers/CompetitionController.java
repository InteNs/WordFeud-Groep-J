package controllers;

import database.access.CompetitionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Competition;
import models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CompetitionController extends Controller {

    private ArrayList<Competition> competitions;

    public CompetitionController() {
        this.competitions = CompetitionDAO.selectCompetition();
    }

    public ArrayList<User> getUser(Competition comp) {
        return comp.getPlayers();
    }

    public ObservableList<Competition> getCompetitions() {
        return FXCollections.observableArrayList(competitions);
    }

    public ObservableList<String> getComps(User user) {
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
    }
}
