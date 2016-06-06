package controllers;

import enumerations.WordStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;
import models.Word;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class WordController extends Controller {

    private ArrayList<Word> fetched;
    private ObjectProperty<Word> selectedWord;
    private ObservableList<Word> words;
    private ArrayList<String> wordsList;

    public WordController() {
        super();
        words = FXCollections.observableArrayList();
        selectedWord = new SimpleObjectProperty<>();
        wordsList = new ArrayList<>();
    }

    public Word getSelectedWord() {
        return selectedWord.get();
    }

    public void setSelectedWord(Word selectedWord) {
        this.selectedWord.set(selectedWord);
    }

    public ObjectProperty<Word> selectedWordProperty() {
        return selectedWord;
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

    private boolean isWordInList(String word) {
        return this.words.stream()
                .map(Word::getWord)
                .anyMatch(s -> s.equals(word));
    }

    public ArrayList<String> getWordsList() {
        return wordsList;
    }

    public ArrayList<String> existingWords(ArrayList<String> words) {
        ArrayList<String> result = new ArrayList<>();
        for (String word : words) {
            wordsList.add(word);
            if (isWordInList(word)) {
                result.add(word);
                wordsList.remove(word);
            }
        }
        return result;
    }

    public void submitWords(ArrayList<String> words) {
        wordDAO.insertWords(words.stream()
                .map(this::createWord)
                .collect(Collectors.toCollection(ArrayList<Word>::new)));
    }

    private Word createWord(String wordString) {
        return new Word(
                wordString.toLowerCase(),
                getSessionController().getCurrentUser().getName(),
                getGameController().getSelectedGame().getLanguage().toString(),
                WordStatus.PENDING);
    }
}
