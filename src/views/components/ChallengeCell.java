package views.components;

import controllers.CompetitionController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Game;
import models.User;

public class ChallengeCell extends ListCell<Game> {

    private final User currentUser;
    private final EventHandler<ActionEvent> handler;
    private CompetitionController competitionController;

    public ChallengeCell(User currentUser, EventHandler<ActionEvent> handler, CompetitionController competitionController) {
        this.currentUser = currentUser;
        this.handler = handler;
        this.competitionController = competitionController;
    }

    @Override
    protected void updateItem(Game item, boolean empty) {
        super.updateItem(item, empty);
        if(empty) setGraphic(null);
        else {
            Label gameLabel = new Label();
            Label gameInfoLabel = new Label();
            VBox vBox = new VBox(gameLabel,gameInfoLabel);
            gameLabel.setAlignment(Pos.CENTER);
            gameInfoLabel.setAlignment(Pos.CENTER);
            if (item.getChallenger().equals(currentUser)) {
                gameLabel.setText("uitnodiging naar " + item.getOpponent() + " in " + competitionController.getCompetition(item.getCompetitionId()).getName());
                gameInfoLabel.setText("[" + item.getLanguage().toString() + "] [" + item.getBoardType().toString() + "]");
            }
            else {
                gameLabel.setText("uitnodiging van " + item.getChallenger() + " uit " + competitionController.getCompetition(item.getCompetitionId()).getName());
                gameInfoLabel.setText("[" + item.getLanguage().toString() + "] [" + item.getBoardType().toString() + "]");
                Button accept = new Button("accepteer");
                Button reject = new Button("wijs af");
                accept.setOnAction(handler);
                reject.setOnAction(handler);
                accept.setUserData(item);
                reject.setUserData(item);
                HBox buttonBox = new HBox(10, accept, reject);
                buttonBox.setAlignment(Pos.CENTER);
                vBox.getChildren().add(buttonBox);
            }



            vBox.setAlignment(Pos.CENTER);
            setGraphic(vBox);
        }
    }
}
