package database.access;

import database.SQL;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends DAO {

    public static ArrayList<User> selectUsers() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet records = database.select(SQL.ALL.USERS);
        try {
            while (records.next()) {
                users.add(new User(records.getString("naam")));
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return users;
    }

    public static User selectUser(String username, String password) {
        if (database.count(SQL.COUNT.USERSWITHCREDS, username, password) > 0)
            return new User(username);
        else
            return null;
    }

    public static User selectUser(String username) {
        if (database.count(SQL.COUNT.USERCOUNT, username) > 0)
            return new User(username);
        else
            return null;
    }

    public static boolean insertUser(String username, String password) {
        return database.insert(SQL.INSERT.INSERTUSER, username, password);
    }

    public static void setAllRoles(ArrayList<User> users) {
        for (User u : users) {
            ArrayList<String> roles = database.selectFirstColumn(SQL.SELECT.SELECTUSERROLES, u.toString());
            for (String r : roles) {
                u.addRole(r);
            }

        }
    }

    public static void setRole(User user, String role) {
        database.insert(SQL.INSERT.SETROLE, user.getName(), role);
    }

    public static void removeRole(User user, String role) {
        database.insert(SQL.DELETE.REMOVEROLE, user.getName(), role);
    }
}

