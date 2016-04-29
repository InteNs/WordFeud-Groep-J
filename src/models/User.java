package models;


import database.DatabaseAccessor;

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
        if(obj instanceof User)
            return ((User)obj).getName().equals(this.getName());

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static User getFor(String userName, String passWord) {
        return DatabaseAccessor.selectUser(userName,passWord);
    }
}
