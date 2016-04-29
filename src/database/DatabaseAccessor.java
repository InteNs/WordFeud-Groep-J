package database;


import models.Game;
import models.Message;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DatabaseAccessor {

    private static Database database = new Database();


    public static ArrayList<Message> selectMessages(Game game) {
        ArrayList<Message> messages = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.SELECTMESSAGEFORGAME, Arrays.asList(game.getID()));

        try {
            while (records.next()) {
                messages.add(new Message(
                        records.getString("account_naam"),
                        records.getString("bericht"),
                        records.getTimestamp("tijdstip")));
            }
            records.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static boolean addMessage(User user, Game game, String message) {
        database.insert("chatregel", Arrays.asList(
                user.getName(),
                game.getID(),
                currentTimeStamp(),
                message
        ));
        database.close();
        return true;
    }
    public static ArrayList<Game> selectGames() {
        ArrayList<Game> games = new ArrayList<>();
        ResultSet records = database.select("spel");
        try {
            while (records.next()) {
                games.add(new Game(
                        records.getInt("id"),
                        Arrays.asList(
                                records.getString("account_naam_uitdager"),
                                records.getString("account_naam_tegenstander")
                        ),
                        records.getString("toestand_type")
                ));
            }
            records.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public static ArrayList<Game> selectGames(User user) {
        ArrayList<Game> games = new ArrayList<>();
        ResultSet records = database.select(
                SQL.SELECT.SELECTGAMESFORUSER,
                Arrays.asList(
                        user.getName(),
                        user.getName()));
        try {
            while (records.next()) {
                games.add(new Game(
                        records.getInt("id"),
                        Arrays.asList(
                                records.getString("account_naam_uitdager"),
                                records.getString("account_naam_tegenstander")
                        ),
                        records.getString("toestand_type")
                ));
            }
            records.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }



    private static Timestamp currentTimeStamp() {
        return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }
}
