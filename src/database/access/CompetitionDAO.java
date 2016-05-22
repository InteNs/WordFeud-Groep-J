package database.access;

import database.SQL;
import models.Competition;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
        return database.insert(SQL.INSERT.INSERTCOMPETITION,
                competition.getName(),
                competition.getOwner().getName()
        );
    }

    public boolean insertPlayer(User user, Competition competition) {
        return database.insert(SQL.INSERT.INSERTPLAYER, user.getName(), competition.getId());
    }

    public HashMap<String, Integer> getPlayerMap() {
        HashMap<String, Integer> userCompMap = new HashMap<>();
        ResultSet records = database.select(SQL.ALL.PLAYERSCOMPS);
        try {
            while (records.next()) {
                userCompMap.put(
                        records.getString("account_naam"),
                        records.getInt("competitie_id")
                );
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return userCompMap;
    }
}
