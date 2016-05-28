package controllers;

import enumerations.WordStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;
import models.Word;

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
        if (wordList.contains(getSelectedWord())) setSelectedWord(wordList.get(wordList.indexOf(getSelectedWord())));
    }

    @Override
    public void refill() {
        wordList.setAll(wordDAO.getWords());
    }

    public ObservableList<Word> getWords(WordStatus status) {
        return wordList.filtered(word -> word.getStatus() == status);
    }

    public ObservableList<Word> getUserWords(User user) {
        return wordList.filtered(word -> word.getOwner().equals(user.toString()));
    }

    public ObservableList<Word> getWords() {
        return wordList;
    }

    public ObjectProperty<Word> getSelectedWord() {
        return selectedWord;
    }

    public boolean updateWordStatus(Word word, WordStatus status) {
        if (wordDAO.updateWordStatus(word, status)) {
            word.setStatus(status);
            return true;
        } else {
            return false;
        }

    }

    public void setSelectedWord(Word selectedWord) {
        this.selectedWord.set(selectedWord);
    }
}

