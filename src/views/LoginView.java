package views;

import controllers.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by markhavekes on 29/04/16.
 */
public class LoginView implements Initializable {
    @FXML private TextField userNameField;
    @FXML private PasswordField userPassField;
    @FXML private Label label;

    private LoginController loginController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginController = new LoginController();
    }


    @FXML
    public void login(){
         label.setText(userPassField.getText());
    }
}
