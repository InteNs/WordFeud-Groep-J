package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.MainView;

import java.io.IOException;

public class Main extends Application {
    private Stage window;

    @Override
    public void start(Stage notUsed) throws Exception {
        window = notUsed;
        loadApp();
    }

    public void loadApp() {
        try {

            window.close();
            window = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/layout/MainView.fxml"));
            window.setScene(new Scene(loader.load()));
            MainView mainView = loader.getController();
            mainView.setApplicationLoader(this);

            window.setTitle("WordFeud");
            window.show();

            //set the minimal window size to the preferred calculated size
            window.setMinWidth(window.getWidth());
            window.setMinHeight(window.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getWindow() {
        return window;
    }
}
