package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

public class PasswordChangeView extends View {

    @FXML
    private PasswordField oldPassField;
    @FXML
    private PasswordField newPassField1;
    @FXML
    private PasswordField newPassField2;
    @FXML
    private Label password;
    @FXML
    private String finalPassword;

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {
        oldPassField.clear();
        newPassField1.clear();
        newPassField2.clear();
        password.setVisible(false);
        finalPassword = null;
    }

    @Override
    public void constructor() {
    }

    /**
     * If the checkPassword method returns true, calls the changePassword method in userController with the
     * new password as parameter.
     */
    public void changePassword() {
        if (checkPassword()) {
            userController.changePassword(finalPassword);
            password.setTextFill(Color.GREEN);
            password.setText("Het wachtwoord is succesvol veranderd");
            password.setVisible(true);
        }
    }

    private boolean checkPassword() {
        password.setTextFill(Color.RED);
        /**Check if filled in oldPassword matches with the password in use */
        if (userController.checkPassword(session.getCurrentUser(), oldPassField.getText())) {
            /**Check if the new passwords match */
            if (newPassField1.getText().equals(newPassField2.getText())) {
                /**Check if the new password is a valid one */
                if (userController.isValidPassword(newPassField1.getText())) {
                    finalPassword = newPassField1.getText();
                    return true;
                } else {
                    password.setText("Het nieuwe wachtwoord is niet goed, probeer het opnieuw");
                    return false;
                }
            } else {
                password.setText("Nieuwe wachtwoorden komen niet overeen, probeer het opnieuw");
                return false;
            }
        } else {
            password.setText("Het oude wachtwoord komt niet overeen, probeer het opnieuw");
            return false;
        }
    }
}