package controllers;

import models.User;

public class UserController {

    public User currentUser;

    public boolean login(String userName, String passWord) {
        currentUser = User.getFor(userName, passWord);
        return currentUser != null;
    }

}
