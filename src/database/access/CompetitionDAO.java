package database.access;

import java.sql.ResultSet;
import java.util.ArrayList;

import database.SQL;
import models.Competition;
import models.Game;
import models.User;

public class CompetitionDAO extends DAO {

	public static ArrayList<Competition> selectCompetition(){
		ResultSet records = database.select(SQL.ALL.COMPETITIONS);
        ArrayList<Competition> competitions = new ArrayList<>();
        try {
            while (records.next()) {
                competitions.add(new Competition(
                        records.getInt("id"),
                        new User(records.getString("account_naam_eigenaar")),
                        records.getString("omschrijving")
                ));
            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return competitions;
	}
}
