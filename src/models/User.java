package models;

import controllers.UserController;
import database.access.UserDAO;

import java.util.ArrayList;

public class User {

    private String name;

    public enum Role {
        player,
        administrator,
        moderator,
        observer
    }

    private ArrayList<Role> roles = new ArrayList<Role>();

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
}
