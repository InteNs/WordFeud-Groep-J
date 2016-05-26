package views;

import enumerations.Role;
import enumerations.WordStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Word;
import javafx.scene.paint.Color;

public class WordInfoView extends View {
    @FXML
    Label labelWord;
    @FXML
    Label labelLanguage;
    @FXML
    Label labelStatus;
    @FXML
    Label labelOwner;
    @FXML
    Label labelSaved;
    @FXML
    Button buttonAccept;
    @FXML
    Button buttonDecline;

    @FXML
    Pane pane;
    private Word selectedWord;

    public void refresh() {
        if (session.getCurrentUser().hasRole(Role.MODERATOR) && selectedWord.getStatus() == WordStatus.PENDING) {
            buttonAccept.setVisible(true);
            buttonDecline.setVisible(true);

        } else {
            buttonAccept.setVisible(false);
            buttonDecline.setVisible(false);
        }
        labelStatus.setText("Status: " + WordStatus.format(selectedWord.getStatus()));
        pane.isFocusTraversable();
        pane.isFocused();
        pane.requestFocus();

    }

    public void setLabelColor() {
        switch (selectedWord.getStatus()) {
            case ACCEPTED:
                labelStatus.setTextFill(Color.web("green"));
                break;
            case PENDING:
                labelStatus.setTextFill(Color.web("orange"));
                break;
            case DENIED:
                labelStatus.setTextFill(Color.web("red"));
                break;
        }

    }

    @Override
    public void constructor() {
        wordController.selectedWordProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            labelWord.setText(newValue.toString());
            labelLanguage.setText("Taal: " + newValue.getLetterset());
            labelStatus.setText("Status: " + WordStatus.format(newValue.getStatus()));
            labelOwner.setText("Gebruiker: " + newValue.getOwner());
            selectedWord = newValue;
            setLabelColor();
            refresh();
        });
    }

    public void acceptWord() {
        if (wordController.updateWordStatus(selectedWord, "accepted")) {
            labelStatus.setTextFill(Color.web("green"));
            buttonAccept.setVisible(false);
            buttonDecline.setVisible(false);
            labelStatus.setVisible(true);
            parent.refresh();
            refresh();
        }

    }

    public void declineWord() {
        if (wordController.updateWordStatus(selectedWord, "denied")) {
            labelStatus.setTextFill(Color.web("red"));
            buttonAccept.setVisible(false);
            buttonDecline.setVisible(false);
            labelStatus.setVisible(true);
            parent.refresh();
            refresh();
        }
    }

}


