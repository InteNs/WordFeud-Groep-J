package database.access;

import database.SQL;
import enumerations.*;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAO extends DAO {

    public ArrayList<Message> selectMessages(Game game) {
        ArrayList<Message> messages = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.MESSAGESFORGAME, game.getId());
        try {
            while (records.next()) {
                messages.add(new Message(
                        new User(records.getString("account_naam")),
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
    public ArrayList<Turn> selectTurns(Game game) {
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
    protected ArrayList<Tile> buildRack(String values) {
        ArrayList<Tile> rack = new ArrayList<>();
        if(values == null)
            return null;
        for (String s : values.split(",")) {
            rack.add(new Tile(s.charAt(0)));
        }
        return rack;
    }

    protected ArrayList<Tile> buildPlacedTiles(String woorddeel, String x, String y) {
        if(woorddeel == null)
            return null;
        ArrayList<Tile> tiles = new ArrayList<>();
        String[] sC = woorddeel.split(",");
        String[] sX = x.split(",");
        String[] sY = y.split(",");
        for (int i = 0; i < sC.length; i++) {
            tiles.add(new Tile(
                    sC[i].charAt(0),
                    Integer.parseInt(sX[i])-1,
                    Integer.parseInt(sY[i])-1
            ));
        }
        return tiles;
    }

    public ArrayList<Tile> selectPot(Language language) {
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

    public Field[][] selectFieldsForBoard(BoardType boardType) {
        Field[][] fields = new Field[15][15];
        ResultSet records = database.select(SQL.SELECT.TILESFORBOARD, boardType.toString());
        try {
            while (records.next()){
                int x=records.getInt("x")-1;
                int y = records.getInt("y")-1;
                fields[(y)][x] = new Field(FieldType.parse(records.getString("tegeltype_soort")),x,y);
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        return fields;
    }

    public void insertMessage(Game selectedGame, User currentUser, String text) {
        database.insert(SQL.INSERT.INSERTMESSAGE,
                selectedGame.getId(),
                currentUser.getName(),
                text
        );
    }
}
