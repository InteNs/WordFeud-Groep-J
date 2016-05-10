package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.User;

public class SessionController extends Controller {
    private ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

    public SessionController(ControllerFactory factory) {
        super(factory);
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    @Override
    public void refresh() {

    }
}
