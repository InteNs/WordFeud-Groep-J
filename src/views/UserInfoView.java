package views;

import controllers.UserController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoView extends View {
    @Override
    public void constructor() {
        userController.selectedUserProperty().addListener((observable, oldValue, newValue) -> {
            userNameLabel.setText(newValue.toString());
            myCompetitions.setItems(FXCollections.observableArrayList(userController.getComps(newValue)));
        });

    }

    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<String> myCompetitions;


    @Override
    public void refresh() {

    }
}
