import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/layout/MainView.fxml"));
        window.setTitle("WordFeud");
        window.setScene(new Scene(root));
        window.show();

        //set the minimal window size to the preferred calculated size
        window.setMinWidth(window.getWidth());
        window.setMinHeight(window.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
