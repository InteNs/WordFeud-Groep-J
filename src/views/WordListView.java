package views;


import enumerations.Role;
import enumerations.WordStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import models.Word;

public class WordListView extends View {
    @FXML
    ListView<Word> myWordList;
    @FXML
    ListView<Word> acceptedWordList;
    @FXML
    ListView<Word> pendingWordList;
    @FXML
    ListView<Word> deniedWordlist;
    @FXML
    TitledPane acceptedWordPane;
    @FXML
    TitledPane pendingWordPane;
    @FXML
    TitledPane deniedWordPane;
    @FXML
    TitledPane myWordPane;
    @FXML
    Accordion accordion;

    private int listIndex;

    public void refresh() {

    }

    @Override
    public void constructor() {
        if (session.getCurrentUser().hasRole(Role.MODERATOR)) {
            acceptedWordList.setItems(wordController.getWords(WordStatus.ACCEPTED));
            pendingWordList.setItems(wordController.getWords(WordStatus.PENDING));
            deniedWordlist.setItems(wordController.getWords(WordStatus.DENIED));
            if (!accordion.getPanes().contains(acceptedWordPane)) {
                accordion.getPanes().addAll(acceptedWordPane, pendingWordPane, deniedWordPane);
            }
        } else {
            accordion.getPanes().removeAll(acceptedWordPane, pendingWordPane, deniedWordPane);
        }
        accordion.setExpandedPane(myWordPane);
        myWordList.setItems(wordController.getWords());

        myWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listIndex = (pendingWordList.getSelectionModel().getSelectedIndex());
            select(newValue);
        });
        acceptedWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            select(newValue);
        });
        deniedWordlist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listIndex = (pendingWordList.getSelectionModel().getSelectedIndex());
            select(newValue);
        });
        pendingWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listIndex = (pendingWordList.getSelectionModel().getSelectedIndex());
            select(newValue);
        });

    }

    public void HandleKeyEvent(KeyEvent ke) {
        if (ke.getCode().toString().equals("Y")) {
            parent.getWordInfoView().acceptWord();
            pendingWordList.getSelectionModel().select(listIndex + 1);
        } else if (ke.getCode().toString().equals("N")) {
            parent.getWordInfoView().declineWord();
            pendingWordList.getSelectionModel().select(listIndex + 1);
        }
    }

    public void select(Word word) {
        wordController.setSelectedWord(word);
        parent.setContent(parent.wordInfoView);
    }
}


