package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Field;


public class GameBoardView extends View {

    @FXML private VBox root;
    @FXML private StackPane stackPane;
    @FXML private GridPane gameBoardGrid;
    @FXML private GridPane playerRackGrid;
    @FXML private ChoiceBox<String> playerActionChoiceBox;
    @FXML private Button playerConfirmActionButton;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;

    public void displayGameBoard(Field[][] gameBoard){
        if(gameBoard == null) {
            gameBoardGrid.getChildren().removeAll();
            return;
        }
        for (int y = 0; y < gameBoard.length; y++)
            for (int x = 0; x < gameBoard.length; x++) {
                Field field = gameBoard[x][y];
                String s;
                if(field.getTile() == null)
                    s = field.getFieldType().toString();
                else
                    s = field.getTile().getCharacter().toString().toUpperCase();

                ImageView imageField = new ImageView(
                        new Image("resources/"+s+".png",40,40,true,true,false));
                GridPane.setConstraints(imageField,y,x);
                gameBoardGrid.getChildren().add(imageField);
            }
    }

    @Override
    public void refresh() {

    }

    @Override
    public void constructor() {
        gameController.selectedGameProperty().addListener((observable, oldValue, newValue) -> {
            newValue.setBoardStateTo(newValue.getLastTurn());
            displayGameBoard(newValue.getGameBoard());
        });
    }
}
