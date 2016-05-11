package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CreateCompetitionView extends View {

    @FXML
    private Label invalidCompetitionNameLabel;
    @FXML
    private TextField competitionName;
    @FXML
    private Label competitionNameReq;

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
    }

    public void createCompetition() {
        if (checkCompetitionName(competitionName.getText())) {
            if (!competitionController.createCompetition(competitionName.getText())) {
                invalidCompetitionNameLabel.setTextFill(Color.web("#f50202"));
                invalidCompetitionNameLabel.setText("Er is al een competitie aangemaakt met dit account!");
                invalidCompetitionNameLabel.setVisible(true);
                return;
            }
            invalidCompetitionNameLabel.setTextFill(Color.web("#00ff00"));
            invalidCompetitionNameLabel.setText("Competitie: " + competitionName.getText() + " succesvol aangemaakt!");
            invalidCompetitionNameLabel.setVisible(true);
        }
    }

    private boolean checkCompetitionName(String competitionName) {
        if (competitionController.isValidCompetitionName(competitionName)) {
            invalidCompetitionNameLabel.setVisible(false);
            return true;
        }
        this.competitionName.clear();
        invalidCompetitionNameLabel.setText("Competitienaam voldoet niet aan de bovenstaande eisen");
        invalidCompetitionNameLabel.setVisible(true);
        return false;
    }
}
