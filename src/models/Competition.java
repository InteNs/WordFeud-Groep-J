package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Competition {

    private int id;
    private User owner;
    private String competitionName;
    private int competitionScoreAvgerage;
    private ObservableList<Game> games;
    private ObservableList<User> players;

    public Competition(int id, User owner, String competitionName, int competitionScoreAvgerage) {
        this.id = id;
        this.owner = owner;
        this.competitionName = competitionName;
        this.players = FXCollections.observableArrayList();
        this.games = FXCollections.observableArrayList();
        this.competitionScoreAvgerage = competitionScoreAvgerage;
    }

    public Competition(int id, User owner, String competitionName) {
        this.id = id;
        this.owner = owner;
        this.competitionName = competitionName;
        this.players = FXCollections.observableArrayList();
        this.games = FXCollections.observableArrayList();
    }

    public Competition(User owner, String competitionName) {
        this.owner = owner;
        this.competitionName = competitionName;
        this.players = FXCollections.observableArrayList();
        this.games = FXCollections.observableArrayList();
        players.add(owner);
    }

    public void setGames(ObservableList<Game> games) {
        this.games = games;
    }

    public ObservableList<User> getPlayers() {
        return players;
    }

    public ObservableList<Game> getGames(){
        return games;
    }

    public void addPlayer(User user) {
        if (!players.contains(user)) players.add(user);
    }

    public int getAmountOfUsers(){
        return players.size();
    }

    public boolean hasUser(User user) {
        return players.contains(user);
    }

    public int getAmountOfGames() {
        return games.filtered(Game::isGame).size();
    }

    public int getCompetitionScoreAvgerage(){
        return competitionScoreAvgerage;
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

    public boolean deepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Competition that = (Competition) o;

        if (id != that.id) return false;
        if (players != null ? !players.equals(that.players) : that.players != null) return false;

        return true;
    }

    public int deepHashCode() {
        int result = id;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }
}