package views.subviews;

import enumerations.Language;
import enumerations.WordStatus;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;
import models.Word;
import views.MainView;

import java.util.ArrayList;

public class SubmitWordView {
    private ArrayList<CheckBox> cbList;
    private Language gameLanguage;
    private User owner;
    private Stage window = new Stage();
    private ArrayList<Word> wordList = new ArrayList<>();

    public SubmitWordView(ArrayList<String> words, ArrayList<String> existingWords, Language gameLanguage, User user, MainView mainView) {
        this.gameLanguage = gameLanguage;
        owner = user;
        cbList = new ArrayList<>();

        window.initModality(Modality.WINDOW_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        TitledPane titledPane = new TitledPane();
        titledPane.setCollapsible(false);
        titledPane.setText("Woorden indienen");
        titledPane.setTextAlignment(TextAlignment.CENTER);

        VBox rootVbox = new VBox();
        rootVbox.setPadding(new Insets(25, 25, 25, 25));

        window.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) window.close();
        });

        Label label1 = new Label();
        if (words.size() > 1) {
            label1.setText("Deze woorden zijn niet geldig! \n" +
                    "Selecteer de woorden die je wil indienen.");
        } else if (words.size() == 1) {
            label1.setText(("Dit woord niet geldig! \n" +
                    "Selecteer het als je het wil indienen."));
        }
        label1.setTextAlignment(TextAlignment.CENTER);

        rootVbox.getChildren().add(label1);
        rootVbox.setAlignment(Pos.CENTER);
        for (String w : words) {
            Label label = new Label(w.toLowerCase());
            label.setPadding(new Insets(10, 10, 10, 10));
            CheckBox checkBox = new CheckBox();
            checkBox.setUserData(w);
            checkBox.setPadding(new Insets(10, 0, 0, 10));
            label.setPadding(new Insets(10, 0, 0, 10));
            HBox wordBox = new HBox(label, checkBox);
            wordBox.setAlignment(Pos.CENTER);
            rootVbox.getChildren().addAll(wordBox);
            cbList.add(checkBox);
        }

        if(words.size() > 0 && existingWords.size() > 0){
            final Separator separator = new Separator();
            separator.setPadding(new Insets(10,0,10,0));
            rootVbox.getChildren().add(separator);
        }

        if (existingWords.size() > 0) {
            Label lExistingWords = new Label("De volgende woorden zijn ongeldig en al ingediend");
            lExistingWords.setAlignment(Pos.CENTER);
            rootVbox.getChildren().add(lExistingWords);
            for (String s : existingWords) {
                Label lWord = new Label(s.toLowerCase());
                HBox wordBox = new HBox(lWord);
                wordBox.setAlignment(Pos.CENTER);
                rootVbox.getChildren().add(lWord);
            }

        }

        Button bConfirm = new Button("Bevestig");
        HBox buttonBox = new HBox(bConfirm);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 10, 0));

        rootVbox.getChildren().add(buttonBox);
        titledPane.setContent(rootVbox);
        Scene scene = new Scene(titledPane);
        bConfirm.setOnAction(event -> submitWords());

        window.setScene(scene);
        window.setX(mainView.gameBoardView.getScene().getWindow().getX() + mainView.gameBoardView.getScene().getWidth() / 2 + 85);
        window.setY(mainView.gameBoardView.getScene().getWindow().getY() + mainView.gameBoardView.getHeight() / 2);
        window.showAndWait();


    }

    public ArrayList<String> submitWords() {
        ArrayList<String>  result = new ArrayList<>();
        for (CheckBox cb : cbList) {
            if (cb.isSelected()) {
                result.add(cb.getUserData().toString().toLowerCase());
            }
        }
        window.close();
        return result;
    }

    public ArrayList<Word> getWordList() {
        return wordList;
    }
}