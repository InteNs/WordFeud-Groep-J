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


    public ArrayList<User.Role> getRoles(User user) {
        return user.getRoles();
    }

    public void setRole(User user, User.Role role) {
        UserDAO.setRole(user, role);
        user.addRole(role);
    }

    public void removeRole(User user, User.Role role) {
        UserDAO.removeRole(user, role.toString());
        if(role.equals(role.player)) {
            user.removeRole(role.player);
        }
          if(role.equals(role.administrator)) {
              user.removeRole(role.administrator);
          }
            if(role.equals(role.moderator)){
                user.removeRole(role.moderator);
            if(role.equals(role.observer)) {
                user.removeRole(role.observer);
            }
        }

    }

    @Override
    public void refresh() {
        users = UserDAO.selectUsers();
        UserDAO.setAllRoles(users);
    }
}
