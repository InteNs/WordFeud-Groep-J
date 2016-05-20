package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Field;
import models.Game;
import models.Tile;
import models.Turn;
import views.components.FieldTileNode;

public class JokerView extends View{
    
    @Override
    public void refresh() {       
    }

    @Override
    public void constructor() {        
    }
    
        
    public char jokerChoice (){
        
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        // you can't close the window until you select a button
        /* window.setOnCloseRequest(e -> {      
            e.consume();           
        });*/
        
        window.setTitle("selecteer hier je keuze");
        window.setMinWidth(800);
        window.setMinHeight(400);
        System.out.println("test");
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(5,5,5,5));
        ImageView aImageView = new ImageView(new Image("A.png", true));
        System.out.println("test");
        tilePane.getChildren().add(aImageView);
        
        
        Scene scene = new Scene(tilePane);       
        window.setScene(scene);
        window.showAndWait();       
        
        
        
        return 'a';
    }
        
      
    

}
