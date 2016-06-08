package views.components;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class ScoreOverlay extends StackPane {

    private StackPane layout;

    public void setCircleInformation(int x, int y, int score){
        this.setLayoutX(x + 35);
        this.setLayoutY(y + 35);
        createCircle(score);
    }

    public void createCircle(int score) {
        Text scoreToDisplay = createText(String.valueOf(score));
        Circle scoreBubble = encircle(scoreToDisplay);

        this.setFocusTraversable(true);
        this.setPickOnBounds(false);
        layout = new StackPane();
        layout.setFocusTraversable(true);
        layout.setPickOnBounds(false);
        layout.getChildren().addAll(scoreBubble,scoreToDisplay);
        this.getChildren().clear();
        this.getChildren().add(layout);
    }

    private Text createText(String string) {
        Text text = new Text(string);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setStyle(
                "-fx-font-family: \"Arial Rounded MT Bold\";" +
                        "-fx-font-style: italic;" +
                        "-fx-font-size: 15px;"
        );
        return text;
    }

    private Circle encircle(Text text) {
        Circle circle = new Circle();
        circle.setFill(Color.YELLOW);
        circle.setStroke(Color.BLACK);
        final double PADDING = 5;
        circle.setRadius(getWidth(text) / 2 + PADDING);

        return circle;
    }

    private double getWidth(Text text) {
        new Scene(new Group(text));
        text.applyCss();

        return text.getLayoutBounds().getWidth();
    }
}
