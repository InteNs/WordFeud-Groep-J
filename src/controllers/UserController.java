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
    
    public boolean userExists (String username){
		 if(UserDAO.userExists(username)){   	
			 return true;
		 }    	
    	return false;    	
    }
    
    public void addUser (String username, String password){
    	UserDAO.addUser(username, password);
    }
    

    public User getCurrentUser() {
        return currentUser;
    }
}
