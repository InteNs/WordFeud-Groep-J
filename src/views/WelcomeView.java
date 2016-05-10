package views;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeView extends View {

    @FXML private Label helloUserLabel;

    @Override
    public void refresh() {

    }

    @Override
    public void constructor() {
        getSession().currentUserProperty().addListener((observable, oldValue, newValue) ->
                helloUserLabel.setText("Hallo "+ newValue + "!"));
    }
}
