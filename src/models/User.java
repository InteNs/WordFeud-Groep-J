package models;

import enumerations.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {

    private String name;
    private String password;
    private ObservableList<Role> roles;
    private int wins;
    private int loses;

    public User(String name) {
        this.name = name;
        roles = FXCollections.observableArrayList();
        wins = 0;
        loses = 0;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        roles = FXCollections.observableArrayList();
        wins = 0;
        loses = 0;
    }

    public User(String name, String password, Role startingRole) {
        this.name = name;
        this.password = password;
        roles = FXCollections.observableArrayList();
        roles.add(startingRole);
        wins = 0;
        loses = 0;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public ObservableList<Role> getRoles() {
        return roles;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public void setLoses(Integer loses) {
        this.loses = loses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name.equals(user.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
