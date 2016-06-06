package views;

import enumerations.Role;
import enumerations.WordStatus;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.Word;
import javafx.scene.paint.Color;

public class WordInfoView extends View {
    @FXML private Label labelWord;
    @FXML private Label labelLanguage;
    @FXML private Label labelStatus;
    @FXML private Label labelOwner;
    @FXML private Label labelSaved;
    @FXML private Button buttonAccept;
    @FXML private Button buttonDecline;
    @FXML private Label labelConfirm;
    @FXML private Pane pane;

    private Word selectedWord;


    public void refresh() {
        if (wordController.getSelectedWord() == null) return;
        if (session.getCurrentUser().hasRole(Role.MODERATOR) && selectedWord.getStatus() == WordStatus.PENDING) {
            buttonAccept.setVisible(true);
            buttonDecline.setVisible(true);

        } else {
            buttonAccept.setVisible(false);
            buttonDecline.setVisible(false);
        }
        if (!session.getCurrentUser().hasRole(Role.MODERATOR)) {
            labelOwner.setVisible(false);
        }
        labelStatus.setText("Status: " + WordStatus.format(selectedWord.getStatus()));

    }

    @Override
    public void clear() {

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

    private void setLabelColor() {
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

    public void acceptWord() {
        if (wordController.updateWordStatus(selectedWord, WordStatus.ACCEPTED)) {
            labelStatus.setTextFill(Color.web("green"));
            buttonAccept.setVisible(false);
            buttonDecline.setVisible(false);
            labelStatus.setVisible(true);
            labelConfirm.setText(selectedWord + " is geaccepteerd!");
            refresh();
            doAnimation();
            parent.reload();
        }

    }

    public void declineWord() {
        if (wordController.updateWordStatus(selectedWord, WordStatus.DENIED)) {
            labelStatus.setTextFill(Color.web("red"));
            buttonAccept.setVisible(false);
            buttonDecline.setVisible(false);
            labelStatus.setVisible(true);
            labelConfirm.setText(selectedWord + " is afgewezen!");
            refresh();
            doAnimation();
            parent.reload();
        }
    }

    private void doAnimation() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), labelConfirm);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setCycleCount(1);
        labelConfirm.setVisible(true);
        fadeOut.play();
    }

}


