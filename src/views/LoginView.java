package views;

import controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginView extends View implements Initializable {
    @FXML private TextField userNameField;
    @FXML private PasswordField userPassField;
    @FXML private Label label;
    @FXML private VBox root;

    private UserController userController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userController = new UserController();
    }

    @FXML
    public void login(){
        if (userController.login(userNameField.getText(),userPassField.getText())){
            parent.login(userController.getCurrentUser());
        }
    }

    @Override
    public void refresh() {
        //nothing to refresh(yet)
    }
}
