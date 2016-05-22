package models;

import enumerations.GameState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import enumerations.Role;

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
        this.competitionScoreAvgerage = competitionScoreAvgerage;
    }

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
        players.add(owner);
    }

    public boolean isUserInCompetition(User user) {
        boolean check = false;
        for (User player : players) {
            if (player == user) {
                check = true;
            }
        }
        return check;
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
        players.add(user);
    }

    public boolean containsUser(User user) {
        return players.contains(user);
    }

    public int getAmountOfUsers(){
        return players.size();
    }

    public int getAmountOfPlayers() {
        int counter = 0;
        for (User user : players){
            ArrayList<Role> roles = user.getRoles();
            for (Role role : roles){
                if ( role == Role.PLAYER){
                    counter++;
                }
            }
        }
        return counter;
    }

    public int getAmountOfGames(){
        int counter = 0;
        for (Game game : games){
            if (game.getGameState() == GameState.PLAYING || game.getGameState() == GameState.FINISHED){
                counter++;
            }
        }
        return counter;
    }

    public int getCompetitionScoreAvgerage(){
        return competitionScoreAvgerage;
    }

    public int getAmountOfRunningGames() {
        int counter = 0;
        for (Game game : games){
            if (game.getGameState() == GameState.PLAYING){
                counter++;
            }
        }
        return counter;
    }

    public int getAmountOfFinishedGames() {
        int counter = 0;
        for (Game game : games){
            if (game.getGameState() == GameState.FINISHED){
                counter++;
            }
        }
        return counter;
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