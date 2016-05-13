package controllers;

import database.access.UserDAO;
import enumerations.Role;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

import java.util.Optional;

public class UserController extends Controller {

    private ObservableList<User> users;
    private ObjectProperty<User> selectedUser;

    public UserController(ControllerFactory factory) {
        super(factory);
        users = FXCollections.observableArrayList(UserDAO.selectUsers());
        selectedUser = new SimpleObjectProperty<>();
        UserDAO.setAllRoles(users);
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
        return users;
    }

    public boolean userExists(String username) {
        return selectUser(username).isPresent();
    }

    public Optional<User> selectUser(String username) {
        return users.stream().filter(user -> user.getName().equals(username)).findFirst();
    }

    public boolean insertUser(String username, String password) {
        if (UserDAO.insertUser(username, password)) {
            User user = new User(username);
            users.add(user);
            setRole(user,(Role.PLAYER));
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

    public boolean checkPassword(String oldPassword) {
        String userName = getSession().getCurrentUser().getName();
        User optionalUser = UserDAO.selectUser(userName, oldPassword);

        if (!(optionalUser == null)) {
            return true;
        } else {
            return false;
        }
    }

    public void changePassword(String password) {
        User user = getSession().getCurrentUser();
        UserDAO.updatePassword(user, password);
    }

    @Override
    public void refresh() {
        users.setAll(UserDAO.selectUsers());
        UserDAO.setAllRoles(users);
    }
}
