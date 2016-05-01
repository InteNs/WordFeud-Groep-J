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
    
    public static boolean userExists (String username){
    	if (database.count(SQL.COUNT.USERCOUNT, username) > 0){
    		return true;
    	} else {    		
    		return false;
    	}
    }
    
    public static void addUser (String username, String password){
    	database.insert(SQL.INSERT.ADDUSER, username, password);
    }
    
}
