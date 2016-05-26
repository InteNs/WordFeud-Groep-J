package database.access;

import database.Database;
import database.SQL;
import enumerations.WordStatus;
import models.User;
import models.Word;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WordDAO extends DAO {
    Database database = new Database();

    public ArrayList<Word> getWords() {
        ResultSet rs = database.select(SQL.ALL.WORDS);
        ArrayList<Word> wordList = new ArrayList<>();
        try {
            while (rs.next()) {
                Word word = new Word(rs.getString("woord"), rs.getString("account_naam"), rs.getString("letterset_code"), WordStatus.parse(rs.getString("status")));
                wordList.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wordList;
    }

    public void setUserWords(User user) {
        ResultSet rs = database.select(SQL.SELECT.SELECTUSERWORDS, user.getName());
        try {
            while (rs.next()) {
                user.addWord(new Word(rs.getString("woord"), rs.getString("account_naam"), rs.getString("letterset_code"), WordStatus.parse(rs.getString("status"))));
            }
        } catch (SQLException e) {
            printError(e);
        }
    }

    public boolean updateWordStatus(Word word,String status){
        return database.update(SQL.UPDATE.UPDATEWORDSTATUS,status,word.toString(),word.getOwner());
    }
}