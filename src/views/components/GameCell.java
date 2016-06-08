package views.components;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
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
            Label gameIdLabel = new Label("[" + game.getId() + "]");
            Label langLabel = new Label("[" + game.getLanguage() + "]");

            gameIdLabel.setPrefWidth(40);
            langLabel.setPrefWidth(40);

            if (game.getChallenger().equals(currentUser))
                infoLabel.setText("Spel met " + game.getOpponent() + " [" + game.getChallengerScore() + " vs " + game.getOpponentScore() + "]");
            else if (game.getOpponent().equals(currentUser))
                infoLabel.setText("Spel met " + game.getChallenger() + " [" + game.getOpponentScore() + " vs " + game.getChallengerScore() + "]");
            else
                infoLabel.setText("Spel tussen " + game.getChallenger() + " en " + game.getOpponent() + " [" + game.getChallengerScore() + " vs " + game.getOpponentScore() + "]");

            switch (game.getGameState()) {
                case FINISHED:
                    lastTurnLabel.setText(game.getWinner() + " heeft gewonnen met " + game.getWinnerScore() + " punten!");
                    break;
                case RESIGNED:
                    if (game.getOpponentScore() == 0)
                        lastTurnLabel.setText(game.getOpponent() + " heeft opgegeven!");
                    else if (game.getChallengerScore() == 0)
                        lastTurnLabel.setText(game.getChallenger() + " heeft opgegeven!");
                    else
                        lastTurnLabel.setText("Iemand heeft opgegeven!");
                    break;
                case PLAYING:
                    if (game.getNextUser().equals(currentUser))
                        lastTurnLabel.setText("Jij bent aan de beurt!");
                    else
                        lastTurnLabel.setText(game.getNextUser() + " is aan de beurt!");
            }
            HBox hBox1 = new HBox(gameIdLabel, infoLabel);
            HBox hBox2 = new HBox(langLabel, lastTurnLabel);
            VBox vBox = new VBox(hBox1, hBox2);
            setGraphic(vBox);
        }
    }
}
