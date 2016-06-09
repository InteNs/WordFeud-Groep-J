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
        ResultSet records = null;
        try {
            records = database.select(SQL.SELECT.MESSAGESFORGAME, game.getId());
            while (records.next()) {
                messages.add(new Message(
                        new User(records.getString("account_naam")),
                        records.getString("bericht"),
                        records.getTimestamp("tijdstip")));
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
        return messages;
    }

    public ArrayList<Game> selectGames() {
        ArrayList<Game> games = new ArrayList<>();
        ResultSet records = null;
        try {
            records = database.select(SQL.ALL.GAMES);
            while (records.next()) {
                Game game = new Game(
                        records.getInt("id"),
                        records.getInt("last_turn"),
                        records.getInt("competitie_id"),
                        new User(records.getString("account_naam_uitdager")),
                        new User(records.getString("account_naam_tegenstander")),
                        GameState.parse(records.getString("toestand_type")),
                        BoardType.parse(records.getString("bord_naam")),
                        Language.parse(records.getString("letterset_naam")),
                        ReactionType.parse(records.getString("reaktie_type"))
                );
                if (games.contains(game))
                    game = games.get(games.indexOf(game));
                else
                    games.add(game);

                if (game.getChallenger().getName().equals(records.getString("account_naam"))) {
                    game.setChallengerScore(records.getInt("totaalscore"));
                } else if (game.getOpponent().getName().equals(records.getString("account_naam"))) {
                    game.setOpponentScore(records.getInt("totaalscore"));
                }
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
        return games;
    }

    public ArrayList<Tile> selectLettersForPot(Game selectedGame) {
        ArrayList<Tile> returnList = new ArrayList<>();
        ResultSet records = null;
        try {
            records = database.select(SQL.SELECT.LETTERSFORGAME, selectedGame.getId());
            while (records.next()) {
                returnList.add(new Tile(
                        records.getInt("id"),
                        records.getInt("waarde"),
                        records.getString("lettertype_karakter").charAt(0)
                ));
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
        return returnList;
    }

    /**
     * fetches all the turns for a game from the database
     * fetches the tiles with proper ID as well
     *
     * @param game the game that has to be loaded
     * @return the turns for the given game
     */
    public ArrayList<Turn> selectTurns(Game game) {
        ArrayList<Turn> turns = new ArrayList<>();
        ArrayList<Tile> tiles = new ArrayList<>();
        ResultSet records = null;
        ResultSet tileRecords = null;
        try {
            tileRecords = database.select(SQL.SELECT.LETTERSFORGAME, game.getId());
            records = database.select(SQL.SELECT.TURNSFORGAME, game.getId());
            //first fetch all the tiles
            while (tileRecords.next()) {
                tiles.add(new Tile(
                        tileRecords.getInt("id"),
                        tileRecords.getInt("waarde"),
                        tileRecords.getString("lettertype_karakter").charAt(0)
                ));
            }
            game.setAllTiles(tiles);

            while (records.next()) {
                // fetch the new turn
                Turn turn = new Turn(
                        game.getId(),
                        records.getInt("beurt"),
                        records.getInt("score"),
                        new User(records.getString("account_naam")),
                        TurnType.getFor(records.getString("aktie_type"))
                );
                // if the turn was previously fetched use the previous one from here on out
                if (turns.contains(turn))
                    turn = turns.get(turns.indexOf(turn));
                else
                    turns.add(turn);

                // find the placed tiles from the turn in the fetched tiles and assign
                if (records.getInt("gelegd_id") > 0) {
                    for (Tile tile : tiles) {
                        if (tile.getId() == records.getInt("gelegd_id")) {
                            if (!turn.hasPlacedTile(tile)) {
                                tile.setX(records.getInt("tegel_x") - 1);
                                tile.setY(records.getInt("tegel_y") - 1);
                                // handle joker
                                if (records.getString("blancoletterkarakter") != null)
                                    tile.replaceJoker(records.getString("blancoletterkarakter").charAt(0));
                                turn.addPlacedTile(tile);
                            }
                            break;
                        }
                    }
                }
                // find the rack tiles from the turn in fetched tiles and assign
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
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records, tileRecords))
                printError(e);
        }
        return turns;
    }

    /**
     * fetches the empty board for a game
     *
     * @param boardType the layout to fetch
     * @return returns a 2D field array
     */
    public Field[][] selectFieldsForBoard(BoardType boardType) {
        Field[][] fields = new Field[15][15];
        ResultSet records = null;
        try {
            records = database.select(SQL.SELECT.TILESFORBOARD, boardType.toString());
            while (records.next()) {
                int x = records.getInt("x") - 1;
                int y = records.getInt("y") - 1;
                fields[(y)][x] = new Field(FieldType.parse(records.getString("tegeltype_soort")), x, y);
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
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

    /**
     * looks up words for validating played words
     *
     * @param game               the game to check words for ( uses the language )
     * @param wordsFoundThisTurn a string array of words it needs to check
     * @return returns an arraylist with pairs of strings, and a boolean whether it was found or not
     */
    public ArrayList<Pair<String, Boolean>> selectWords(Game game, ArrayList<String> wordsFoundThisTurn) {
        ArrayList<Pair<String, Boolean>> results = new ArrayList<>();
        wordsFoundThisTurn.forEach(s -> {
            results.add(new Pair<>(s, database.count(SQL.COUNT.COUNTWORDS, s, game.getLanguage().toString()) > 0));
        });
        return results;
    }

    /**
     * inserts new turn into database
     *
     * @param game the current game
     * @param turn the turn to be inserted
     */
    public void insertTurn(Game game, Turn turn) {
        // first insert the turn itself
        database.insert(SQL.INSERT.INSERTTURN,
                turn.getId(),
                game.getId(),
                turn.getUser().getName(),
                turn.getScore(),
                TurnType.format(turn.getType())
        );

        if (!turn.getRack().isEmpty()) {
            // if the turn has tiles on rack, build the query for insertion
            StringBuilder insertRackQuery = new StringBuilder(SQL.INSERT.INSERTRACKTILES);
            ArrayList<Object> insertRackvalues = new ArrayList<>();
            turn.getRack().forEach(tile -> {
                // for every tile add to query and add the values
                insertRackQuery.append("(?,?,?),");
                insertRackvalues.addAll(Arrays.asList(game.getId(), tile.getId(), turn.getId()));
            });
            // end the query
            insertRackQuery.deleteCharAt(insertRackQuery.length() - 1);
            insertRackQuery.append((";"));

            database.insert(insertRackQuery.toString(), insertRackvalues);
        }

        switch (turn.getType()) {
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
                insertPlacedQuery.deleteCharAt(insertPlacedQuery.length() - 1);
                insertPlacedQuery.append(";");
                database.insert(insertPlacedQuery.toString(), insertPlacedValues);
                break;
        }
    }

    /**
     * creates a new pot for a new game
     *
     * @param selectedGame the given game
     */
    public void createPot(Game selectedGame) {
        StringBuilder insertLettersForPotQuery = new StringBuilder(SQL.INSERT.LETTERSFORPOT);
        ArrayList<Object> insertLettersForPotValues = new ArrayList<>();
        ResultSet records = null;

        try {
            // first fetch the needed tile info from 'lettertype'
            records = database.select(SQL.SELECT.LETTERSFORNEWGAME, selectedGame.getLanguage().toString());
            int idCounter = 1;
            while (records.next()) {
                //loop through amount and build the query for the pot
                int amountOfLetter = records.getInt("aantal");
                for (int i = 0; i < amountOfLetter; i++) {
                    insertLettersForPotQuery.append("(?,?,?,?),");
                    insertLettersForPotValues.addAll(Arrays.asList(
                            idCounter,
                            selectedGame.getId(),
                            selectedGame.getLanguage().toString(),
                            records.getString("karakter")));
                    idCounter++;
                }
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, records))
                printError(e);
        }
        insertLettersForPotQuery.deleteCharAt(insertLettersForPotQuery.length() - 1);
        insertLettersForPotQuery.append(";");
        database.insert(insertLettersForPotQuery.toString(), insertLettersForPotValues);
    }

    public void createGame(int compId, String requester, Language language, String receiver) {
        database.insert(SQL.INSERT.CREATEGAME, compId, requester, language.toString(), receiver);
    }

    public void updateGameState(GameState gameState, Game selectedGame) {
        database.update(SQL.UPDATE.UPDATEGAMESTATE, GameState.format(gameState), selectedGame.getId());
    }

    public void updateReactionType(ReactionType reactionType, Game selectedGame) {
        database.update(SQL.UPDATE.UPDATEREACTIONTYPE, ReactionType.format(reactionType), selectedGame.getId());
    }

    /**
     * fetches the current pot for a game
     *
     * @param selectedGame the game
     * @return returns arrayList of tiles for the pot
     */
    public ArrayList<Tile> selectPot(Game selectedGame) {
        ArrayList<Tile> returnList = new ArrayList<>();
        ResultSet result = null;
        try {
            result = database.select(SQL.SELECT.POT,selectedGame.getId(), selectedGame.getLanguage().toString());
            while (result.next()){
                returnList.add(new Tile(result.getInt("letter_id"),
                        result.getInt("waarde"),
                        result.getString("karakter").charAt(0)));
            }
        } catch (SQLException | NullPointerException e) {
            if (!recordsAreNull(e, result))
                printError(e);
        }
        return returnList;
    }
}
