package views;


import enumerations.Role;
import enumerations.WordStatus;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import models.User;
import models.Word;

import java.util.function.Predicate;

public class WordListView extends View {
    @FXML private ListView<Word> myWordList;
    @FXML private ListView<Word> acceptedWordList;
    @FXML private ListView<Word> pendingWordList;
    @FXML private ListView<Word> deniedWordlist;
    @FXML private TitledPane acceptedWordPane;
    @FXML private TitledPane pendingWordPane;
    @FXML private TitledPane deniedWordPane;
    @FXML private TitledPane myWordPane;
    @FXML private Accordion accordion;
    @FXML private TextField filterField;

    private FilteredList<Word> filteredUserWords;
    private FilteredList<Word> filteredPendingWords;
    private FilteredList<Word> filteredAcceptedWords;
    private FilteredList<Word> filteredDeniedWords;
    private Predicate<Word> filterText;

    public void refresh() {
        showComponents();
        filter();
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        session.getCurrentUser().getRoles().addListener((ListChangeListener<? super Role>) (observable) -> {
            showComponents();
        });
        showComponents();
        accordion.setExpandedPane(myWordPane);
        filteredUserWords = new FilteredList<>(wordController.getWords(session.getCurrentUser()));
        filteredAcceptedWords = new FilteredList<>(wordController.getWords(WordStatus.ACCEPTED));
        filteredPendingWords = new FilteredList<>(wordController.getWords(WordStatus.PENDING));
        filteredDeniedWords = new FilteredList<>(wordController.getWords(WordStatus.DENIED));
        if(accordion.getPanes().contains(acceptedWordPane)){
            acceptedWordList.setItems(filteredAcceptedWords);
            pendingWordList.setItems(filteredPendingWords);
            deniedWordlist.setItems(filteredDeniedWords);

        }
        myWordList.setItems(filteredUserWords);
        myWordList.setOnMouseClicked(event -> select(myWordList.getSelectionModel().getSelectedItem()));
        acceptedWordList.setOnMouseClicked(event -> select(acceptedWordList.getSelectionModel().getSelectedItem()));
        deniedWordlist.setOnMouseClicked(event -> select(deniedWordlist.getSelectionModel().getSelectedItem()));
        pendingWordList.setOnMouseClicked(event -> select(pendingWordList.getSelectionModel().getSelectedItem()));

        filterText = Word -> Word.getWord().contains(filterField.getText().toLowerCase());
        filterField.textProperty().addListener(observable -> {
            filteredUserWords.setPredicate(null);
            filteredUserWords.setPredicate(filterText);
            filteredAcceptedWords.setPredicate(null);
            filteredAcceptedWords.setPredicate(filterText);
            filteredPendingWords.setPredicate(null);
            filteredPendingWords.setPredicate(filterText);
            filteredDeniedWords.setPredicate(null);
            filteredDeniedWords.setPredicate(filterText);
        });
    }

    private void filter() {

        acceptedWordList.setItems(filteredAcceptedWords);
        pendingWordList.setItems(filteredPendingWords);
        deniedWordlist.setItems(filteredDeniedWords);
    }

    private void showComponents() {
        if (session.getCurrentUser().hasRole(Role.MODERATOR)) {
            if (!accordion.getPanes().contains(acceptedWordPane)) {
                accordion.getPanes().addAll(acceptedWordPane, pendingWordPane, deniedWordPane);
                acceptedWordList.setItems(filteredAcceptedWords);
                pendingWordList.setItems(filteredPendingWords);
                deniedWordlist.setItems(filteredDeniedWords);
            }
        } else {
            accordion.getPanes().removeAll(acceptedWordPane, pendingWordPane, deniedWordPane);

        }
    }

    public void HandleKeyEvent(KeyEvent ke) {
        if (ke.getCode().toString().equals("Y")) {
            parent.getWordInfoView().acceptWord();
            pendingWordList.getSelectionModel().selectNext();
        } else if (ke.getCode().toString().equals("N")) {
            parent.getWordInfoView().declineWord();
            pendingWordList.getSelectionModel().selectNext();
        }
    }

    public void select(Word word) {
        wordController.setSelectedWord(word);
        parent.setContent(parent.wordInfoView);

    }
}


