package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import models.User;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    @FXML private SplitPane content;
    @FXML private StackPane stackPane;

    @FXML private GameListView gameListController;
    @FXML private LoginView loginViewController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginViewController.setParent(this);
        gameListController.setParent(this);
    }

    @FXML
    public void switchLayout() {
        Collections.swap(content.getItems(), 0, 1);
    }

    public User getCurrentUser() {
        //TODO return current user
        return new User("jager684");
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
