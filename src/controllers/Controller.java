package controllers;

import models.User;

public abstract class Controller {

    protected static User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }
}
