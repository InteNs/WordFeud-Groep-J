package views;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import main.Main;
import models.Tile;
import resources.ResourceFactory;
import views.components.FieldTileNode;

public class SwapTileView extends View {
    ResourceFactory resourceFactory;
    FieldTileNode field;
    ObservableList<FieldTileNode> returnvalue;
    MainView mainView;
    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        // TODO Auto-generated method stub
       
    }
    
    public SwapTileView(MainView mainView, ResourceFactory resourceFactory){
        this.resourceFactory = resourceFactory;
        this.mainView = mainView;
       
    }
    
    public ObservableList<FieldTileNode> swapTiles(ObservableList<Tile> currentRack){
        Stage window = new Stage();
        window.setMinHeight(200);
        window.setMinWidth(640);
        window.initStyle(StageStyle.UNDECORATED);
        window.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) window.close();
        });
        
        TilePane tilePane = new TilePane();
        tilePane.setOnDragDetected(event -> event.consume());
        tilePane.setPadding(new Insets(20, 10, 10,10));
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(7);
        tilePane.setStyle("-fx-background-color: gray;");
        ObservableList<Tile> tiles = currentRack;
        ObservableList<FieldTileNode> selectedTiles = FXCollections.observableArrayList();
        for(Tile tile: currentRack){
            FieldTileNode field = new FieldTileNode(tile, this.resourceFactory);
            tilePane.getChildren().add(field);
            field.setOnMouseClicked(EventHandler -> {     
                if(selectedTiles.contains(field)){
                    selectedTiles.remove(field);
                    field.setRotate(0);
                }
                else{
                    selectedTiles.add(field);
                    field.setRotate(10);
                }
            });
        }
             
        Button swap  = new Button("Ruilen");
        swap.setStyle("-fx-background-color: white; -fx-font-size: 18px;");
        swap.setOnAction(event -> { 
            returnvalue = selectedTiles;
            window.close();
        });
        
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: gray;");
        hBox.getChildren().add(swap);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
       
        VBox vBox = new VBox();
        vBox.getChildren().addAll(tilePane, hBox);
        vBox.setStyle("-fx-background-color: gray; -fx-border-color: white; -fx-border-width: 4px;");
        vBox.setPadding(new Insets(0,0, 5, 0));
        
       Scene scene = new Scene(vBox);
       window.setScene(scene);
       window.setX(mainView.gameBoardView.getScene().getWindow().getX() + mainView.gameBoardView.getScene().widthProperty().intValue() /2 - window.getMinWidth()/2);
       window.setY(mainView.gameBoardView.getScene().getWindow().getY() + mainView.gameBoardView.getScene().heightProperty().intValue() /2 - window.getMinHeight()/2);
       window.showAndWait(); 
       
       if(returnvalue == null){
           return null;
       }
       return returnvalue;
    }
}

