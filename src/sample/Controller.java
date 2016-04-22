package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML private Button testbutton;

    public void doStuff() {
        testbutton.setText("click!");
    }
}
