package models;


import database.DatabaseAccessor;

import java.util.ArrayList;
import java.util.Collections;

public class User {

    private String name;
    private ArrayList<Letter>deck;


    public User(String name) {
        this.name = name;
        deck = new ArrayList<>();

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

    public ArrayList<Letter> getDeck() {
        return deck;
    }

    public void addDeck(Letter... letterInDeck) {
        Collections.addAll(deck, letterInDeck);
    }
}
