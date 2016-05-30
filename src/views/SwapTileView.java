package views;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Tile;
import resources.ResourceFactory;
import views.components.FieldTileNode;

public class SwapTileView extends View {
    ResourceFactory resourceFactory;
    FieldTileNode field;
    ObservableList<FieldTileNode> returnvalue;
    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void constructor() {
        // TODO Auto-generated method stub
       
    }
    
    public SwapTileView(ResourceFactory resourceFactory){
        this.resourceFactory = resourceFactory;
       
    }
    
    public ObservableList<FieldTileNode> swapTiles(ObservableList<Tile> currentRack){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        
        TilePane tilePane = new TilePane();
        tilePane.setOnDragDetected(event -> event.consume());
        tilePane.setPadding(new Insets(20, 10, 10,10));
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(7);
        tilePane.setStyle("-fx-background-color: gray");
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
        Button close = new Button("Sluiten");
        
        tilePane.getChildren().addAll(close, swap);
        
        close.setOnAction(event -> window.close());
        swap.setOnAction(event -> { 
            
            returnvalue = selectedTiles;
            window.close();
        });
        
       Scene scene = new Scene(tilePane);
       window.setScene(scene);
       window.showAndWait(); 
       
       if(returnvalue.isEmpty()){
           return null;
       }
       return returnvalue;
       
       
    }
   

}

