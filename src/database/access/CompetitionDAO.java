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
        ArrayList<Competition> competitions = new ArrayList<>();
        ResultSet records = null;
        try {
            records = database.select(SQL.ALL.COMPETITIONS);
            while (records.next()) {
                competitions.add(new Competition(records.getInt("id"), new User(records.getString("account_naam_eigenaar")), records.getString("omschrijving"), records.getInt("gemiddelde_score")));
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
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
        ResultSet records = null;
        try {
            records = database.select(SQL.ALL.PLAYERSCOMPS);
            while (records.next()) {
                pairs.add(new Pair<>(
                        records.getString("account_naam"),
                        records.getInt("competitie_id")
                ));
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
        return pairs;
    }
}
