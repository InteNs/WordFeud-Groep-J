package controllers;

import enumerations.Role;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

public class UserController extends Controller {

    private ObservableList<User> users;
    private ObjectProperty<User> selectedUser;

    public UserController(ControllerFactory factory) {
        super(factory);
        users = FXCollections.observableArrayList();
        selectedUser = new SimpleObjectProperty<>();
    }

    public ObjectProperty<User> selectedUserProperty() {
        return selectedUser;
    }

    public void setSelectedUser(User user) {
        selectedUser.set(user);
    }

    public User getSelectedUser() {
        return selectedUser.get();
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public User getUser(String name) {
        User user = new User(name);
        if(users.contains(user))
            return users.get(users.indexOf(user));
        return null;
    }

    /**
     * check if user exists, checks database if no users in memory
     * @param username name of user to check
     * @return true if user already exists
     */
    public boolean userExists(String username) {
        if(users != null)
            return getUser(username) != null;
        else
            return userDAO.selectUser(username, null) != null;
    }

    public boolean insertUser(String username, String password) {
        User user = new User(username, password, Role.PLAYER);
        return userDAO.insertUser(user);
    }

    public boolean isValidUsername(String username) {
        return username.length() >= 5 & username.length() <= 25
                && username.matches("[a-zA-Z0-9]+" );
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 25 && (!password.contains(" "));
    }

    public void setRole(User user, Role role, Boolean enabled) {
        if (enabled) {
            userDAO.setRole(user, role);
            user.addRole(role);
        } else {
            userDAO.removeRole(user, role);
            user.removeRole(role);
        }
    }

    public boolean checkPassword(String oldPassword) {
        return getSession().getCurrentUser().getPassword().equals(oldPassword);
    }

    public void changePassword(String password) {
        User user = getSession().getCurrentUser();
        userDAO.updatePassword(user, password);
    }

    @Override
    public void refresh() {
        users.setAll(userDAO.selectUsers());
        userDAO.setAllStats(users);
    }
}
