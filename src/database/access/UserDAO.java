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
                if (r.equals("player")) {
                    u.addRole(User.Role.player);
                }
                if (r.equals("administrator")) {
                    u.addRole(User.Role.administrator);
                }
                if (r.equals("moderator")) {
                    u.addRole(User.Role.moderator);
                }
                if (r.equals("observer")) {
                    u.addRole(User.Role.observer);
                }
            }

        }
    }

    public static void setRole(User user, User.Role role) {
        database.insert(SQL.INSERT.SETROLE, user.getName(), role.toString());
    }

    public static void removeRole(User user, String role) {
        database.insert(SQL.DELETE.REMOVEROLE, user.getName(), role);
    }
}

