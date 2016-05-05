package views;

import controllers.GameController;
import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import models.Field;
import models.Game;
import models.User;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ben on 4-5-2016.
 */
public class GameBoardView extends View implements Initializable {

    @FXML private GridPane gameBoardGrid;
    @FXML private GridPane playerRackGrid;
    @FXML private ChoiceBox playerActionChoiceBox;
    @FXML private Button playerConfirmActionButton;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;

    private GameController gameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gameController = new GameController();
        playerActionChoiceBox.setItems(FXCollections.observableArrayList("Play Word",
                new Separator(),
                "Pass Beurt",
                new Separator(),
                "Shuffle",
                new Separator(),
                "Ruil stenen")); //TODO: Add Opgeven!

//        Game game = new Game(511,new User("jager684"),new User("marijntje42"), GameState.FINISHED, BoardType.STANDARD, Language.NL);
//        gameController.loadGame(game);
//        displayGameBoard(game.getGameBoard());
    }

    public void displayGameBoard(Field[][] gameBoard){
        Image image = null;
        ImageView imageViewer;
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard.length; x++) {
                if (gameBoard[y][x].getTile() == null) {
                    image = new Image("\\resources\\Blank-TilePNG.png");
                } else {
                    image = new Image("\\resources\\A-TilePNG.png");
                }

                imageViewer= new ImageView();
                imageViewer.setImage(image);
                imageViewer.setFitHeight(30);
                imageViewer.setFitWidth(40);
                imageViewer.setPreserveRatio(true);
                imageViewer.setSmooth(true);
                gameBoardGrid.add(imageViewer,x,y);
            }
        }
    }

    @Override
    public void refresh() {
        //Called when a new Turn has been passed or when a User want a certain turn to be displayed.
    }
}
