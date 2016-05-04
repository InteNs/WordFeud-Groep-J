package controllers;

import database.access.UserDAO;
import models.User;

import java.util.ArrayList;

public class UserController {

    private User currentUser;
    private ArrayList<User> users;

    public UserController() {
        users = UserDAO.selectUsers();
    }

    public boolean login(String userName, String passWord) {
        currentUser = UserDAO.selectUser(userName, passWord);
        return currentUser != null;
    }
    public ArrayList<User> getUsers(){
        return users;
    }

    public boolean userExists(String username) {
        return selectUser(username) != null;
    }

	public User selectUser(String username) {
		return UserDAO.selectUser(username);
	}

	public boolean insertUser(String username, String password) {
		return UserDAO.insertUser(username, password);
	}

    public boolean isValidUsername(String username) {
        return username.length() >= 5 & username.length() <= 25
                && username.matches("[a-zA-Z0-9]+");
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 25;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<String> getComps(User user){
       return UserDAO.getUserComps(user.toString());
    }

}
