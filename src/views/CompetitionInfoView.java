package views;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Pair;
import models.Competition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CompetitionInfoView extends View {

    @FXML
    private Label competitionName;
    @FXML
    private Label competitionInfo;
    @FXML
    private Label gameChart;
    @FXML
    private Label playerChart;
    @FXML
    private BarChart<String, Integer> scoreChart;
    @FXML
    private Button joinButton;
    @FXML
    private Label name1, name2, name3, name4, name5;
    @FXML
    private Label games1, games2, games3, games4, games5;

    @Override
    public void constructor() {
        competitionController.selectedCompetitionProperty()
                .addListener((o, oldValue, newValue) -> {
                    if (!Objects.equals(oldValue, newValue) && newValue != null)
                        setInfo(newValue);
                });
    }

    private void setInfo(Competition competition) {
        competitionName.setText(competition.getName());
        competitionInfo.setText("Eigenaar: " + competition.getOwner().getName());
        joinButton.setVisible(!competitionController.isUserInCompetition(session.getCurrentUser(), competition));

        prepareChart(scoreChart, "Gemiddelde score", competition.getCompetitionScoreAvgerage());
        gameChart.setText("Aantal games: " + competition.getAmountOfGames());
        playerChart.setText("Aantal spelers: " + competition.getAmountOfUsers());
        setRanking(competition.getId());
    }

    private void setRanking(int id) {
        ArrayList<Pair<String, Integer>> topPlayers = competitionController.getTopPlayers(id);
        ArrayList<Label> names = new ArrayList<>(Arrays.asList(
                name1, name2, name3, name4, name5
        ));
        ArrayList<Label> games = new ArrayList<>(Arrays.asList(
                games1, games2, games3, games4, games5
        ));
        for (Label l : names) {
            l.setText("");
        }
        for (Label l : games) {
            l.setText("");
        }
        for (int i = 0; i < topPlayers.size(); i++) {
            names.get(i).setText(topPlayers.get(i).getKey());
            games.get(i).setText(topPlayers.get(i).getValue().toString());
        }
    }

    private void prepareChart(BarChart<String, Integer> chart, String label, int value) {
        chart.setVerticalGridLinesVisible(false);
        chart.setAnimated(false);
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(label, value));
        chart.getData().clear();
        chart.getData().add(series);
    }

    @FXML
    public void joinCompetition() {
        competitionController.addUserInCompetition(
                session.getCurrentUser(),
                competitionController.getSelectedCompetition()
        );
        refresh();
    }

    @Override
    public void refresh() {
        if (competitionController.getSelectedCompetition() != null)
            setInfo(competitionController.getSelectedCompetition());
    }

    @Override
    public void clear() {
        scoreChart.getData().clear();
        competitionInfo.setText("");
        competitionName.setText("");
        joinButton.setVisible(false);
    }

    @FXML
    public void showPlayers() {
        parent.setTab(parent.userListView);
    }

    @FXML
    public void showGames() {
        parent.setTab(parent.gameListView);
    }
}