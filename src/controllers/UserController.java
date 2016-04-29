package controllers;

import models.User;

public class UserController {

    public boolean verifyUserInformation(String userName, String passWord) {
        if (User.getUser(userName,passWord)==null) {
            return false;
        }
        return true;
    }

}
