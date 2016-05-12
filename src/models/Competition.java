package models;

import java.util.ArrayList;

public class Competition {

    private int id;
    private User owner;
    private String competitionName;
    private ArrayList<User> players;

    public Competition(int id, User owner, String competitionName, ArrayList<User> players) {
        this.id = id;
        this.owner = owner;
        this.competitionName = competitionName;
        this.players = players;
    }

    public Competition(User owner, String competitionName) {
        this.owner = owner;
        this.competitionName = competitionName;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public boolean containsUser(User user) {
        for (User u : players) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public int getAmountOfPlayers() {
        return players.size();
    }

    public String getName() {
        return competitionName;
    }

    public String toString() {
        return owner.getName() + " - " + competitionName;
    }


    public User getOwner() {
        return owner;
    }

    public Object getId() {
        return id;
    }
}
