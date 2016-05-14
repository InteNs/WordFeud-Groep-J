package views;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Field;
import models.Game;
import models.Tile;
import views.components.DraggableNode;

import java.util.ArrayList;


public class GameBoardView extends View {

    @FXML
    private VBox root;
    @FXML
    private StackPane stackPane;
    @FXML
    private GridPane gameBoardGrid;
    @FXML
    private HBox playerRackGrid;
    @FXML
    private ChoiceBox<String> playerActionChoiceBox;
    @FXML
    private Button playerConfirmActionButton;
    @FXML
    private Label player1ScoreLabel;
    @FXML
    private Label player2ScoreLabel;
    private Game selectedGame;
    private DraggableNode tileBeingDragged;

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
                    if(draggableNode.getField().getTile() == null)
                        event.acceptTransferModes(TransferMode.MOVE);
                    else
                        event.acceptTransferModes(TransferMode.NONE);
                });
                draggableNode.setOnDragDropped(event -> {
                    event.consume();
                    draggableNode.getField().setTile(tileBeingDragged.getTile());
                    draggableNode.redrawImage();
                    event.setDropCompleted(true);
                });
            }
    }

    public void displayPlayerRack(ArrayList<Tile> playerRack) {
        playerRackGrid.getChildren().clear();
        playerRack.forEach(tile -> {
            DraggableNode draggableNode = new DraggableNode(tile);
            playerRackGrid.getChildren().add(draggableNode);

            draggableNode.setOnDragDetected(event -> {
                event.consume();
                ((Node) event.getSource()).setCursor(Cursor.HAND);
                Dragboard db = draggableNode.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(draggableNode.getImage());
                db.setContent(content);
                tileBeingDragged = draggableNode;
                playerRackGrid.getChildren().remove(draggableNode);
            });
            draggableNode.setOnDragDone(event -> {
                event.consume();
                if(event.getTransferMode() != TransferMode.MOVE)
                    playerRackGrid.getChildren().add(tileBeingDragged);
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
