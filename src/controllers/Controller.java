package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.Game;
import models.User;

import javax.crypto.interfaces.PBEKey;

public abstract class Controller {

    public abstract void refresh();

    protected static ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    protected static ObjectProperty<Game> selectedGame = new SimpleObjectProperty<>();

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public Game getSelectedGame() {
        return selectedGame.get();
    }

    public ObjectProperty<Game> selectedGameProperty() {
        return selectedGame;
    }

    public void setSelectedGame(Game selectedGame) {
        Controller.selectedGame.set(selectedGame);
    }
}
