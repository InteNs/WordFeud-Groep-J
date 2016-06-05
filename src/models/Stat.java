package models;

/**
 * Created by cedricremond on 05/06/16.
 */
public class Stat {
    private int competitionID;
    private int wins;
    private int lost;
    private int total_games;
    private double avg_score;

    public Stat(int competitionID, int wins, int lost,int total_games,double avg_score) {
        this.competitionID = competitionID;
        this.wins = wins;
        this.lost = lost;
        this.total_games = total_games;
        this.avg_score = avg_score;
    }

    public int getCompetitionID() {
        return competitionID;
    }

    public int getWins() {
        return wins;
    }

    public int getLost() {
        return lost;
    }

    public int getTotal_games() {
        return total_games;
    }

    public double getAvg_score() {
        return avg_score;
    }
}
