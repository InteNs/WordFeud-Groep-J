package models;

import enumerations.Role;

import java.util.ArrayList;

public class User {

    private String name;
    private String passWord;
    private ArrayList<Role> roles;

    public User(String name) {
        this.name = name;
        roles = new ArrayList<>();
    }

    public User(String name, String passWord) {
        this.name = name;
        this.passWord = passWord;
        roles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPassWord() {
        return passWord;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name.equals(user.name);

    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public boolean hasRole(Role role) {
        Boolean result = false;
        for (Role r : roles) {
            if (r.equals(role)) {
                result = true;
            }
        }
        return result;
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
