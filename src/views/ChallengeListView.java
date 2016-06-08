package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Game;
import views.components.ChallengeCell;

public class ChallengeListView extends View implements EventHandler<ActionEvent> {

    @FXML private TextField filterField;
    @FXML private SplitPane lists;
    @FXML private TitledPane incomingChallengePane;
    @FXML private TitledPane outgoingChallengePane;
    @FXML private ListView<Game> incomingChallengeList;
    @FXML private ListView<Game> outgoingChallengeList;

    @Override
    public void refresh() {
        incomingChallengeList.setCellFactory(param ->
                new ChallengeCell(session.getCurrentUser(), this, session.getCompetitionController())
        );
        incomingChallengeList.setItems(
                gameController.getIncomingChallenges(session.getCurrentUser())
        );
        outgoingChallengeList.setCellFactory(param ->
                new ChallengeCell(session.getCurrentUser(), this , session.getCompetitionController())
        );
        outgoingChallengeList.setItems(
                gameController.getOutgoingChallenges(session.getCurrentUser())
        );
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        incomingChallengeList.setCellFactory(param ->
                new ChallengeCell(session.getCurrentUser(), this, session.getCompetitionController())
        );
        incomingChallengeList.setItems(
                gameController.getIncomingChallenges(session.getCurrentUser())
        );
        outgoingChallengeList.setCellFactory(param ->
                new ChallengeCell(session.getCurrentUser(), this, session.getCompetitionController())
        );
        outgoingChallengeList.setItems(
                gameController.getOutgoingChallenges(session.getCurrentUser())
        );
    }

    @Override
    public void handle(ActionEvent event) {
        Button source = (Button) event.getSource();
        if (source.getText().equals("accepteer")) {
            gameController.acceptInvite((Game)source.getUserData());

        } else if (source.getText().equals("wijs af")) {
            gameController.rejectInvite((Game)source.getUserData());
        }
        parent.reload();
    }
}
