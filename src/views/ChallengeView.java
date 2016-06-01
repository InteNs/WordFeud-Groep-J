package views;

import enumerations.Language;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import models.Competition;
import models.User;

public class ChallengeView extends View {

    @FXML private ComboBox<Language> languageBox;
    @FXML private Label challenge;

    public void challenge() {
        Competition comp = competitionController.getSelectedCompetition();
        User requester = session.getCurrentUser();
        User receiver = userController.getSelectedUser();

        if (!languageBox.getSelectionModel().isEmpty()) {
            int feedback = gameController.challenge(languageBox.getValue(), requester, receiver, comp);
            setFeedback(feedback);
        } else {
            this.setFeedback(4);
        }
    }

    private void setFeedback(int feedback) {
        switch (feedback) {
            case 0:
                challenge.setTextFill(Color.GREEN);
                challenge.setText("Succesvol uitgedaagd");
                break;
            case 1:
                challenge.setTextFill(Color.RED);
                challenge.setText("U kunt uzelf niet uitnodigen");
                break;
            case 2:
                challenge.setTextFill(Color.RED);
                challenge.setText("U speelt al een game met deze persoon");
                break;
            case 3:
                challenge.setTextFill(Color.RED);
                challenge.setText("U zit niet in dezelfde competitie");
                break;
            case 4:
                challenge.setTextFill(Color.RED);
                challenge.setText("Er is geen taal aangewezen");
                break;
        }
    }

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {
        challenge.setTextFill(Color.BLACK);
        challenge.setText("Uitdagen");
    }

    @Override
    public void constructor() {
        languageBox.getItems().setAll(Language.NL, Language.EN);
    }
}