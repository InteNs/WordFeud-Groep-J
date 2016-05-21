package views;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.ResourceFactory;

public class JokerView extends View {
    char returnValue = '!';
    char alphabet;

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
    }

    public JokerView(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public char jokerChoice() {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        // you can't close the window until you select a button
        window.initStyle(StageStyle.UNDECORATED);

        window.setTitle("selecteer hier je keuze");
        TilePane tilePane = new TilePane();
        tilePane.setStyle("-fx-background-color: gray");
        tilePane.setPrefColumns(7);
        tilePane.setVgap(10);
        tilePane.setHgap(10);
        window.setAlwaysOnTop(true);

        for (alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            ImageView imageView = new ImageView(resourceFactory.getImage(alphabet + ".png", true));
            imageView.setId("" + alphabet);
            imageView.setOnMouseClicked(EventHandler -> {
                returnValue = imageView.getId().charAt(0);
                window.close();
            });

            tilePane.getChildren().add(imageView);

        }
        VBox vBox = new VBox(tilePane);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(vBox);
        window.setScene(scene);

        window.showAndWait();

        return returnValue;
    }

}
