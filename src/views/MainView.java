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
    @FXML private LoginView loginViewController; //1st child of Stackpane
    @FXML private WelcomeView welcomeViewController; //2nd child of Stackpane

    //some sort of session placeholder
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginViewController.setParent(this);
        gameListController.setParent(this);
        welcomeViewController.setParent(this);
    }

    @FXML
    public void switchLayout() {
        Collections.swap(content.getItems(), 0, 1);
    }

    public void login(User user) {
        currentUser = user;
        welcomeViewController.setUserLabel(currentUser);
        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(true);
        refresh();
    }

    @FXML
    public void refresh() {
        gameListController.refresh();
    }
}
