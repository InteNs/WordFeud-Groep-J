package views;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Tile;
import resources.ResourceFactory;
import views.components.FieldTileNode;

public class potView extends View {

    public potView(ObservableList<Tile> pot, ResourceFactory resourceFactory) {

        Stage window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        TilePane tilePane = new TilePane();
        tilePane.setMaxHeight(400);
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setStyle("-fx-background-color: gray");
        tilePane.setOnMouseClicked(event -> window.close());
        window.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) window.close();
        });

        for (Tile tile : pot) {
            FieldTileNode field = new FieldTileNode(tile, resourceFactory);
            tilePane.getChildren().add(field);
        }

        ScrollPane scroll = new ScrollPane();
        scroll.setPadding(new Insets(10, 10, 10, 10));
        scroll.setContent(tilePane);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setMaxHeight(400);

        Scene scene = new Scene(scroll);

        window.setScene(scene);
        window.show();
    }

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
    }
}
