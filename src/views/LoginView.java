package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginView extends View {

    @FXML private Label invalidUserLabel;
    @FXML private TextField userNameField;
    @FXML private PasswordField userPassField;

    @FXML
    public void login(){
        if (userController.login(userNameField.getText(),userPassField.getText())){
            parent.login();
            invalidUserLabel.setVisible(false);
        } else
            invalidUserLabel.setVisible(true);
    }
    @FXML
    public void openRegisterView () {
        parent.showRegisterView();
    }

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
    }
    
    public void resetFields(){
    	userNameField.setText(null);
    	userPassField.setText(null);
    }
}
