package views;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.chart.XYChart;
import models.Competition;

public class CompetitionInfoView extends View {

    private int totalGames;
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

    public void setInfo(Competition competition){
        competitionName.setText(competition.getName());
        competitionInfo.setText("Eigenaar: " + competition.getOwner().getName());
        totalGames = competition.getAmountOfGames();
        int totalPlayers = competition.getAmountOfUsers();
        int averageScoreInComp = competition.getCompetitionScoreAvgerage();
        joinButton.setVisible(true);

        //game chart
        gameChart.getData().clear();
        gameChart.setBarGap(10);
        gameChart.setVerticalGridLinesVisible(false);
        gameChart.setAnimated(false);
        XYChart.Series<String,Integer> gameSeries = new XYChart.Series<>();
        gameSeries.getData().add(new XYChart.Data<>("Totaal spellen", totalGames));
        gameChart.getData().add(gameSeries);

        //player chart
        playerChart.getData().clear();
        playerChart.setBarGap(10);
        playerChart.setVerticalGridLinesVisible(false);
        playerChart.setAnimated(false);
        XYChart.Series<String,Integer> playerSeries = new XYChart.Series<>();
        playerSeries.getData().add(new XYChart.Data<>("Totaal spelers", totalPlayers));
        playerChart.getData().add(playerSeries);

        //score chart
        scoreChart.getData().clear();
        scoreChart.setBarGap(10);
        scoreChart.setVerticalGridLinesVisible(false);
        scoreChart.setAnimated(false);
        XYChart.Series<String,Integer> scoreSeries = new XYChart.Series<>();
        scoreSeries.getData().add(new XYChart.Data<>("Gemiddelde score", averageScoreInComp));
        scoreChart.getData().add(scoreSeries);

        if (competitionController.isUserInCompetition(session.getCurrentUser(), competition)) {
            joinButton.setVisible(false);
        }
    }

    @FXML public void joinCompetition(){
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
}