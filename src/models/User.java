package models;

import enumerations.Role;

import java.util.ArrayList;

public class User {

    private String name;
    private String passWord;
    private ArrayList<Role> roles;
    private int wins;
    private int loses;

    public User(String name) {
        this.name = name;
        roles = new ArrayList<>();
        wins = 0;
        loses = 0;
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
    public boolean equals(Object obj) {
        if (obj instanceof User)
            return ((User) obj).getName().equals(this.getName());

        return super.equals(obj);
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
}
