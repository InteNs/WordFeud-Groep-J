package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class LoginView extends View {

    @FXML private Label invalidUserLabel;
    @FXML private TextField userNameField;
    @FXML private PasswordField userPassField;
    @FXML private ProgressIndicator progress;

    @FXML
    public void login(){
        if (session.login(userNameField.getText(), userPassField.getText())) {
            progress.setVisible(true);
            parent.login();
            clear();
        } else
            invalidUserLabel.setVisible(true);
    }
    @FXML
    public void openRegisterView () {
        parent.setContent(parent.registerView);
    }

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {
        userNameField.clear();
        userPassField.clear();
        invalidUserLabel.setVisible(false);
        progress.setVisible(false);
    }

    @Override
    public void constructor() {
    }
}
