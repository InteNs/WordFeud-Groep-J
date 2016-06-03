package views.subviews;

import controllers.ControllerFactory;
import controllers.WordController;
import database.access.WordDAO;
import enumerations.Language;
import enumerations.WordStatus;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.User;
import models.Word;
import resources.ResourceFactory;
import views.View;

import javax.naming.spi.ResolveResult;
import java.awt.*;
import java.util.ArrayList;

public class SubmitWordView extends View {
    private ArrayList<CheckBox> cbList;
    private Language gameLanguage;
    private User owner;
    private Stage window = new Stage();
    private ArrayList<Word> wordList = new ArrayList<>();

    public SubmitWordView(ArrayList<String> words, Language gameLanguage, User user) {
        this.gameLanguage = gameLanguage;
        owner = user;
        cbList = new ArrayList<>();
        window.initModality(Modality.WINDOW_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        TilePane tilePane = new TilePane();
        tilePane.setMaxHeight(400);
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setStyle("-fx-background-color: white");
        tilePane.setOnMouseClicked(event -> window.close());
        window.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) window.close();
        });


        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        Label label1 = new Label("Selecteer de woorden die je wil indienen.");
        vbox.getChildren().add(label1);
        for (String w : words) {
            Label label = new Label(w.toLowerCase());
            CheckBox checkBox = new CheckBox();
            checkBox.setUserData(w);
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            checkBox.setPadding(new Insets(10, 0, 0, 10));
            label.setPadding(new Insets(10, 0, 0, 10));
            hbox.getChildren().addAll(label, checkBox);
            vbox.getChildren().add(hbox);
            cbList.add(checkBox);
        }
        Button bConfirm = new Button("Bevestig");
        vbox.getChildren().add(bConfirm);
        Scene scene = new Scene(vbox);
        bConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitWords();
            }
        });
        window.setScene(scene);
        window.showAndWait();
    }

    private void submitWords() {
        for (CheckBox cb : cbList) {
            if (cb.isSelected()) {
                Word word = new Word(cb.getUserData().toString().toLowerCase(), owner.getName(), gameLanguage.toString(), WordStatus.PENDING);
                wordList.add(word);
            }
            window.close();
        }
    }

    public ArrayList<Word> getWordList() {
        return wordList;
    }

    @Override
    public void refresh() {
    }


    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
    }
}
