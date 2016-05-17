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
    private Tile tileBeingDragged;

    private void displayGameBoard(Game selectedGame, Turn selectedTurn) {
        Field[][] gameBoard = selectedGame.getGameBoard();
        gameBoardGrid.getChildren().removeAll();
        for (int y = 0; y < gameBoard.length; y++)
            for (int x = 0; x < gameBoard.length; x++) {
                FieldTileNode fieldNode = new FieldTileNode(gameBoard[x][y], resourceFactory);
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
                        gameController.placeTile(selectedGame, fieldNode.getField(), tileBeingDragged);
                        fieldNode.redrawImage();
                        event.setDropCompleted(true);
                        event.consume();
                    });

                    fieldNode.setOnDragDetected(event -> {
                        if (!selectedGame.getFieldsChangedThisTurn().contains(fieldNode.getField()))
                            return;
                        prepareDrag(fieldNode);
                        tileBeingDragged = fieldNode.getField().getTile();
                        gameController.removeTile(selectedGame, fieldNode.getField());
                        fieldNode.redrawImage();
                        event.consume();
                    });

                    fieldNode.setOnDragDone(event -> {
                        if (event.getTransferMode() != TransferMode.MOVE) {
                            gameController.placeTile(selectedGame, fieldNode.getField(), tileBeingDragged);
                            fieldNode.redrawImage();
                            fieldNode.setCursor(Cursor.DEFAULT);
                        }
                        event.consume();
                    });
                }
            }
    }

    private void displayPlayerRack(Game selectedGame, Turn selectedTurn) {
        ObservableList<FieldTileNode> nodes = FXCollections.observableArrayList();
        playerRackGrid.getChildren().setAll(nodes);

        nodes.addListener((ListChangeListener<? super FieldTileNode>) observable -> playerRackGrid.getChildren().setAll(nodes));

        selectedGame.getCurrentRack().forEach(tile -> {
            FieldTileNode tileNode = new FieldTileNode(tile, resourceFactory);
            nodes.add(tileNode);
            tileNode.setCursor(Cursor.OPEN_HAND);

            if (selectedGame.isLastTurn(selectedTurn)) {
                tileNode.setOnDragDetected(event -> {
                    if (tileNode.isPlaceHolder())
                        return;
                    prepareDrag(tileNode);
                    tileBeingDragged = tileNode.getTile();
                    tileNode.setTile(null);
                    event.consume();
                });

                tileNode.setOnDragDone(event -> {
                    if (event.getTransferMode() != TransferMode.MOVE) {
                        tileNode.setTile(tileBeingDragged);
                    }
                    setCurrentRack(selectedGame, nodes);
                    event.consume();
                });

                tileNode.setOnDragDropped(event -> {
                    if (tileNode.isPlaceHolder()) {
                        tileNode.setTile(tileBeingDragged);
                    } else {
                        FilteredList<FieldTileNode> emptys = nodes.filtered(FieldTileNode::isPlaceHolder);
                        ArrayList<FieldTileNode> sorted = emptys.stream().sorted((o1, o2) -> {
                            int diff1 = Math.abs(nodes.indexOf(o1) - nodes.indexOf(tileNode));
                            int diff2 = Math.abs(nodes.indexOf(o2) - nodes.indexOf(tileNode));
                            if (diff1 > diff2) return 1;
                            else return -1;
                        }).collect(Collectors.toCollection(ArrayList<FieldTileNode>::new));

                        FieldTileNode place = sorted.get(0);
                        if (nodes.indexOf(place) > nodes.indexOf(tileNode)) {
                            nodes.remove(place);
                            nodes.add(nodes.indexOf(tileNode), place);
                            place.setTile(tileBeingDragged);

                        } else {
                            nodes.remove(place);
                            nodes.add(nodes.indexOf(tileNode) + 1, place);
                            place.setTile(tileBeingDragged);
                        }
                    }

                    setCurrentRack(selectedGame, nodes);
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

    private void setCurrentRack(Game game, ObservableList<FieldTileNode> tileNodes) {
        gameController.setPlayerRack(game, tileNodes.stream()
                .filter(fieldTileNode -> !fieldTileNode.isPlaceHolder())
                .map(FieldTileNode::getTile).collect(Collectors.toList()));
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
        gameController.selectedGameProperty().addListener((observable, oldValue, selectedGame) -> {
            gameController.loadGame(selectedGame);
            gameController.setSelectedTurn(selectedGame.getLastTurn());
        });

        gameController.selectedTurnProperty().addListener((observable, oldValue, selectedTurn) -> {
            gameController.setBoardState(gameController.getSelectedGame(), selectedTurn);
            displayGameBoard(gameController.getSelectedGame(), selectedTurn);
            displayPlayerRack(gameController.getSelectedGame(), selectedTurn);
        });
    }
}
