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

public class UserInfoView extends View implements Initializable {

    @FXML private Label userNameLabel;
    @FXML private TextField userNameField;
    @FXML private PasswordField userPassField;
    @FXML private ListView<String> myCompetitions;



    private UserController userController;
    private User user;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userController = new UserController();

    }
    public void setUser(User user){
        this.user = user;
        userNameLabel.setText(user.toString());
        getComps();
    }
    private void getComps(){
        myCompetitions.setItems(FXCollections.observableArrayList(userController.getComps(user)));
        //myCompetitions.getItems().setAll(userController.getMyComps(user.toString()));

    }


    @Override
    public void refresh() {

    }
}
