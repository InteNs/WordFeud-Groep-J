package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class RegisterView extends View {

    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField userPassField1;
    @FXML
    private PasswordField userPassField2;
    @FXML
    private Label invalidUserLabel;
    @FXML
    private Label usernameReq;
    @FXML
    private Label passwordReq;

    @Override
    public void refresh() {
    }

    @Override
    public void constructor() {
    }

    @FXML
    public void register() {
        if (!userController.userExists(userNameField.getText())) {
            if (checkUserName(userNameField.getText())
                    && checkPassword(userPassField1.getText(), userPassField2.getText())) {
                userController.insertUser(userNameField.getText(), userPassField1.getText());
                invalidUserLabel.setTextFill(Color.web("#00ff00"));
                invalidUserLabel.setText("registreren geslaagd");
                invalidUserLabel.setVisible(true);
            }
        } else {
            invalidUserLabel.setText("gebruikersnaam niet beschikbaar");
            invalidUserLabel.setTextFill(Color.web("#ff0000"));
            invalidUserLabel.setVisible(true);
        }
    }

    @FXML
    public void openLoginView() {
        parent.showLoginView();
    }

    // Check if username meets the requirements (5 -25 char)
    private boolean checkUserName(String username) {
        if (userController.isValidUsername(username)) {
            usernameReq.setTextFill(Color.web("#000000"));
            invalidUserLabel.setVisible(false);
            return true;
        }
        usernameReq.setTextFill(Color.web("#ff0000"));
        invalidUserLabel.setText("gebruikernaam voldoet niet aan de eisen");
        invalidUserLabel.setVisible(true);
        return false;

    }

    // Check if username meets the requirements (pass 1 = pass2, length: 5 - 25 )
    private boolean checkPassword(String password1, String password2) {
        if (password1.equals(password2)) {
            if (userController.isValidPassword(password1)) {
                passwordReq.setTextFill(Color.web("#000000"));
                invalidUserLabel.setVisible(false);
                return true;
            } else {
                passwordReq.setTextFill(Color.web("#ff0000"));
                invalidUserLabel.setText("wachtwoord voldoet niet aan de eisen");
                invalidUserLabel.setVisible(true);
                return false;
            }
        }
        invalidUserLabel.setText("wachtwoord komt niet overeen");
        invalidUserLabel.setVisible(true);
        return false;
    }
}
