package controllers;

import enumerations.Language;
import enumerations.WordStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;
import models.Word;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class WordController extends Controller {

    private ArrayList<Word> fetched;
    private ObjectProperty<Word> selectedWord;
    private ObservableList<Word> words;
    private ArrayList<String> existingWords;
    private ArrayList<String> wordsList;

    public WordController() {
        super();
        words = FXCollections.observableArrayList();
        selectedWord = new SimpleObjectProperty<>();
        existingWords = new ArrayList<>();
        wordsList = new ArrayList<>();
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
        if (!words.equals(fetched))
            words.setAll(fetched);
    }

    @Override
    public void fetch() {
        fetched = wordDAO.getWords();
        wordDAO.close();
    }

    public ObservableList<Word> getWords(WordStatus status) {

        return words.filtered(word -> word.getStatus() == status);
    }

    public ObservableList<Word> getUserWords(User user) {
        return words.filtered(word -> word.getOwner().equals(user.toString()));
    }

    public boolean updateWordStatus(Word word, WordStatus status) {
        wordDAO.updateWordStatus(word, status);
        word.setStatus(status);
        return true;
    }

    public boolean wordInList(String word) {
        for (Word w : words) {
            if (w.getWord().equals(word)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getWordsList() {
        return wordsList;
    }

    public ArrayList<String> existingWords(ArrayList<String> words) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : words) {
            wordsList.add(s.toLowerCase());
            if (wordInList(s.toLowerCase())) {
                result.add(s.toLowerCase());
                wordsList.remove(s.toLowerCase());
            }
        }
        return result;
    }

    public void submitWords(ArrayList<Word> words) {
        wordDAO.insertWords(words);
    }

    public Word createWord(String wordString, String owner, String letterSet, WordStatus status) {
        return new Word(wordString, owner, letterSet, status);
    }
}
