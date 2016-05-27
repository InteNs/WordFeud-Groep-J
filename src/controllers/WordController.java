package controllers;

import enumerations.WordStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        this.wordList.setAll(wordDAO.getWords());
    }

    @Override
    public void refill() {

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

