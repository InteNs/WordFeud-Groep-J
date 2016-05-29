package database.access;

import database.SQL;
import enumerations.WordStatus;
import models.Word;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WordDAO extends DAO {

    public ArrayList<Word> getWords() {
        ResultSet rs = database.select(SQL.ALL.WORDS);
        ArrayList<Word> wordList = new ArrayList<>();
        try {
            while (rs.next()) {
                wordList.add(new Word(
                        rs.getString("woord"),
                        rs.getString("account_naam"),
                        rs.getString("letterset_code"),
                        WordStatus.parse(rs.getString("status"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wordList;
    }

    public boolean updateWordStatus(Word word, WordStatus status) {
        return database.update(SQL.UPDATE.UPDATEWORDSTATUS,
                status.toString().toLowerCase(),
                word.toString(),
                word.getOwner()
        );
    }
}