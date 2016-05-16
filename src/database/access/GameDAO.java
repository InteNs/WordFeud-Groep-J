package database.access;

import database.SQL;
import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;
import models.Game;
import models.Letter;
import models.Message;
import models.User;

import java.sql.ResultSet;
import java.util.ArrayList;

public class GameDAO extends DAO {

    public ArrayList<Message> selectMessages(Game game) {
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

    public ArrayList<Game> selectGames() {
        ResultSet records = database.select(SQL.ALL.GAMES);
        ArrayList<Game> games = new ArrayList<>();
        try {
            while (records.next()) {
                games.add(new Game(
                        records.getInt("id"),
                        new User(records.getString("account_naam_uitdager")),
                        new User(records.getString("account_naam_tegenstander")),
                        GameState.parse(records.getString("toestand_type")),
                        BoardType.parse(records.getString("bord_naam")),
                        Language.parse(records.getString("letterset_naam"))
                ));
            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return games;
    }
    public ArrayList<Letter> selectLetters(Language language) {
        ArrayList<Letter>letters = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.SELECTLETTERS, language);
        try {
            while (records.next()){
                for (int i = 0; i <records.getInt("aantal") ; i++) {
                    letters.add(new Letter(
                            records.getInt("waarde"),
                            records.getString("karakter").charAt(0)
                    ));
                }
            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return letters;
    }
    
}
