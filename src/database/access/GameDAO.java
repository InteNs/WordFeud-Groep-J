package database.access;

import database.SQL;
import models.Game;
import models.Message;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

public class GameDAO extends DAO {

    public static ArrayList<Message> selectMessages(Game game) {
        ArrayList<Message> messages = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.MESSAGESFORGAME, game.getID());
        try {
            while (records.next()) {
                messages.add(new Message(
                        records.getString("account_naam"),
                        records.getString("bericht"),
                        records.getTimestamp("tijdstip")));
            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return messages;
    }

    public static ArrayList<Game> selectGames() {
        ResultSet records = database.select(SQL.ALL.GAMES);
        ArrayList<Game> games = new ArrayList<>();
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
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return games;
    }
}
