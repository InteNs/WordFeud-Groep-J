package views;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
import views.components.FieldTileNode;


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
                FieldTileNode fieldNode = new FieldTileNode(field);

                GridPane.setConstraints(fieldNode, y, x);
                gameBoardGrid.getChildren().add(fieldNode);
                fieldNode.setOnDragOver(event -> {
                    event.consume();
                    if (fieldNode.getField().getTile() == null)
                        event.acceptTransferModes(TransferMode.MOVE);
                    else
                        event.acceptTransferModes(TransferMode.NONE);
                });
                fieldNode.setOnDragDropped(event -> {
                    event.consume();
                    selectedGame.addPlacedTile(fieldNode.getField(), tileBeingDragged);
                    fieldNode.redrawImage();
                    event.setDropCompleted(true);
                });
                fieldNode.setOnDragDetected(event -> {
                    event.consume();
                    if (!selectedGame.getFieldsChangedThisTurn().contains(fieldNode.getField()))
                        return;
                    ((Node) event.getSource()).setCursor(Cursor.HAND);
                    Dragboard db = fieldNode.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(fieldNode.getImage());
                    db.setContent(content);
                    tileBeingDragged = fieldNode.getField().getTile();
                    selectedGame.removePlacedTile(fieldNode.getField());
                    fieldNode.redrawImage();
                });
                fieldNode.setOnDragDone(event -> {
                    event.consume();
                    if (event.getTransferMode() != TransferMode.MOVE) {
                        selectedGame.addPlacedTile(fieldNode.getField(), tileBeingDragged);
                        fieldNode.redrawImage();
                    }
                    tileBeingDragged = null;
                });
            }
    }

    public void displayPlayerRack(ObservableList<Tile> playerRack) {
        playerRackGrid.setOnDragOver(event -> {
            event.consume();
            event.acceptTransferModes(TransferMode.MOVE);
        });
        playerRackGrid.setOnDragDropped(event -> {
            playerRack.add(tileBeingDragged);
            tileBeingDragged = null;
            event.setDropCompleted(true);
        });
        buildNodeRack(playerRack);
        playerRack.addListener((ListChangeListener<? super Tile>) observable -> buildNodeRack(playerRack));
    }

    private void buildNodeRack(ObservableList<Tile> playerRack) {
        playerRackGrid.getChildren().clear();
        playerRack.forEach(tile -> {
            FieldTileNode tileNode = new FieldTileNode(tile);
            playerRackGrid.getChildren().add(tileNode);

            tileNode.setOnDragDetected(event -> {
                event.consume();
                ((Node) event.getSource()).setCursor(Cursor.HAND);
                Dragboard db = tileNode.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(tileNode.getImage());
                db.setContent(content);
                tileBeingDragged = tileNode.getTile();
                playerRack.remove(tileBeingDragged);
            });
            tileNode.setOnDragDone(event -> {
                event.consume();
                if (event.getTransferMode() != TransferMode.MOVE)
                    playerRack.add(tileBeingDragged);
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
            newValue.setBoardStateTo(newValue.getTurns().get(20));
            displayGameBoard(newValue.getGameBoard());
            displayPlayerRack(newValue.getCurrentRack());
        });
    }
}
