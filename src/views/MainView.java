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

    //some sort of session placeholder
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginViewController.setParent(this);
        gameListController.setParent(this);
    }

    @FXML
    public void switchLayout() {
        Collections.swap(content.getItems(), 0, 1);
    }

    public void login(User user) {
        currentUser = user;
        stackPane.getChildren().clear();
        refresh();
    }

    @FXML
    public void refresh() {
        gameListController.refresh();
    }
}
