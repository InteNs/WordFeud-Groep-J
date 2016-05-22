package views;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.chart.XYChart;
import models.Competition;

public class CompetitionInfoView extends View {

    private int totalGames;
    @FXML
    private Label competitionName;
    @FXML private Label competitionInfo;
    @FXML private BarChart<String,Integer> Chart;
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
        int totalPlayers = competition.getAmountOfPlayers();
        int averageScoreInComp = competition.getCompetitionScoreAvgerage();
        joinButton.setVisible(true);

        Chart.getData().clear();
        Chart.setBarGap(10);
        Chart.setVerticalGridLinesVisible(false);
        Chart.setAnimated(false);
        XYChart.Series<String,Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Totaal spellen", totalGames));
        series.getData().add(new XYChart.Data<>("Totaal spelers", totalPlayers));
        series.getData().add(new XYChart.Data<>("Gemiddelde score / 10", (int)(0.1* averageScoreInComp)));
        Chart.getData().add(series);
        if (competitionController.isUserInCompetition(session.getCurrentUser(), competition)) {
            joinButton.setVisible(false);
        }
    }

    @FXML public void joinCompetition(){
        if (!competitionController.isUserInCompetition(session.getCurrentUser(), competitionController.getSelectedCompetition())) {
            if (competitionController.insertUserInCompetition(session.getCurrentUser(), competitionController.getSelectedCompetition())){
                refresh();
            }
        }
    }
    @Override
    public void refresh() {
        setInfo(competitionController.getSelectedCompetition());
    }
}