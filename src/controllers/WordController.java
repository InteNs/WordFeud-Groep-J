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
    private ObservableList<Word> words;

    public WordController(ControllerFactory factory) {
        super(factory);
        words = FXCollections.observableArrayList();
        selectedWord = new SimpleObjectProperty<>();
    }

    public Word getSelectedWord() {
        return selectedWord.get();
    }

    public ObjectProperty<Word> selectedWordProperty() {
        return selectedWord;
    }

    public void setSelectedWord(Word selectedWord) {
        this.selectedWord.set(selectedWord);
    }

    @Override
    public void refresh() {
        if (words.contains(getSelectedWord())) setSelectedWord(words.get(words.indexOf(getSelectedWord())));
    }

    @Override
    public void refill() {
        words.setAll(wordDAO.getWords());
    }

    public ObservableList<Word> getWords(WordStatus status) {
        return words.filtered(word -> word.getStatus() == status);
    }

    public ObservableList<Word> getUserWords(User user) {
        return words.filtered(word -> word.getOwner().equals(user.toString()));
    }

    public ObservableList<Word> getWords() {
        return words;
    }

    public boolean updateWordStatus(Word word, WordStatus status) {
        if (wordDAO.updateWordStatus(word, status)) {
            word.setStatus(status);
            return true;
        }
        return false;
    }
}
