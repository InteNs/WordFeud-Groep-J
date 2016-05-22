package views;

import java.awt.BorderLayout;
import java.awt.TextArea;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Tile;
import resources.ResourceFactory;
import views.components.FieldTileNode;

public class potView extends View {
    ObservableList<Tile> tiles;
    ResourceFactory resourceFactory;
    FieldTileNode field;
    
    public potView(ResourceFactory resourceFactory){
        
    }
    
    public potView(ObservableList<Tile> pot, ResourceFactory resourceFactory){
        
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        this.resourceFactory = resourceFactory;
        tiles = pot;
       
        TilePane tilePane = new TilePane();
        tilePane.setMaxHeight(400);
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setStyle("-fx-background-color: gray");
        
        for(Tile tile: tiles){
            field = new FieldTileNode(tile, resourceFactory);
            tilePane.getChildren().add(field);
                       
        }
        
        VBox box = new VBox(tilePane);
        box.setPadding(new Insets(10, 30, 10, 10));
        
        
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(box);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setMaxHeight(500);
        
        Button button = new Button("Sluiten");
        button.setOnAction(event -> window.close());
       
        tilePane.getChildren().add(button);
       
        
        BorderPane pane = new BorderPane();
        pane.setCenter(scroll);
        pane.setBottom(button);
        pane.setAlignment(button, Pos.CENTER);
        
        
        
        Scene scene = new Scene(pane);
        
        window.setScene(scene);
        window.show();
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void constructor() {
        // TODO Auto-generated method stub
        
    }
}
