package database.access;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.SQL;
import models.Competition;
import models.Game;
import models.User;

import javax.management.openmbean.CompositeType;

public class CompetitionDAO extends DAO {

    public static ArrayList<Competition> selectCompetition() {
        ResultSet rs = database.select(SQL.ALL.COMPETITIONS);
        ArrayList<Competition> competitions = new ArrayList<>();
        try {
            while (rs.next()) {
                ArrayList<User> users = selectAllUsers(rs.getInt("id"));
                competitions.add(new Competition(rs.getInt("id"), new User(rs.getString("account_naam_eigenaar")), rs.getString("omschrijving"), users));

            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return competitions;
    }
    
    public static boolean insertCompetition(String compName, User user) {
        return database.insert(SQL.INSERT.INSERTCOMPETITION, compName, user.getName());
    }
    
    public static boolean insertPlayer(String username, int ID) {
        return database.insert(SQL.INSERT.INSERTPLAYER, username, ID);
    }

    public static ArrayList<User> selectAllUsers(Integer comp_id) {
        List<String> usernames;
        ArrayList<User> users = new ArrayList<>();
        usernames = database.selectFirstColumn(SQL.SELECT.SELECTUSERINCOMP, comp_id);
        for (String s : usernames) {
            users.add(new User(s));
        }

        return users;
    }
}
