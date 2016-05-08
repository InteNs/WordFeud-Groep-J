package controllers;

import database.access.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

import java.util.*;

public class UserController extends Controller {
    private ArrayList<User> users;

    public UserController() {
        refresh();

    }

    public boolean login(String userName, String passWord) {
        setCurrentUser(users.get(users.indexOf(UserDAO.selectUser(userName, passWord))));
        return currentUser != null;
    }

    public ObservableList<User> getUsers() {
        return FXCollections.observableArrayList(users);
    }

    public boolean userExists(String username) {
        return selectUser(username).isPresent();
    }

    public Optional<User> selectUser(String username) {
        return users.stream().filter(user -> user.getName().equals(username)).findFirst();
    }

    public boolean insertUser(String username, String password) {
        if (UserDAO.insertUser(username, password)) {
            users.add(new User(username));
            return true;
        }
        return false;
    }

    public boolean isValidUsername(String username) {
        return username.length() >= 5 & username.length() <= 25
                && username.matches("[a-zA-Z0-9]+");
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 25;
    }


    public ArrayList<String> getRoles(User user) {
        return user.getRoles();
    }

    public void setRole(User user, String role) {
        UserDAO.setRole(user, role);
        user.addRole(role);
    }

    public void removeRole(User user, String role) {
        UserDAO.removeRole(user, role);
        user.removeRole(role);
    }

    @Override
    public void refresh() {
        users = UserDAO.selectUsers();
        UserDAO.setAllRoles(users);
    }
}
