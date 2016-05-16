package views;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.effect.SepiaTone;
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
import models.Turn;
import resources.Resource;
import views.components.FieldTileNode;

import java.util.ArrayList;
import java.util.stream.Collectors;


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
    private Turn selectedTurn;
    private Tile tileBeingDragged;
    private Resource resource;

    private void displayGameBoard(Field[][] gameBoard) {
        if (gameBoard == null) {
            gameBoardGrid.getChildren().removeAll();
            return;
        }
        for (int y = 0; y < gameBoard.length; y++)
            for (int x = 0; x < gameBoard.length; x++) {
                FieldTileNode fieldNode = new FieldTileNode(gameBoard[x][y], resource);
                GridPane.setConstraints(fieldNode, y, x);
                gameBoardGrid.getChildren().add(fieldNode);


                if (selectedTurn.getPlacedTiles().contains(fieldNode.getField().getTile())) {
                    fieldNode.setEffect(new SepiaTone(1));
                }

                if (selectedGame.isLastTurn(selectedTurn)) {
                    fieldNode.setOnDragOver(event -> {
                        if (fieldNode.getField().getTile() == null)
                            event.acceptTransferModes(TransferMode.MOVE);
                        else
                            event.acceptTransferModes(TransferMode.NONE);
                        event.consume();
                    });

                    fieldNode.setOnDragDropped(event -> {
                        fieldNode.setCursor(Cursor.OPEN_HAND);
                        selectedGame.addPlacedTile(fieldNode.getField(), tileBeingDragged);
                        fieldNode.redrawImage();
                        event.setDropCompleted(true);
                        event.consume();
                    });

                    fieldNode.setOnDragDetected(event -> {
                        if (!selectedGame.getFieldsChangedThisTurn().contains(fieldNode.getField()))
                            return;
                        prepareDrag(fieldNode);
                        tileBeingDragged = fieldNode.getField().getTile();
                        selectedGame.removePlacedTile(fieldNode.getField());
                        fieldNode.redrawImage();
                        event.consume();
                    });

                    fieldNode.setOnDragDone(event -> {
                        if (event.getTransferMode() != TransferMode.MOVE) {
                            selectedGame.addPlacedTile(fieldNode.getField(), tileBeingDragged);
                            fieldNode.redrawImage();
                            fieldNode.setCursor(Cursor.DEFAULT);
                        }
                        event.consume();
                    });
                }


            }
    }

    private void displayPlayerRack(ObservableList<Tile> playerRack) {
        ObservableList<FieldTileNode> nodes = FXCollections.observableArrayList();
        playerRackGrid.getChildren().setAll(nodes);

        nodes.addListener((ListChangeListener<? super FieldTileNode>) observable -> playerRackGrid.getChildren().setAll(nodes));

        playerRack.forEach(tile -> {
            FieldTileNode tileNode = new FieldTileNode(tile, resource);
            nodes.add(tileNode);
            tileNode.setCursor(Cursor.OPEN_HAND);

            if (selectedGame.isLastTurn(selectedTurn)) {
                tileNode.setOnDragDetected(event -> {
                    if (tileNode.isPlaceHolder())
                        return;
                    prepareDrag(tileNode);
                    tileBeingDragged = tileNode.getTile();
                    playerRack.remove(tileBeingDragged);
                    tileNode.setTile(null);
                    event.consume();
                });

                tileNode.setOnDragDone(event -> {
                    if (event.getTransferMode() != TransferMode.MOVE) {
                        playerRack.add(tileBeingDragged);
                        tileNode.setTile(tileBeingDragged);
                    }
                    event.consume();
                });

                tileNode.setOnDragDropped(event -> {
                    if (tileNode.isPlaceHolder()) {
                        tileNode.setTile(tileBeingDragged);
                    }

                    else {
                        FilteredList<FieldTileNode> emptys = nodes.filtered(FieldTileNode::isPlaceHolder);
                        ArrayList<FieldTileNode> sorted = emptys.stream().sorted((o1, o2) -> {
                            int diff1 = Math.abs(nodes.indexOf(o1) - nodes.indexOf(tileNode));
                            int diff2 = Math.abs(nodes.indexOf(o2) - nodes.indexOf(tileNode));
                            if(diff1 > diff2) return 1;
                            else return -1;
                        }).collect(Collectors.toCollection(ArrayList<FieldTileNode>::new));

                        FieldTileNode place = sorted.get(0);
                        if(nodes.indexOf(place) > nodes.indexOf(tileNode)) {
                            nodes.remove(place);
                            nodes.add(nodes.indexOf(tileNode), place);
                            place.setTile(tileBeingDragged);

                        } else {
                            nodes.remove(place);
                            nodes.add(nodes.indexOf(tileNode)+1, place);
                            place.setTile(tileBeingDragged);
                        }
                    }

                    playerRack.add(tileBeingDragged);
                    event.setDropCompleted(true);
                    event.consume();
                });

                tileNode.setOnDragOver(event -> {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                });
            }

        });

    }

    private void prepareDrag(FieldTileNode fieldTileNode) {
        Dragboard db = fieldTileNode.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(fieldTileNode.getImage());
        db.setContent(content);
    }

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
        resource = new Resource();
        gameController.selectedGameProperty().addListener((observable, oldValue, newValue) -> {

            selectedGame = newValue;
            gameController.loadGame(selectedGame);
            selectedTurn = selectedGame.getLastTurn();
            selectedGame.setBoardStateTo(selectedTurn);
            displayGameBoard(selectedGame.getGameBoard());
            displayPlayerRack(selectedGame.getCurrentRack());
        });

        gameController.selectedTurnProperty().addListener((observable, oldValue, newValue) -> {
            selectedTurn = newValue;
            selectedGame.setBoardStateTo(selectedTurn);
            displayGameBoard(selectedGame.getGameBoard());
            displayPlayerRack(selectedGame.getCurrentRack());
        });
    }
}
