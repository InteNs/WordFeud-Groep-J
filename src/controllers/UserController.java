package controllers;

import models.User;

public class UserController {

    private User currentUser;

    public boolean login(String userName, String passWord) {
        currentUser = User.getFor(userName, passWord);
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
