package views.subviews;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.ResourceFactory;
import views.MainView;
import views.View;

public class JokerView extends View {
    private char returnValue = '!';
    private char alphabet;
    private MainView mainView;

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
    }

    public JokerView(ResourceFactory resourceFactory, MainView mainView) {
        this.resourceFactory = resourceFactory;
        this.mainView = mainView;
    }

    public char jokerChoice() {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        // you can't close the window until you select a button
        window.initStyle(StageStyle.UNDECORATED);

        TilePane tilePane = new TilePane();
        tilePane.setStyle("-fx-background-color: gray");
        tilePane.setPrefColumns(7);
        tilePane.setVgap(10);
        tilePane.setHgap(10);
        window.setAlwaysOnTop(true);

        Label infoLabel = new Label ("Klik hier je keuze aan of toets de letter in op je toetsenbord");
        infoLabel.setFont(Font.font ("Verdana", 20));
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0,0,15,0));
        hbox.getChildren().add(infoLabel);
        hbox.setAlignment(Pos.CENTER);

        // Loops trough the alphabet, gets the image for every letter, sets a mouselistener and displays them.
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
        BorderPane borderpane = new BorderPane();
        borderpane.setBottom(hbox);
        borderpane.setCenter(vBox);
        Scene scene = new Scene(borderpane);
        window.setScene(scene);
        // when the image is selected this method returns the selecter character
        scene.setOnKeyPressed(e -> {
            for (alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                if (e.getCode().toString().equals("" + alphabet)) {
                    returnValue = alphabet; window.close(); return;
                }
            }
        });

        // Displays the jokerview centered on the application
        window.show();
        window.setX(mainView.gameBoardView.getScene().getWindow().getX() + mainView.gameBoardView.getScene().widthProperty().intValue() /2 - window.getWidth()/2);
        window.setY(mainView.gameBoardView.getScene().getWindow().getY() + mainView.gameBoardView.getScene().heightProperty().intValue() /2 - window.getHeight()/2);
        window.hide();
        window.showAndWait();
        return returnValue;
    }
}
