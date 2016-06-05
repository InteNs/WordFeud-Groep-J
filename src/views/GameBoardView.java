package views;

import enumerations.Role;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import models.Field;
import models.Game;
import models.Tile;
import models.Turn;
import views.components.FieldTileNode;
import views.components.ScoreOverlay;
import views.subviews.JokerView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameBoardView extends View {

    ObservableList<FieldTileNode> nodes;
    private ScoreOverlay scoreOverlay;
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

    public void displayGameBoard(Game selectedGame, Turn selectedTurn) {
        Field[][] gameBoard = selectedGame.getTurnBuilder().getGameBoard();
        gameBoardGrid.getChildren().clear();
        for (int y = 0; y < gameBoard.length; y++)
            for (int x = 0; x < gameBoard.length; x++) {
                FieldTileNode fieldNode = new FieldTileNode(gameBoard[x][y], resourceFactory);
                GridPane.setConstraints(fieldNode, y, x);
                gameBoardGrid.getChildren().add(fieldNode);

                if (selectedTurn.getPlacedTiles().contains(fieldNode.getField().getTile())) {
                    fieldNode.setEffect(new SepiaTone(1));
                }

                if (selectedGame.isLastTurn(selectedTurn) && selectedGame.getGameMode() == Role.PLAYER) {
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
                        event.setDropCompleted(true);
                        if (gameController.isJokerTile(tileBeingDragged)) {
                            JokerView jokerView = new JokerView(resourceFactory, parent);
                            char choice = jokerView.jokerChoice();
                            tileBeingDragged.replaceJoker(choice);
                            ((FieldTileNode) event.getTarget()).redrawImage();
                        }
                        event.consume();
                        fieldNode.redrawImage();
                    });

                    fieldNode.setOnDragDetected(event -> {
                        if (!selectedGame.getTurnBuilder().getFieldsChanged().contains(fieldNode.getField()))
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

    public void displayPlayerRack(Game selectedGame, Turn selectedTurn) {
        nodes = FXCollections.observableArrayList();
        playerRackGrid.getChildren().setAll(nodes);

        nodes.addListener((ListChangeListener<? super FieldTileNode>) observable ->
                playerRackGrid.getChildren().setAll(nodes));

        selectedGame.getTurnBuilder().getCurrentRack().forEach(tile -> {
            FieldTileNode tileNode = new FieldTileNode(tile, resourceFactory);
            nodes.add(tileNode);
            tileNode.setCursor(Cursor.OPEN_HAND);

            if (selectedGame.isLastTurn(selectedTurn) && selectedGame.getGameMode() == Role.PLAYER) {
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
                        if (tileNode.getTile().getCharacter().equals('?')) {
                            tileBeingDragged.replaceJoker(null);
                            tileNode.redrawImage();
                        }
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

    public void clearBoard() {
        //get changed fields
        ObservableList<Field> fields = gameController.getFieldsChanged(gameController.getSelectedGame());
        //for every blank space in rack
        nodes.stream()
                .filter(FieldTileNode::isPlaceHolder)
                .forEach(fieldTileNode -> {
                    if (fields.isEmpty()) return;
                    Field field = fields.get(0);
                    fieldTileNode.setTile(field.getTile());
                    gameController.removeTile(gameController.getSelectedGame(), field);
                    findNodeInGrid(field.getX(), field.getY()).redrawImage();
                });
        setCurrentRack(gameController.getSelectedGame(), nodes);
    }

    private FieldTileNode findNodeInGrid(int col, int row) {
        for (Node node : gameBoardGrid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (FieldTileNode) node;
            }
        }
        return (FieldTileNode) gameBoardGrid.getChildren().get(0);
    }

    public void shuffleRack() {
        FXCollections.shuffle(nodes);
        setCurrentRack(gameController.getSelectedGame(), nodes);
    }

    private void setCurrentRack(Game game, ObservableList<FieldTileNode> tileNodes) {
        gameController.setPlayerRack(game, tileNodes.stream()
                .filter(fieldTileNode -> !fieldTileNode.isPlaceHolder())
                .map(FieldTileNode::getTile)
                .collect(Collectors.toList())
        );
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
    public void clear() {

    }

    @Override
    public void constructor() {

        gameController.selectedTurnProperty().addListener((observable, oldValue, selectedTurn) -> {
            if (selectedTurn != null) {
                showTurn(gameController.getSelectedGame(), selectedTurn);
            }
        });
        gameController.currentRoleProperty().addListener((observable, oldValue, newValue) -> {
            if (gameController.getSelectedGame() != null && gameController.getSelectedTurn() != null) {
                showTurn(gameController.getSelectedGame(), gameController.getSelectedTurn());
            }
        });
        gameController.selectedGameProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue != null){
               newValue.getTurnBuilder().getFieldsChanged().addListener((ListChangeListener<? super Field>) observable1 -> {
                   if (newValue.getTurnBuilder().getFieldsChanged().isEmpty() || newValue.getTurnBuilder().getScore() == 0){
                       stackPane.getChildren().remove(scoreOverlay);
                   } else {
                       stackPane.getChildren().add(new ScoreOverlay(newValue.getTurnBuilder().getScore()));
                   }
               });
           }
        });
        stackPane.widthProperty().addListener(e -> sizeBoard());
        stackPane.heightProperty().addListener(e -> sizeBoard());

    }

    private void showTurn(Game game, Turn turn) {
        gameController.setBoardState(game, turn);
        displayGameBoard(game, turn);
        displayPlayerRack(game, turn);
    }

    private void sizeBoard() {
        double base = 1.0;
        double norm = 600;
        double min = Math.min(stackPane.getWidth(), stackPane.getHeight());
        gameBoardGrid.setScaleX(base * min/norm);
        gameBoardGrid.setScaleY(base * min/norm);
    }

    public void showJokers() {
        for (Node field : gameBoardGrid.getChildren()) {
            FieldTileNode fieldnode = (FieldTileNode) field;
            if ((fieldnode.getField().getTile() != null) && gameController.isJokerTile(fieldnode.getField().getTile()))
                fieldnode.highLight();
        }
    }
}
