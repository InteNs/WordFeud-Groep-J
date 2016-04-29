package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    @FXML
    private SplitPane content;
    @FXML
    private GameList gameListController;

    @FXML private LoginView loginViewController;
    @FXML private StackPane stackPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginViewController.setParent(this);
    }

    @FXML
    public void switchLayout() {
        Collections.swap(content.getItems(), 0, 1);
    }

    @FXML
    public void removeLoginScreen() {
        stackPane.getChildren().clear();

    }

    @FXML
    public void refresh() {
        gameListController.refresh();
    }

}
