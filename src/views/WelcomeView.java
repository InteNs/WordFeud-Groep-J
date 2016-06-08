package views;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class WelcomeView extends View {

    @FXML private Label helloUserLabel;
    @FXML private Text label2;

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {
        helloUserLabel.setText("Hallo "+ session.getCurrentUser() + "!");
        if (session.getCurrentUser().getRoles().filtered(role -> role != null).isEmpty()){
            label2.setText("Welkom in Wordfeud! \nU heeft geen rollen en kunt daarom niets doen. \nNeem contant op met een administrator om een nieuwe rol te krijgen.");
            label2.setTextAlignment(TextAlignment.CENTER);
        } else {
            label2.setText("Welkom in Wordfeud! \nVia de tab-bladen in het control paneel links kunt u navigeren. \n \nVeel plezier met spelen,\nxxx projectgroep J");
            label2.setTextAlignment(TextAlignment.CENTER);
        }
    }
}
