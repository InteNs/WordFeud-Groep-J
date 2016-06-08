package views;

import enumerations.Language;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.Competition;
import models.User;

import java.util.Objects;

public class ChallengeView extends View {

    @FXML
    private ComboBox<Language> languageBox;
    @FXML
    private Label challenge;
    @FXML
    private PieChart winLoseChart;
    @FXML
    private Label lNoStats;
    @FXML
    private Label lAvg_score;
    @FXML
    private Label lTotal_games;
    @FXML
    private Label lName;
    private Competition selectedComp;

    /**
     * This method calls the challenge method in gameController if the button was pressed after a language
     * was selected. Also calls the setFeedback method when the challenge method sends a return.
     */
    public void challenge() {
        if (!languageBox.getSelectionModel().isEmpty()) {
            int feedback = gameController.challenge(languageBox.getValue(),
                    session.getCurrentUser(),
                    userController.getSelectedUser(),
                    competitionController.getSelectedCompetition());
            setFeedback(feedback);
        } else {
            this.setFeedback(4);
        }
    }

    private void setFeedback(int feedback) {
        challenge.setVisible(true);
        challenge.setTextFill(Color.RED);
        switch (feedback) {
            case 0:
                challenge.setTextFill(Color.GREEN);
                challenge.setText("Succesvol uitgedaagd");
                break;
            case 1:
                challenge.setText("U kunt uzelf niet uitnodigen");
                break;
            case 2:
                challenge.setText("U speelt al een game met deze persoon");
                break;
            case 3:
                challenge.setText("U zit niet in dezelfde competitie");
                break;
            case 4:
                challenge.setText("Er is geen taal aangewezen");
                break;
        }
    }

    private void setStats(User selectedUser, Competition comp) {
        int wins = selectedUser.getStat(comp.getId()).getWins();
        int lost = selectedUser.getStat(comp.getId()).getLost();
        lName.setText(selectedUser.toString());
        lAvg_score.setText("Gemiddelde score: " + selectedUser.getStat(comp.getId()).getAvg_score());
        lTotal_games.setText("Totaal aantal spellen:" + selectedUser.getStat(comp.getId()).getTotal_games());
        FadeTransition ft = new FadeTransition(Duration.millis(1000), lNoStats);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        if (wins == 0 && lost == 0) {
            winLoseChart.setVisible(false);
            lNoStats.setVisible(true);
            ft.play();
        } else {
            winLoseChart.setOpacity(1);
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Gewonnen( "+ selectedUser.getStat(comp.getId()).getWins() + ")",
                                    selectedUser.getStat(comp.getId()).getWins()),
                            new PieChart.Data("Verloren("+ selectedUser.getStat(comp.getId()).getLost() + ")",
                                    selectedUser.getStat(comp.getId()).getLost()));
            winLoseChart.setVisible(true);
            winLoseChart.setData(pieChartData);
            lNoStats.setVisible(false);
        }
    }

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {
        challenge.setTextFill(Color.BLACK);
        challenge.setVisible(false);
    }

    @Override
    public void constructor() {
        userController.selectedUserProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue)) {
                selectedComp = competitionController.getSelectedCompetition();
                if (selectedComp != null) {
                    if (!selectedComp.hasUser(newValue)) return;
                    setStats(newValue, selectedComp);
                }
                clear();
            }
        });
        selectedComp = competitionController.getSelectedCompetition();
        languageBox.getItems().setAll(Language.NL, Language.EN);
    }
}