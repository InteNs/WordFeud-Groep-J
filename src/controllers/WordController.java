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
    private ArrayList<String> invalidWordsList;

    public WordController() {
        super();
        words = FXCollections.observableArrayList();
        selectedWord = new SimpleObjectProperty<>();
        invalidWordsList = new ArrayList<>();
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

    /**
     * @param word to check if already exists in wordlist
     * @return true if word exists
     */
    public boolean wordInList(String word) {
        for (Word w : words) {
            if (w.getWord().equals(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getInvalidWordsList() {
        return invalidWordsList;
    }

    /**
     * @param words list of invalid words to be submitted
     * @return the list of words that is invalid and have not yet been submitted
     */
    public ArrayList<String> filterWords(ArrayList<String> words) {
        //First add al words to the wordsList and net filter the existingWords out of it.
        //So this methode returns a list with only the non existing words and creates
        //a list with the existing words so we can show both lists in the view
        ArrayList<String> result = new ArrayList<>();
        invalidWordsList.forEach(String::toLowerCase);
        for (String s : words) {
            invalidWordsList.add(s); //Add all submitted words
            if (wordInList(s)) { //if word is already submitted add it to the return list.
                result.add(s);
                invalidWordsList.remove(s); // remove it from the return list.
            }
        }
        return result;
    }

    public void submitWords(ArrayList<Word> words) {
        wordDAO.insertWords(words);
    }

    public Word createWord(String wordString,String letterSet) {
        return new Word(wordString, getSessionController().getCurrentUser().toString(), letterSet, WordStatus.PENDING);
    }
}