package controllers;

import database.access.UserDAO;
import enumerations.Role;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

import java.util.ArrayList;
import java.util.Optional;

public class UserController extends Controller {

    private ArrayList<User> users;
    private ObjectProperty<User> selectedUser = new SimpleObjectProperty<>();

    public UserController(ControllerFactory factory) {
        super(factory);
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

    public boolean login(String userName, String passWord) {
        User optionalUser = UserDAO.selectUser(userName, passWord);
        if(users.contains(optionalUser))
            getSession().setCurrentUser(users.get(users.indexOf(optionalUser)));
        return getSession().getCurrentUser() != null;
    }

    public void logOut() {
        getSession().setCurrentUser(null);
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
                && username.matches("[a-zA-Z0-9]+" );
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 5 && password.length() <= 25 && (!password.contains(" "));
    }

    public void setRole(User user, Role role) {
        UserDAO.setRole(user, role);
        user.addRole(role);
    }

    public void removeRole(User user, Role role) {
        UserDAO.removeRole(user, role);
        user.removeRole(role);
    }

    @Override
    public void refresh() {
        users = UserDAO.selectUsers();
        UserDAO.setAllRoles(users);
    }
}
