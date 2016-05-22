package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Competition;
import models.User;
import models.Game;

public class CompetitionController extends Controller {

    private ObservableList<Competition> competitions;
    private ObjectProperty<Competition> selectedCompetition;

    public CompetitionController(ControllerFactory factory) {
        super(factory);
        competitions = FXCollections.observableArrayList(competitionDAO.selectCompetitions());
        mapPlayers();
        selectedCompetition = new SimpleObjectProperty<>();
        assignGames(getGameController().getGames());
    }

    public boolean insertUserInCompetition(User user, Competition competition) {
        if (competitionDAO.insertPlayer(user, competition)) {
            competition.addPlayer(user);
            return true;
        }
        return false;
    }

    public boolean isUserInCompetition(User thisUser, Competition thisCompetition){
        boolean check = false;
        for (Competition competition : competitions){
            if (thisCompetition == competition){
                ObservableList<User> players = competition.getPlayers();
                for (User user : players){
                    if (thisUser == user){
                        check = true;
                        break;
                    }
                }
                break;
            }
        }
        return check;
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
        if (getCompetition(getSession().getCurrentUser()) != null) {
            return false;
        }
        Competition newComp = new Competition(getSession().getCurrentUser(), competitionName);
        competitionDAO.insertCompetition(newComp);
        //add owner as player
        refresh();
        competitions.stream()
                .filter(competition -> competition.getOwner().equals(newComp.getOwner()))
                .forEach(competition -> {
                    competitionDAO.insertPlayer(competition.getOwner(), competition);
                    competition.addPlayer(competition.getOwner());
                });
        return true;
    }

    public ObservableList<Competition> getCompetitions() {
        return competitions;
    }

    public ObservableList<Competition> getCompetitions(User user) {
        return competitions.filtered(competition -> competition.getPlayers().contains(user));
    }

    public void mapPlayers() {
        competitionDAO.getPlayerMap().entrySet().forEach(set -> {
            getCompetition(set.getValue()).addPlayer(getUserController().getUser(set.getKey()));
        });
    }

    public void assignGames(ObservableList<Game> games){
        for (Competition competition : competitions){
            competition.setGames(games.filtered(game -> game.getCompetitionId() == competition.getId()));
        }
    }

    @Override
    public void refresh() {
        competitions.setAll(competitionDAO.selectCompetitions());
        mapPlayers();
    }
}
