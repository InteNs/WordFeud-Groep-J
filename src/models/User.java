package models;

import controllers.UserController;
import database.access.UserDAO;

import java.util.ArrayList;

public class User {

    private String name;
    private ArrayList<String> roles = new ArrayList<>();


    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User)
            return ((User) obj).getName().equals(this.getName());

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static ArrayList getAllUsers() {
        return UserDAO.selectUsers();
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public void removeRole(String role) {
        roles.remove(role);
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public boolean hasRole(String roleString) {
        Boolean result = false;
        for (String role : roles) {
            if (role.equals(roleString)) {
                result = true;
            }
        }
        return result;
    }
}
