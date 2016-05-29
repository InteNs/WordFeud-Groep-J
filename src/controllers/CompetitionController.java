package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import models.Competition;
import models.User;

import java.util.ArrayList;

public class CompetitionController extends Controller {

    private ArrayList<Pair<String, Integer>> playerMap;
    private ArrayList<Competition> fetched;
    private ObservableList<Competition> competitions;
    private ObjectProperty<Competition> selectedCompetition;

    public CompetitionController(ControllerFactory factory) {
        super(factory);
        competitions = FXCollections.observableArrayList();
        selectedCompetition = new SimpleObjectProperty<>();
    }

    public boolean addUserInCompetition(User user, Competition competition) {
        if (competitionDAO.insertPlayer(user, competition)) {
            competition.addPlayer(user);
            return true;
        }
        return false;
    }

    public boolean isUserInCompetition(User thisUser, Competition thisCompetition) {
        return thisCompetition.getPlayers().contains(thisUser);
    }

    public Competition getSelectedCompetition() {
        return selectedCompetition.get();
    }

    public void setSelectedCompetition(Competition competition) {
        selectedCompetition.set(competition);
    }

    public ObjectProperty<Competition> selectedCompetitionProperty() {
        return selectedCompetition;
    }

    public Competition getCompetition(User user) {
        for (Competition competition : competitions)
            if (competition.getOwner().equals(user)) return competition;
        return null;
    }

    public Competition getCompetition(int id) {
        for (Competition competition : competitions)
            if (competition.getId() == id) return competition;
        return null;
    }

    public boolean isValidCompetitionName(String competitionName) {
        return competitionName.length() >= 5 & competitionName.length() <= 25
                && competitionName.matches("[a-zA-Z0-9]+");
    }

    public boolean createCompetition(String competitionName) {
        if (getCompetition(getSession().getCurrentUser()) != null)
            return false;
        Competition newComp = new Competition(getSession().getCurrentUser(), competitionName);
        competitions.add(newComp);
        return competitionDAO.insertCompetition(newComp);
    }

    public ObservableList<Competition> getCompetitions() {
        return competitions;
    }

    public ObservableList<Competition> getCompetitions(User user) {
        return competitions.filtered(competition -> competition.getPlayers().contains(user));
    }

    @Override
    public void refresh() {
        playerMap.forEach(set ->
                getCompetition(set.getValue()).addPlayer(getUserController().getUser(set.getKey()))
        );

        competitions.forEach(competition ->
                competition.setGames(getGameController().getGames(competition)));

        if(competitions.contains(getSelectedCompetition()))
            setSelectedCompetition(competitions.get(competitions.indexOf(getSelectedCompetition())));
    }

    @Override
    public void refill() {
        competitions.setAll(fetched);
    }

    @Override
    public void fetch() {
        fetched = competitionDAO.selectCompetitions();
        playerMap = competitionDAO.getPlayerMap();
    }

}
