package database.access;

import database.SQL;
import models.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserDAO extends DAO {

    public static ArrayList<User> selectUsers() {
        return database.selectFirstColumn(SQL.ALL.USERS, "naam").stream()
                .map(User::new)
                .collect(Collectors.toCollection(ArrayList<User>::new));
    }

    public static User selectUser(String username, String password) {
        if (database.count(SQL.COUNT.USERSWITHCREDS, username, password) > 0)
            return new User(username);
        else
            return null;
    }
}
