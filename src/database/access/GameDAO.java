package database.access;

import database.SQL;
import enumerations.*;
import javafx.util.Pair;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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
        return messages;
    }

    public ArrayList<Game> selectGames() {
        ResultSet records = database.select(SQL.ALL.GAMES);
        ArrayList<Game> games = new ArrayList<>();
        try {
            while (records.next()) {
                games.add(new Game(
                        records.getInt("id"),
                        records.getInt("last_turn"),
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
        return games;
    }

    public ArrayList<Turn> selectTurns(Game game) {
        ArrayList<Turn> turns = new ArrayList<>();
        ArrayList<Tile> tiles = new ArrayList<>();
        ResultSet records = database.select(SQL.SELECT.TURNSFORGAME, game.getId());
        ResultSet tileRecords = database.select(SQL.SELECT.LETTERSFORGAME, game.getId());
        if (records == null || tileRecords == null) return null;
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
                        if (tile.getId() == records.getInt("gelegd_id")) {
                            if (!turn.hasPlacedTile(tile)) {
                                tile.setX(records.getInt("tegel_x") - 1);
                                tile.setY(records.getInt("tegel_y") - 1);
                                if (records.getString("blancoletterkarakter") != null)
                                    tile.replaceJoker(records.getString("blancoletterkarakter").charAt(0));
                                turn.addPlacedTile(tile);
                            }
                            break;
                        }
                    }
                }
                if (records.getInt("plank_id") > 0) {
                    for (Tile tile : tiles) {
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
        game.setPot(tiles);
        return turns;
    }

    public Field[][] selectFieldsForBoard(BoardType boardType) {
        Field[][] fields = new Field[15][15];
        ResultSet records = database.select(SQL.SELECT.TILESFORBOARD, boardType.toString());
        try {
            while (records.next()) {
                int x = records.getInt("x") - 1;
                int y = records.getInt("y") - 1;
                fields[(y)][x] = new Field(FieldType.parse(records.getString("tegeltype_soort")), x, y);
            }
        } catch (SQLException e) {
            printError(e);
        }
        return fields;
    }

    public void insertMessage(Game selectedGame, User currentUser, String text) {
        database.insert(SQL.INSERT.INSERTMESSAGE,
                selectedGame.getId(),
                currentUser.getName(),
                text
        );
    }

    public ArrayList<Pair<String, Boolean>> selectWords(Game game, ArrayList<String> wordsFoundThisTurn) {
        ArrayList<Pair<String, Boolean>> results = new ArrayList<>();
        wordsFoundThisTurn.forEach(s -> {
            results.add(new Pair<>(s, database.count(SQL.COUNT.COUNTWORDS, s, game.getLanguage().toString()) > 0));
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
        if (!turn.getRack().isEmpty()){
            StringBuilder insertRackQuery = new StringBuilder(SQL.INSERT.INSERTRACKTILES);
            ArrayList<Object> insertRackvalues = new ArrayList<>();
            turn.getRack().forEach(tile -> {
                insertRackQuery.append("(?,?,?),");
                insertRackvalues.addAll(Arrays.asList(game.getId(), tile.getId(), turn.getId()));
            });
            insertRackQuery.deleteCharAt(insertRackQuery.length()-1);
            insertRackQuery.append((";"));

            database.insert(insertRackQuery.toString(), insertRackvalues);
        }

        switch (turn.getType()) {
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
                StringBuilder insertPlacedQuery = new StringBuilder(SQL.INSERT.INSERTPLACEDTILES);
                ArrayList<Object> insertPlacedValues = new ArrayList<>();
                turn.getPlacedTiles().forEach(tile -> {
                    insertPlacedQuery.append("(?,?,?,?,?,?,?),");
                    insertPlacedValues.addAll(Arrays.asList(
                            tile.getId(),
                            game.getId(),
                            turn.getId(),
                            tile.getX() + 1,
                            tile.getY() + 1,
                            BoardType.format(game.getBoardType()),
                            tile.getReplacedJokerAsString())
                    );
                });
                insertPlacedQuery.deleteCharAt(insertPlacedQuery.length()-1);
                insertPlacedQuery.append(";");
                database.insert(insertPlacedQuery.toString(), insertPlacedValues);
                break;
            case UNDEFINED:
                break;
        }

    }

    public void createGame(int compId, String requester, Language language, String receiver) {
        database.insert(SQL.INSERT.CREATEGAME, compId, requester, language.toString(), receiver);
    }

    public void updateGameState(GameState gameState, Game selectedGame){
        database.update(SQL.UPDATE.UPDATEGAMESTATE, GameState.format(gameState), selectedGame.getId());
    }
}
