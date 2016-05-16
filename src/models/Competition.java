package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Competition {

    private int id;
    private User owner;
    private String competitionName;
    private ObservableList<User> players;

    public Competition(int id, User owner, String competitionName) {
        this.id = id;
        this.owner = owner;
        this.competitionName = competitionName;
        this.players = FXCollections.observableArrayList();
    }

    public Competition(User owner, String competitionName) {
        this.owner = owner;
        this.competitionName = competitionName;
        this.players = FXCollections.observableArrayList();
    }

    public ObservableList<User> getPlayers() {
        return players;
    }

    public void addPlayer(User user) {
        players.add(user);
    }

    public boolean containsUser(User user) {
        return players.contains(user);
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

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Competition that = (Competition) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}