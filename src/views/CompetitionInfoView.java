package views;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Competition;

public class CompetitionInfoView extends View {

    @FXML private Label competitionName;
    @FXML private Label competitionInfo;
    @FXML private BarChart<String,Integer> gameChart;
    @FXML private BarChart<String,Integer> playerChart;
    @FXML private BarChart<String,Integer> scoreChart;
    @FXML private Button joinButton;

    @Override
    public void constructor() {
       competitionController.selectedCompetitionProperty().addListener((observable, oldValue, newValue) -> {
           competitionController.setSelectedCompetition(newValue);
           setInfo(newValue);
        });
    }

    private void setInfo(Competition competition){
        competitionName.setText(competition.getName());
        competitionInfo.setText("Eigenaar: " + competition.getOwner().getName());

        prepareChart(gameChart, "Totaal spellen", competition.getAmountOfGames());
        prepareChart(playerChart, "Totaal spelers", competition.getAmountOfUsers());
        prepareChart(scoreChart, "Gemiddelde score", competition.getCompetitionScoreAvgerage());

        if (competitionController.isUserInCompetition(session.getCurrentUser(), competition)) {
            joinButton.setVisible(false);
        }
    }

    private void prepareChart(BarChart<String, Integer> chart, String label, int value) {
        chart.setVerticalGridLinesVisible(false);
        chart.setAnimated(false);
        XYChart.Series<String,Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(label , value));
        chart.getData().clear();
        chart.getData().add(series);
    }

    @FXML
    public void joinCompetition(){
        if (!competitionController.isUserInCompetition(session.getCurrentUser(), competitionController.getSelectedCompetition())) {
            if (competitionController.addUserInCompetition(session.getCurrentUser(), competitionController.getSelectedCompetition())){
                refresh();
            }
        }
    }
    @Override
    public void refresh() {
        setInfo(competitionController.getSelectedCompetition());
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