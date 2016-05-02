package database.access;

import database.SQL;
import enumerations.*;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAO extends DAO {

    public static ArrayList<Message> selectMessages(Game game) {
        ArrayList<Message> messages = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.MESSAGESFORGAME, game.getId());
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
                        new User(records.getString("account_naam_uitdager")),
                        new User(records.getString("account_naam_tegenstander")),
                        GameState.stateFor(records.getString("toestand_type")),
                        BoardType.boardTypeFor(records.getString("bord_naam")),
                        Language.languageFor(records.getString("letterset_naam"))
                ));
            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return games;
    }
    public static ArrayList<Turn> selectTurns(Game game) {
        ArrayList<Turn> turns = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.TURNSFORGAME, game.getId());
        try {
            while (records.next()) {
                turns.add(new Turn(
                        records.getInt("score"),
                        new User(records.getString("account_naam")),
                        TurnType.getFor(records.getString("aktie_type")),
                        buildPlacedTiles(
                                records.getString("woorddeel"),
                                records.getString("x-waarden"),
                                records.getString("y-waarden")
                        ),
                        buildRack(records.getString("inhoud"))
                ));
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return turns;
    }
    protected static ArrayList<Tile> buildRack(String values) {
        ArrayList<Tile> rack = new ArrayList<>();
        if(values == null)
            return null;
        for (String s : values.split(",")) {
            rack.add(new Tile(s.charAt(0)));
        }
        return rack;
    }

    protected static ArrayList<Tile> buildPlacedTiles(String woorddeel, String x, String y) {
        if(woorddeel == null)
            return null;
        ArrayList<Tile> tiles = new ArrayList<>();
        String[] sC = woorddeel.split(",");
        String[] sX = x.split(",");
        String[] sY = y.split(",");
        for (int i = 0; i < sC.length; i++) {
            tiles.add(new Tile(
                    sC[i].charAt(0),
                    Integer.parseInt(sX[i]),
                    Integer.parseInt(sY[i])
            ));
        }
        return tiles;
    }

    public static ArrayList<Tile> selectTiles(Language language) {
        ArrayList<Tile> tiles = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.LETTERSFORLANG, language.toString());
        try {
            while (records.next()){
                for (int i = 0; i <records.getInt("aantal") ; i++) {
                    tiles.add(new Tile(
                            records.getInt("waarde"),
                            records.getString("karakter").charAt(0)
                    ));
                }
            }
        } catch (Exception e) {
            printError(e);
        }
        database.close();
        return tiles;
    }

    public static Field[][] selectFieldsForBoard(BoardType boardType) {
        Field[][] fields = new Field[15][15];
        ResultSet records = database.select(SQL.SELECT.TILESFORBOARD,boardType.toString());

        try {
            while (records.next()){
                int x=records.getInt("x")-1;
                int y = records.getInt("y")-1;
                fields[x][y] =
                        new Field(FieldType.fieldTypeFor(records.getString("tegeltype_soort")));
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return fields;
    }
}
