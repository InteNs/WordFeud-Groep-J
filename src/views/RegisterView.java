package views;

import controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterView extends View implements Initializable{
	
	@FXML private TextField userNameField;
	
	@FXML private UserController userController;
	@FXML private PasswordField userPassField1;
	@FXML private PasswordField userPassField2;
	@FXML private Label invalidUserLabel; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userController = new UserController();		
	}
	
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void register (){		
		if(!userController.userExists(userNameField.getText())){
			if(checkPassword(userPassField1.getText(), userPassField2.getText())){
				invalidUserLabel.setVisible(false);
				userController.addUser(userNameField.getText(), userPassField1.getText());				
			}
		} else {
		invalidUserLabel.setText("gebruikersnaam niet beschikbaar");
		invalidUserLabel.setVisible(true);		
		}
	}		
	
	@FXML
	public void openLoginView (){
		parent.toLoginView();
	}
	
	private boolean checkPassword (String password1, String password2){
		if (password1.equals(password2)) {
			return true;			
		} 
		invalidUserLabel.setText("wachtwoord komt niet overeen");
		invalidUserLabel.setVisible(true);
		return false;
	}

	

}
