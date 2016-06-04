package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.User;

public class SessionController extends Controller {
    private ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public boolean login(String username, String password) {
        User optionalUser = userDAO.selectUser(username, password);
        setCurrentUser(optionalUser);
        return getCurrentUser() != null;
    }

    @Override
    public void refresh() {
        setCurrentUser(getUserController().getUser(getCurrentUser().getName()));
    }

    @Override
    public void refill() {

    }

    @Override
    public void fetch() {

    }
}
