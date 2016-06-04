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
        try {
            ResultSet records = database.select(SQL.ALL.USERS);

            while (records.next()) {
                User user = new User(records.getString("naam"), records.getString("wachtwoord"));
                if (users.contains(user)) {
                    users.get(users.indexOf(user)).addRole(Role.parse(records.getString("rol_type")));
                } else {
                    user.addRole(Role.parse(records.getString("rol_type")));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            printError(e);
        }
        return users;
    }

    /**
     * select user from database
     *
     * @param username username
     * @param password null to not check password
     * @return the user if it exists
     */
    public User selectUser(String username, String password) {
        ResultSet records = null;
        User user = null;
        try {
            if (password != null) {
                records = database.select(SQL.SELECT.USERWITHCREDS, username, password);
            } else {
                records = database.select(SQL.SELECT.USERWITHNAME, username);
            }


            while (records.next()) {
                if (records.getString("naam").equals(username)) {
                    if (password != null & !records.getString("wachtwoord").equals(password))
                        return null;
                    if (user == null)
                        user = new User(username, password);
                    user.addRole(Role.parse(records.getString("rol_type")));
                }
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
        return user;
    }

    public boolean insertUser(User user) {
        boolean success = database.insert(SQL.INSERT.INSERTUSER, user.getName(), user.getPassword());
        for (Role role : user.getRoles())
            if (!database.insert(SQL.INSERT.SETROLE, user.getName(), Role.format(role)))
                success = false;
        return success;
    }

    public boolean insertUserRole(User user, Role role) {
        return database.insert(SQL.INSERT.SETROLE, user.getName(), Role.format(role));
    }

    public boolean deleteUserRole(User user, Role role) {
        return database.delete(SQL.DELETE.REMOVEROLE, user.getName(), Role.format(role));
    }

    public void setAllStats(List<User> users) {
        ResultSet records = null;
        try {
            records = database.select(SQL.ALL.WINSLOSES);

            while (records.next()) {
                for (User user : users) {
                    if (user.getName().equals(records.getString("account_naam"))) {
                        user.setWins(records.getInt("wins"));
                        user.setLoses(records.getInt("lost"));
                    }
                }
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
    }

    public void updatePassword(User user, String password) {
        database.update(SQL.UPDATE.UPDATEPASSWORD, password, user.getName());
    }
}