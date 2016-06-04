package views.components;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import models.Game;
import models.User;

public class GameCell extends ListCell<Game> {

    private final User currentUser;

    public GameCell(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    protected void updateItem(Game game, boolean empty) {
        super.updateItem(game, empty);
        if (empty) setGraphic(null);
        else {
            Label infoLabel = new Label();
            Label lastTurnLabel = new Label();

            if (game.getChallenger().equals(currentUser))
                infoLabel.setText("[" + game.getId() + "]\tSpel met " + game.getOpponent());
            else if (game.getOpponent().equals(currentUser))
                infoLabel.setText("[" + game.getId() + "]\tSpel met " + game.getChallenger());
            else
                infoLabel.setText("[" + game.getId() + "]\tSpel tussen " + game.getChallenger() + " en " + game.getOpponent());

            if (game.getNextUser().equals("ended"))
                lastTurnLabel.setText("[" + game.getLanguage() + "]\t\t Game is afgelopen");
            else if (game.getNextUser().equals("currentUser"))
                lastTurnLabel.setText("[" + game.getLanguage() + "]\t\tJij bent aan de beurt!");
            else
                lastTurnLabel.setText("[" + game.getLanguage() + "]\t\t" + game.getOpponent() + " is aan de beurt!");

            VBox vBox = new VBox(infoLabel, lastTurnLabel);
            setGraphic(vBox);
        }

    }
}
