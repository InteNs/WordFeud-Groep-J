package controllers;

import enumerations.Role;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

import java.util.ArrayList;

public class UserController extends Controller {

    private ArrayList<User> fetched;
    private ObservableList<User> users;
    private ObjectProperty<User> selectedUser;

    public UserController() {
        super();
        users = FXCollections.observableArrayList();
        selectedUser = new SimpleObjectProperty<>();
    }

    public ObjectProperty<User> selectedUserProperty() {
        return selectedUser;
    }

    public User getSelectedUser() {
        return selectedUser.get();
    }

    public void setSelectedUser(User user) {
        selectedUser.set(user);
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public User getUser(String name) {
        User user = new User(name);
        if (users.contains(user))
            return users.get(users.indexOf(user));
        return null;
    }

    /**
     * check if user exists, checks database if no users in memory
     *
     * @param username name of user to check
     * @return true if user already exists
     */
    public boolean userExists(String username) {
        if (users != null)
            return getUser(username) != null;
        else
            return userDAO.selectUser(username, null) != null;
    }

    /**
     *
     * @param username name of user to insert
     * @param password password of user to insert
     * @return true if insert is succesfull
     */
    public boolean insertUser(String username, String password) {
        User user = new User(username, password, Role.PLAYER);
        return userDAO.insertUser(user);
    }

    /**
     *
     * @param username name of user to check
     * @return true if username meets the requirements (5 -25 char, only numbers and letters)
     */
    public boolean isValidUsername(String username) {
        return username.length() >= 5 & username.length() <= 25
                && username.matches("[a-zA-Z0-9]+");
    }

    /**
     *
     * @param password password to check
     * @return true if password meets the requirements (length: 5 - 25)
     */
    public boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 25 && (!password.contains(" "));
    }

    public void setRole(User user, Role role, Boolean enabled) {
        if (enabled) {
            userDAO.insertUserRole(user, role);
            user.addRole(role);
        } else {
            userDAO.deleteUserRole(user, role);
            user.removeRole(role);
        }
    }

    public boolean checkPassword(User user, String oldPassword) {
        return user.getPassword().equals(oldPassword);
    }

    public void changePassword(String password) {
        User user = getSessionController().getCurrentUser();
        userDAO.updatePassword(user, password);
    }

    @Override
    public void refresh() {
        if (users.contains(getSelectedUser())) setSelectedUser(users.get(users.indexOf(getSelectedUser())));
    }

    @Override
    public void refill() {
        if (!users.equals(fetched) || !users.stream().allMatch(user ->
                user.deepEquals(fetched.get(fetched.indexOf(user)))))
            users.setAll(fetched);
    }

    @Override
    public void fetch() {
        fetched = userDAO.selectUsers();
        userDAO.setAllStats(fetched);
        userDAO.close();
    }
}
