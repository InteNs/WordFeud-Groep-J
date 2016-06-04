package views.subviews;

import enumerations.Feedback;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Window;

public class FeedbackView {


    public FeedbackView(Window window, String message, Feedback feedback) {
        Popup popup = new Popup();

        Text text = new Text(message);
        VBox vBox = new VBox(20);
        text.setTextAlignment(TextAlignment.CENTER);
        switch (feedback) {
            case ERROR: text.setStyle("-fx-alignment: center; -fx-fill: red"); break;
            case WARNING: text.setStyle("-fx-alignment: center; -fx-fill: black"); break;
        }
        vBox.setOnMouseClicked(event -> popup.hide());
        vBox.getChildren().addAll(text);
        vBox.setStyle("-fx-background-color: #BDBDBD; -fx-border-color: #3c3c3c; -fx-border-width: 1px; -fx-padding: 20px");
        vBox.setAlignment(Pos.CENTER);

        popup.getContent().add(vBox);
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.show(window);
    }
}
