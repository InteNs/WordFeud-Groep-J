package database.access;

import database.SQL;
import enumerations.Role;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public static void setAllRoles(List<User> users) {
        ResultSet rs = database.select(SQL.ALL.ROLES);
        try {
            while (rs.next())
                for (User user : users)
                    if (user.getName().equals(rs.getString("account_naam")))
                        user.addRole(Role.parse(rs.getString("rol_type")));
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
    }

    public static void setRole(User user, Role role) {
        database.insert(SQL.INSERT.SETROLE, user.getName(), role.toString().toLowerCase());
    }

    public static void removeRole(User user, Role role) {
        database.delete(SQL.DELETE.REMOVEROLE, user.getName(), role.toString().toLowerCase());
    }
    
    public static void updatePassword(User user, String password){
        database.update(SQL.UPDATE.UPDATEPASSWORD, password, user.getName());
    }
    
}