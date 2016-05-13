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
import views.components.DraggableNode;

import java.awt.*;
import java.util.ArrayList;


public class GameBoardView extends View {

    @FXML
    private VBox root;
    @FXML
    private StackPane stackPane;
    @FXML
    private GridPane gameBoardGrid;
    @FXML
    private TilePane playerRackGrid;
    @FXML
    private ChoiceBox<String> playerActionChoiceBox;
    @FXML
    private Button playerConfirmActionButton;
    @FXML
    private Label player1ScoreLabel;
    @FXML
    private Label player2ScoreLabel;
    private Game selectedGame;
    private Tile tileBeingDragged;

    public void displayGameBoard(Field[][] gameBoard) {
        if (gameBoard == null) {
            gameBoardGrid.getChildren().removeAll();
            return;
        }
        for (int y = 0; y < gameBoard.length; y++)
            for (int x = 0; x < gameBoard.length; x++) {
                Field field = gameBoard[x][y];
                DraggableNode draggableNode = new DraggableNode(field);

                GridPane.setConstraints(draggableNode, y, x);
                gameBoardGrid.getChildren().add(draggableNode);
                draggableNode.setOnDragOver(event -> {
                    event.consume();
                        event.acceptTransferModes(TransferMode.MOVE);
                });
                draggableNode.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean succes = false;
                    // if (db.hasImage()) {
                    draggableNode.setImage(db.getDragView());
                    draggableNode.getField().setTile(tileBeingDragged);
                        System.out.println(draggableNode.getField().getTile());
                        succes = true;
                    //  }
                    //TODO: Check this value with Source setOnDropDone method
                    event.setDropCompleted(succes);
                });
            }
    }

    public void displayPlayerRack(ArrayList<Tile> playerRack) {
        playerRack.forEach(e -> {
            String s = e.toString();
            DraggableNode draggableNode = new DraggableNode(e);
            playerRackGrid.getChildren().add(draggableNode);

            draggableNode.setOnDragDetected(event -> {
                event.consume();
                ((Node) event.getSource()).setCursor(Cursor.HAND);
                Dragboard db = draggableNode.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                //content.putImage(draggableNode.getImage());
                //TODO: setDragView not working as intended
                db.setDragView(draggableNode.getImage());
                content.putString(draggableNode.getId());
                tileBeingDragged = draggableNode.getTile();
                playerRackGrid.getChildren().remove(draggableNode);
                db.setContent(content);
            });

            draggableNode.setOnDragDone(event -> {
                if (!event.isDropCompleted()) {
                    playerRackGrid.getChildren().add(draggableNode);
                }
                tileBeingDragged = null;
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
            selectedGame = newValue;
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
