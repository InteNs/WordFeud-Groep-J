package views;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.User;

public class WelcomeView extends View {

    @FXML private Label helloUserLabel;

    @Override
    public void refresh() {

    }

    @Override
    public void constructor() {
        userController.currentUserProperty().addListener((observable, oldValue, newValue) ->
                helloUserLabel.setText("Hello "+ newValue + "!"))
        ;
    }
}
