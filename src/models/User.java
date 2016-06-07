package models;

import enumerations.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {

    private String name;
    private String password;
    private ObservableList<Role> roles;
    private ObservableList<Stat> stats;


    public User(String name) {
        this.name = name;
        roles = FXCollections.observableArrayList();
        stats = FXCollections.observableArrayList();
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        roles = FXCollections.observableArrayList();
        stats = FXCollections.observableArrayList();
    }


    public User(String name, String password, Role startingRole) {
        this.name = name;
        this.password = password;
        roles = FXCollections.observableArrayList();
        stats = FXCollections.observableArrayList();
        roles.add(startingRole);
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

    public boolean deepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return roles != null ? roles.equals(user.roles) : user.roles == null;

    }

    public int deepHashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

    public Stat getStat(int competitionID){
        for (Stat s: stats) {
            if(s.getCompetitionID() == competitionID) {
                return s;
            }
        }
        return null;
    }

    public ObservableList<Stat> getStats() {
        return stats;
    }

    public void addStat(Stat stat){
        stats.add(stat);
    }

    public void setStats(ObservableList<Stat> stats) {
        this.stats = stats;
    }

}
