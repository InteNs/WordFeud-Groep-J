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

	public boolean selectUser(String username) {
		if (UserDAO.selectUser(username)) {
			return true;
		} else {
			return false;
		}
	}

	public void insertUser(String username, String password) {
		UserDAO.insertUser(username, password);
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
