package views;

import controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterView extends View implements Initializable {

	@FXML
	private TextField userNameField;
	@FXML
	private UserController userController;
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
	public void initialize(URL location, ResourceBundle resources) {
		userController = new UserController();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@FXML
	public void register() {
		if (!userController.selectUser(userNameField.getText())) {
			if (checkUserName(userNameField.getText())) {
				if (checkPassword(userPassField1.getText(), userPassField2.getText())) {
					userController.insertUser(userNameField.getText(), userPassField1.getText());
					invalidUserLabel.setTextFill(Color.web("#00ff00"));
					invalidUserLabel.setText("registreren geslaagd");
					invalidUserLabel.setVisible(true);
				}
			}
		} else {
			invalidUserLabel.setText("gebruikersnaam niet beschikbaar");
			invalidUserLabel.setTextFill(Color.web("#ff0000"));
			invalidUserLabel.setVisible(true);
		}
	}

	@FXML
	public void openLoginView() {
		parent.toLoginView();
	}

	// Check if username meets the requirements (5 -25 char)
	private boolean checkUserName(String username) {
		if (username.length() >= 5 & username.length() <= 25) {
			if (username.matches("[a-zA-Z0-9]+")) {
				usernameReq.setTextFill(Color.web("#000000"));
				invalidUserLabel.setVisible(false);
				return true;
			}

		}
		usernameReq.setTextFill(Color.web("#ff0000"));
		invalidUserLabel.setText("gebruikernaam voldoet niet aan de eisen");
		invalidUserLabel.setVisible(true);
		System.out.println("lengte");
		return false;

	}

	// Check if username meets the requirements (pass 1 = pass2, length: 5 - 25 )
	private boolean checkPassword(String password1, String password2) {
		if (password1.equals(password2)) {
			if (password1.length() >= 5 & password1.length() <= 25) {
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
