package models;
import database.access.UserDAO;

import java.util.ArrayList;

public class User {

    private String name;


    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public static ArrayList getAllUsers() {
        return UserDAO.selectUsers();
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
