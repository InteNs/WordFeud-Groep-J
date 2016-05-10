package views;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import models.Field;
import models.Game;
import models.Tile;

import java.awt.*;
import java.util.ArrayList;


public class GameBoardView extends View {

    @FXML private VBox root;
    @FXML private StackPane stackPane;
    @FXML private GridPane gameBoardGrid;
    @FXML private TilePane playerRackGrid;
    @FXML private ChoiceBox<String> playerActionChoiceBox;
    @FXML private Button playerConfirmActionButton;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;
    private Game selectedGame;

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
                    s = field.getTile().toString().toUpperCase();

                String myString ="resources/"+s+".png";
                ImageView imageField = new ImageView(
                        new Image(myString,40,40,true,true,true));
                GridPane.setConstraints(imageField,y,x);
                gameBoardGrid.getChildren().add(imageField);
                imageField.setOnDragOver(event -> {
                    event.consume();
                    if (event.getSource()!=imageField && event.getDragboard().hasImage())
                        event.acceptTransferModes(TransferMode.MOVE);

                });
                imageField.setOnDragDropped(event -> {
                    int xTarget;
                    int yTarget;
                    Dragboard db = event.getDragboard();
                    boolean succes = false;
                    if (db.hasImage()) {
                        imageField.setImage(db.getImage());
                        xTarget = GridPane.getColumnIndex(imageField);
                        yTarget = GridPane.getRowIndex(imageField);
                        gameBoard[xTarget][yTarget].setTile(gameController.getTile(selectedGame, db.getString()));
                        succes = true;
                    }
                    event.setDropCompleted(succes);
                });
            }
    }

    public void displayPlayerRack(ArrayList<Tile>playerRack){
//        playerRack.add(new Tile(1,'A'));
//        playerRack.add(new Tile(1,'A'));
//        playerRack.add(new Tile(1,'A'));
//        playerRack.add(new Tile(1,'A'));
//        playerRack.add(new Tile(1,'A'));
//        playerRack.add(new Tile(1,'A'));
//        playerRack.add(new Tile(1,'A'));


        playerRack.forEach( e -> {
            ImageView imageView;
            String s = e.toString();
            imageView = new ImageView((new Image("resources/" + s + ".png", 40, 40, true, true, true)));
            playerRackGrid.getChildren().add(imageView);

            imageView.setOnDragDetected(event -> {
                event.consume();
                ((Node)event.getSource()).setCursor(Cursor.HAND);
                Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(imageView.getImage());
                content.putString(String.valueOf(e.hashCode()));
                db.setContent(content);
            });

        });
    }

    @Override
    public void refresh() {

    }

    @Override
    public void constructor() {
        gameController.selectedGameProperty().addListener((observable, oldValue, newValue) -> {
            gameController.loadGame(newValue);
            selectedGame=newValue;
//            newValue.setBoardStateTo(newValue.getLastTurn());
            newValue.setBoardStateTo(newValue.getTurns().get(20));
            displayPlayerRack(newValue.getTurns().get(20).getRack());//PLACEHOLDER

            displayGameBoard(newValue.getGameBoard());
        });
//        final double SCALE_DELTA = 1.1;
//        final StackPane zoomPane = new StackPane();
//        stackPane.setOnScroll(event -> {
//            event.consume();
//            if (event.getDeltaY() == 0) {
//                return;
//            }
//            double scaleFactor =
//                    (event.getDeltaY() > 0)
//                            ? SCALE_DELTA
//                            : 1/SCALE_DELTA;
//
//            gameBoardGrid.setScaleX(gameBoardGrid.getScaleX() * scaleFactor);
//            gameBoardGrid.setScaleY(gameBoardGrid.getScaleY() * scaleFactor);
//        });
    }
}
