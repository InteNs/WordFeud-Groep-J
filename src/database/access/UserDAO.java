package database.access;

import database.SQL;
import enumerations.Role;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO {

    public ArrayList<User> selectUsers() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet records = database.select(SQL.ALL.USERS);
        try {
            while (records.next()) {
                users.add(new User(records.getString("naam"), records.getString("wachtwoord")));
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return users;
    }

    public User selectUser(String username, String password) {
        ResultSet records = database.select(SQL.ALL.USERS);
        try {
            while (records.next()) {
                if (records.getString("naam").equals(username) &
                        records.getString("wachtwoord").equals(password)) {
                    return new User(username, password);
                }
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return null;
    }

    public boolean insertUser(String username, String password) {
        return database.insert(SQL.INSERT.INSERTUSER, username, password);
    }

    public void setAllRoles(List<User> users) {
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

    public void setRole(User user, Role role) {
        database.insert(SQL.INSERT.SETROLE, user.getName(), Role.format(role));
    }

    public void removeRole(User user, Role role) {
        database.delete(SQL.DELETE.REMOVEROLE, user.getName(), Role.format(role));
    }

    public void setAllStats(List<User> users) {
        ResultSet rs = database.select(SQL.ALL.WINSLOSES);
        try {
            while (rs.next()) {
                for (User user : users) {
                    if (user.getName().equals(rs.getString("account_naam"))) {
                        user.setWins(rs.getInt("wins"));
                        user.setLoses(rs.getInt("lost"));
                    }
                }
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
    }

    public void updatePassword(User user, String password) {
        database.update(SQL.UPDATE.UPDATEPASSWORD, password, user.getName());
    }
}