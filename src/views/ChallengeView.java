package views;

import enumerations.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import models.Competition;
import models.User;

public class ChallengeView extends View {

    private ObservableList<Language> languages = FXCollections.observableArrayList(Language.NL, Language.EN);

    @FXML
    private ComboBox<Language> languageBox;
    @FXML
    private Label challenge;

    public void challenge() {
        Competition comp = competitionController.getSelectedCompetition();
        User requester = session.getCurrentUser();
        User receiver = userController.getSelectedUser();

        if (!languageBox.getSelectionModel().isEmpty()) {
            if (!gameController.challenge(languageBox.getValue(), requester, receiver, comp)) {
                challenge.setTextFill(Color.RED);
                challenge.setText("Fout met uitnodigen");
            }
        } else {
            challenge.setTextFill(Color.RED);
            challenge.setText("Er is geen taal aangewezen");
        }
    }

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
        languageBox.setItems(languages);
    }

    public void setDefaultText() {
        challenge.setTextFill(Color.BLACK);
        challenge.setText("Uitdagen");
    }
}