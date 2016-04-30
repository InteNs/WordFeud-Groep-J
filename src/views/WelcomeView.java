package views;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeView extends View implements Initializable {

    @FXML private Label helloUserLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setUserLabel(User currentUser){
        helloUserLabel.setText("Hello "+currentUser.getName()+"!");
    }

    @Override
    public void refresh() {

    }
}
