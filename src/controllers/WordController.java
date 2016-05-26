package controllers;

import database.access.UserDAO;
import database.access.WordDAO;
import enumerations.Role;
import enumerations.WordStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;
import models.Word;
import views.WordInfoView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

public class WordController extends Controller {
    private ObjectProperty<Word> selectedWord;
    private ObservableList<Word> wordList;

    public WordController(ControllerFactory factory) {
        super(factory);
        wordList = FXCollections.observableArrayList();
        selectedWord = new SimpleObjectProperty<>();
    }


    @Override
    public void refresh() {
        this.wordList.setAll(wordDAO.getWords());
    }

    public void setCurrentWord(Word currentWord) {
        selectedWord.set(currentWord);
    }

    public void setWordList() {

    }

    public void setUserWords(User user) {
        wordDAO.setUserWords(user);
    }

    public ObservableList<Word> getWords(WordStatus status) {
        return wordList.filtered(word -> word.getStatus() == status);
    }

    public ObservableList<Word> getWords() {
        return wordList;
    }

    public ObjectProperty<Word> selectedWordProperty() {
        return selectedWord;
    }

    public boolean updateWordStatus(Word word, String status) {
        if (wordDAO.updateWordStatus(word, status)) {
            word.setStatus(WordStatus.parse(status));
            return true;
        } else {
            return false;
        }

    }

    public void setSelectedWord(Word selectedWord) {
        this.selectedWord.set(selectedWord);
    }
}

