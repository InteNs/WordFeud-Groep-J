package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.Competition;
import models.Game;
import models.User;

public abstract class Controller {

    public abstract void refresh();

    //selected game (from list)
    private static ObjectProperty<Game> selectedGame = new SimpleObjectProperty<>();

    public Game getSelectedGame() {
        return selectedGame.get();
    }

    public ObjectProperty<Game> selectedGameProperty() {
        return selectedGame;
    }

    public void setSelectedGamee(Game game) {
        selectedGame.set(game);
    }

    //selected competition (from list)
    private static ObjectProperty<Competition> selectedCompetition = new SimpleObjectProperty<>();

    public Competition getSelectedCompetition() {
        return selectedCompetition.get();
    }

    public ObjectProperty<Competition> selectedCompetitionProperty() {
        return selectedCompetition;
    }

    public void setSelectedCompetition(Competition competition) {
        selectedCompetition.set(competition);
    }

    //selected user (from list)
    private static ObjectProperty<User> selectedUser = new SimpleObjectProperty<>();

    public void setSelectedUser(User user) {
        selectedUser.set(user);
    }

    public User getSelectedUser() {
        return selectedUser.get();
    }

    public ObjectProperty<User> selectedUserProperty() {
        return selectedUser;
    }


    //current user (from logged in)
    private static ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public User getCurrentUser() {
        return currentUser.get();
    }
}
