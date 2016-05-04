package views;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeView extends View {

    @FXML private Label helloUserLabel;

    @Override
    public void refresh() {
        helloUserLabel.setText("Hello "+ userController.getCurrentUser().getName() + "!");
    }

    @Override
    public void constructor() {

    }
}
