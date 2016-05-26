package views;

import controllers.WordController;
import enumerations.GameState;
import enumerations.Role;
import enumerations.WordStatus;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import models.Competition;
import models.User;
import models.Word;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

        myWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                select(newValue));
        acceptedWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                select(newValue));
        deniedWordlist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                select(newValue));
        pendingWordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                select(newValue));

    }

    public void select(Word word) {
        wordController.setSelectedWord(word);
        parent.setContent(parent.wordInfoView);
    }
}


