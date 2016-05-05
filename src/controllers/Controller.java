package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.User;

public abstract class Controller {

    public abstract void refresh();

    protected static ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

    protected static ObjectProperty<User> selectedUser = new SimpleObjectProperty<>();

    public void setSelectedUser(User user) {
        selectedUser.set(user);
    }

    public User getSelectedUser() {
        return selectedUser.get();
    }

    public ObjectProperty<User> selectedUserProperty() {
        return selectedUser;
    }


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
