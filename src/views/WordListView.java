package views;


import enumerations.Role;
import enumerations.WordStatus;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import models.Word;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class WordListView extends View implements Observer {
    @FXML
    private ListView<Word> myWordList;
    @FXML
    private ListView<Word> acceptedWordList;
    @FXML
    private ListView<Word> pendingWordList;
    @FXML
    private ListView<Word> deniedWordlist;
    @FXML
    private TitledPane acceptedWordPane;
    @FXML
    private TitledPane pendingWordPane;
    @FXML
    private TitledPane deniedWordPane;
    @FXML
    private TitledPane myWordPane;
    @FXML
    private Accordion accordion;

    public void refresh() {
        showComponents();
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        session.getCurrentUser().getRoles().addListener((ListChangeListener<? super Role>) (observable) -> {
            showComponents();
        });
        wordController.addObserver(this);
        showComponents();
        accordion.setExpandedPane(myWordPane);
        myWordList.setItems(wordController.getUserWords(session.getCurrentUser()));

        myWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            select(newValue);
        });
        acceptedWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            select(newValue);
        });
        deniedWordlist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            select(newValue);
        });
        pendingWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            select(newValue);
        });

    }

    private void filter() {
        acceptedWordList.setItems(wordController.getWords(WordStatus.ACCEPTED));
        pendingWordList.setItems(wordController.getWords(WordStatus.PENDING));
        deniedWordlist.setItems(wordController.getWords(WordStatus.DENIED));
    }

    private void showComponents() {
        if (session.getCurrentUser().hasRole(Role.MODERATOR)) {
            if (!accordion.getPanes().contains(acceptedWordPane)) {
                accordion.getPanes().addAll(acceptedWordPane, pendingWordPane, deniedWordPane);
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

    @Override
    public void update(Observable o, Object arg) {
        filter();
    }
}


