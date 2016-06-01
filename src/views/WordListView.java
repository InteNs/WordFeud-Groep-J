package views;


import enumerations.Role;
import enumerations.WordStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import models.Word;

import java.util.Observable;
import java.util.Observer;

public class WordListView extends View implements Observer {
    @FXML private ListView<Word> myWordList;
    @FXML private ListView<Word> acceptedWordList;
    @FXML private ListView<Word> pendingWordList;
    @FXML private ListView<Word> deniedWordlist;
    @FXML private TitledPane acceptedWordPane;
    @FXML private TitledPane pendingWordPane;
    @FXML private TitledPane deniedWordPane;
    @FXML private TitledPane myWordPane;
    @FXML private Accordion accordion;

    //private int listIndex;

    public void refresh() {
        filter();
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        wordController.addObserver(this);

        if (session.getCurrentUser().hasRole(Role.MODERATOR)) {

            if (!accordion.getPanes().contains(acceptedWordPane)) {
                accordion.getPanes().addAll(acceptedWordPane, pendingWordPane, deniedWordPane);
            }
        } else {
            accordion.getPanes().removeAll(acceptedWordPane, pendingWordPane, deniedWordPane);
        }
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


