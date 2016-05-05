package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.User;

public abstract class Controller {

    public abstract void refresh();

    protected static ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

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
