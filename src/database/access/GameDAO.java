package database.access;

import database.SQL;
import enumerations.*;
import javafx.util.Pair;
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
                        records.getInt("competitie_id"),
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
        ArrayList<Tile> tiles = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.TURNSFORGAME, game.getId());
        ResultSet tileRecords = database.select(SQL.SELECT.LETTERSFORGAME, game.getId());
        try {
            while (tileRecords.next()) {
                tiles.add(new Tile(
                        tileRecords.getInt("id"),
                        tileRecords.getInt("waarde"),
                        tileRecords.getString("lettertype_karakter").charAt(0)
                ));
            }

            while (records.next()) {
                Turn turn = new Turn(
                        records.getInt("beurt"),
                        records.getInt("score"),
                        new User(records.getString("account_naam")),
                        TurnType.getFor(records.getString("aktie_type"))
                );
                if (turns.contains(turn))
                    turn = turns.get(turns.indexOf(turn));
                else
                    turns.add(turn);

                if (records.getInt("gelegd_id") > 0) {
                    for (Tile tile : tiles) {
                        if(tile.getId() == records.getInt("gelegd_id")) {
                            if(!turn.hasPlacedTile(tile)) {
                                tile.setX(records.getInt("tegel_x")-1);
                                tile.setY(records.getInt("tegel_y")-1);
                                if (records.getString("blancoletterkarakter") != null)
                                    tile.replaceJoker(records.getString("blancoletterkarakter").charAt(0));
                                turn.addPlacedTile(tile);
                            }
                            break;
                        }
                    }
                }
                if (records.getInt("plank_id") > 0) {
                    for (Tile tile: tiles) {
                        if (tile.getId() == records.getInt("plank_id")) {
                            if (!turn.hasRackTile(tile))
                                turn.addRackTile(tile);
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            printError(e);
        }
        database.close();
        game.setPot(tiles);
        return turns;
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

    public ArrayList<Pair<String,Boolean>> selectWords(Game game, ArrayList<String> wordsFoundThisTurn) {
        ArrayList<Pair<String,Boolean>> results = new ArrayList<>();
        wordsFoundThisTurn.forEach(s -> {
            results.add(new Pair<>(s,database.count(SQL.COUNT.COUNTWORDS,s,game.getLanguage().toString())> 0));
        });
        return results;
    }

    public void insertTurn(Game game, Turn turn) {
        database.insert(SQL.INSERT.INSERTTURN,
                turn.getId(),
                game.getId(),
                turn.getUser().getName(),
                turn.getScore(),
                TurnType.format(turn.getType())
        );

        game.getCurrentRack().forEach(tile ->
                database.insert(SQL.INSERT.INSERTRACKTILES,
                        game.getId(),
                        tile.getId(),
                        turn.getId())
        );

        switch (turn.getType()){
            case BEGIN:
                break;
            case END:
                break;
            case PASS:
                break;
            case RESIGN:
                break;
            case SWAP:
                break;
            case WORD:
                turn.getPlacedTiles().forEach(tile ->
                        database.insert(SQL.INSERT.INSERTPLACEDTILES,
                                tile.getId(),
                                game.getId(),
                                turn.getId(),
                                tile.getX()+1,
                                tile.getY()+1,
                                BoardType.format(game.getBoardType()),
                                tile.getReplacedJokerCharacter())
                        );
                break;
            case UNDEFINED:
                break;
        }
    }
}
