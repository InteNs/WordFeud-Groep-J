package database.access;

import database.SQL;
import javafx.util.Pair;
import models.Competition;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompetitionDAO extends DAO {

    public ArrayList<Competition> selectCompetitions() {
        ResultSet rs = database.select(SQL.ALL.COMPETITIONS);
        ArrayList<Competition> competitions = new ArrayList<>();
        try {
            while (rs.next()) {
                competitions.add(new Competition(rs.getInt("id"), new User(rs.getString("account_naam_eigenaar")), rs.getString("omschrijving"), rs.getInt("gemidddelde_score")));
            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return competitions;
    }

    public boolean insertCompetition(Competition competition) {
        database.insert(SQL.INSERT.INSERTCOMPETITION,
                competition.getName(),
                competition.getOwner().getName()
        );
        database.insert(SQL.INSERT.INSERTPLAYER,
                competition.getOwner().getName(),
                Integer.parseInt(database.selectFirstColumnRow(SQL.SELECT.COMPFOROWNER,
                        competition.getOwner().getName()))
        );
        return true;
    }

    public boolean insertPlayer(User user, Competition competition) {
        return database.insert(SQL.INSERT.INSERTPLAYER, user.getName(), competition.getId());
    }

    public ArrayList<Pair<String, Integer>> getPlayerMap() {
        ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();
        ResultSet records = database.select(SQL.ALL.PLAYERSCOMPS);
        try {
            while (records.next()) {
                pairs.add(new Pair<>(
                        records.getString("account_naam"),
                        records.getInt("competitie_id")
                ));
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return pairs;
    }
    
    public boolean alreadyInvited(String requester, String receiver){
        ResultSet rs = database.select(SQL.ALL.GAMES);
        try {
            while(rs.next()){
                if(rs.getString("account_naam_uitdager").equals(requester) && rs.getString("account_naam_tegenstander").equals(receiver)){
                return true;
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
          }
        database.close();
        return false;  
    }
}
