package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Competition;
import models.Game;
import models.User;

public class CompetitionController extends Controller {

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
        return competitions.filtered(competition -> competition.getId() == id).get(0);
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

    public void mapPlayers() {
        competitionDAO.getPlayerMap().forEach(set ->
                getCompetition(set.getValue()).addPlayer(getUserController().getUser(set.getKey()))
        );
    }

    public void assignGames(ObservableList<Game> games) {
        for (Competition competition : competitions) {
            competition.setGames(games.filtered(game -> game.getCompetitionId() == competition.getId()));
        }
    }

    @Override
    public void refresh() {
        assignGames(getGameController().getGames());
        mapPlayers();
        if(competitions.contains(getSelectedCompetition()))
            setSelectedCompetition(competitions.get(competitions.indexOf(getSelectedCompetition())));
    }

    @Override
    public void refill() {
        competitions.setAll(competitionDAO.selectCompetitions());
    }
}
