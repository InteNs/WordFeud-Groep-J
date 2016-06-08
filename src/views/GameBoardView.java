package views;

import enumerations.Role;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.util.Comparator;
import java.util.stream.Collectors;

public class GameBoardView extends View {

    private ObservableList<FieldTileNode> nodes;
    private ScoreOverlay scoreOverlay;
    @FXML private VBox root;
    @FXML private StackPane stackPane;
    @FXML private GridPane gameBoardGrid;
    @FXML private HBox playerRackGrid;
    @FXML private Tile tileBeingDragged;
    @FXML private Pane bubblePane;

    private void displayGameBoard(Game selectedGame, Turn selectedTurn) {
        Field[][] gameBoard = selectedGame.getTurnBuilder().getGameBoard();
        gameBoardGrid.getChildren().clear();
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard.length; x++) {
                FieldTileNode fieldNode = new FieldTileNode(gameBoard[x][y], resourceFactory);
                GridPane.setConstraints(fieldNode, y, x);
                gameBoardGrid.getChildren().add(fieldNode);

                if (selectedTurn.getPlacedTiles().contains(fieldNode.getField().getTile())) {
                    fieldNode.setEffect(new SepiaTone(1));
                }

                if (selectedGame.isLastTurn(selectedTurn) && gameController.getCurrentRole() == Role.PLAYER) {
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
                        // Checks if the tile placed is a joker, gets selected tile from jokerview and replaces it
                        if (tileBeingDragged.isJokerTile()) {
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
    }

    private void displayPlayerRack(Game selectedGame, Turn selectedTurn) {
        nodes = FXCollections.observableArrayList();
        playerRackGrid.getChildren().setAll(nodes);

        nodes.addListener((ListChangeListener<? super FieldTileNode>) observable ->
                playerRackGrid.getChildren().setAll(nodes));

        selectedGame.getTurnBuilder().getCurrentRack().forEach(tile -> {
            FieldTileNode tileNode = new FieldTileNode(tile, resourceFactory);
            nodes.add(tileNode);
            tileNode.setCursor(Cursor.OPEN_HAND);

            if (selectedGame.isLastTurn(selectedTurn) && gameController.getCurrentRole() == Role.PLAYER) {
                tileNode.setOnDragDetected(event -> {
                    if (tileNode.isEmptyRackNode())
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
                    if (tileNode.isEmptyRackNode()) {
                        //place the tile in the empty node
                        tileNode.setTile(tileBeingDragged);
                        if (tileNode.getTile().getCharacter().equals('?')) {
                            tileBeingDragged.replaceJoker(null);
                            tileNode.redrawImage();
                        }
                    } else {
                        //find closest empty rack node
                        FieldTileNode emptyRackNode = nodes.stream()
                                .filter(FieldTileNode::isEmptyRackNode)
                                .sorted(distanceToNode(tileNode))
                                .collect(Collectors.toCollection(ArrayList<FieldTileNode>::new))
                                .get(0);

                        //remove empty rack node and add at the place the user dropped the tile
                        //the remove will shift the tiles to that direction
                        if (nodes.indexOf(emptyRackNode) > nodes.indexOf(tileNode)) {
                            nodes.remove(emptyRackNode);
                            nodes.add(nodes.indexOf(tileNode), emptyRackNode);
                            emptyRackNode.setTile(tileBeingDragged);

                        } else {
                            nodes.remove(emptyRackNode);
                            nodes.add(nodes.indexOf(tileNode) + 1, emptyRackNode);
                            emptyRackNode.setTile(tileBeingDragged);
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

    /**
     * returns a comparator comparing the distance to the given node in the (rack)
     * nodes arrayList
     * @param tileNode the target
     * @return
     */
    private Comparator<FieldTileNode> distanceToNode(FieldTileNode tileNode) {
        return (o1, o2) -> {
            int diff1 = Math.abs(nodes.indexOf(o1) - nodes.indexOf(tileNode));
            int diff2 = Math.abs(nodes.indexOf(o2) - nodes.indexOf(tileNode));
            if (diff1 > diff2) return 1;
            else return -1;
        };
    }

    public void clearBoard() {
        //get changed fields
        ObservableList<Field> fields = gameController.getFieldsChanged(gameController.getSelectedGame());
        //for every blank space in rack
        nodes.stream().filter(FieldTileNode::isEmptyRackNode)
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
                .filter(fieldTileNode -> !fieldTileNode.isEmptyRackNode())
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
        root.setOnMousePressed(event -> parent.setTab(parent.gameControlView));

        gameController.selectedTurnProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showTurn(gameController.getSelectedGame(), newValue);
            }
        });

        gameController.currentRoleProperty().addListener((observable, oldValue, newValue) -> {
            if (gameController.getSelectedGame() != null && gameController.getSelectedTurn() != null) {
                showTurn(gameController.getSelectedGame(), gameController.getSelectedTurn());
            }
        });
        scoreOverlay = new ScoreOverlay();
    }



    private void showTurn(Game game, Turn turn) {
        bubblePane.getChildren().clear();
        gameController.setBoardState(game, turn);
        displayGameBoard(game, turn);
        displayPlayerRack(game, turn);
        game.getTurnBuilder().getFieldsChanged().addListener((ListChangeListener<? super Field>) observable -> {
            if (observable.getList().isEmpty() || !gameController.getSelectedGame().getTurnBuilder().isValidAction()) {
                scoreOverlay.setVisible(false);
            } else {
               if (!bubblePane.getChildren().isEmpty()) {
                   bubblePaneInfo();
                   scoreOverlay.setVisible(true);
               } else {
                   bubblePaneInfo();
                   bubblePane.getChildren().add(scoreOverlay);
               }
            }
        });
    }

    private void bubblePaneInfo(){
        int layoutX = 0;
        int layoutY = 0;
        int scoreToShow = gameController.getSelectedGame().getTurnBuilder().getScore();
        if (scoreToShow > 0 && gameController.getSelectedGame().getTurnBuilder().isValidAction()){
            int xPosInGameBoard = gameController.getSelectedGame().getTurnBuilder().getBubbleField().get(gameController.getSelectedGame().getTurnBuilder().getBubbleField().size()-1).getX();
            int yPosInGameBoard = gameController.getSelectedGame().getTurnBuilder().getBubbleField().get(gameController.getSelectedGame().getTurnBuilder().getBubbleField().size()-1).getY();

            if (xPosInGameBoard == 14){
                xPosInGameBoard = gameController.getSelectedGame().getTurnBuilder().getBubbleField().get(0).getX();
                yPosInGameBoard = gameController.getSelectedGame().getTurnBuilder().getBubbleField().get(0).getY();
                layoutX = (int)findNodeInGrid(xPosInGameBoard,yPosInGameBoard).getLayoutX() - 40;
            } else {
                layoutX = (int)findNodeInGrid(xPosInGameBoard,yPosInGameBoard).getLayoutX();
            }

            if (yPosInGameBoard == 14) {
                xPosInGameBoard = gameController.getSelectedGame().getTurnBuilder().getBubbleField().get(0).getX();
                yPosInGameBoard = gameController.getSelectedGame().getTurnBuilder().getBubbleField().get(0).getY();
                layoutY = (int) findNodeInGrid(xPosInGameBoard, yPosInGameBoard).getLayoutY() - 40;
            } else {
                layoutY = (int) findNodeInGrid(xPosInGameBoard, yPosInGameBoard).getLayoutY();
            }

            scoreOverlay.setCircleInformation(layoutX, layoutY, scoreToShow);
        }
    }



    // Checks the gameboard for jokers, the highlights them
    public void showJokers() {
        gameBoardGrid.getChildren().stream()
                .map(node -> ((FieldTileNode) node))
                .filter(node -> node.getField().containsJoker())
                .forEach(FieldTileNode::highLight);
    }
}
