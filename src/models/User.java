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
}
