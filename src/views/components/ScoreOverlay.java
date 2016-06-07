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

public class ScoreOverlay extends Label {

    public void nignog(int x,int y){
        this.setLayoutX(100);
        this.setLayoutY(100);
        this.setStyle("-fx-background-color: brown");
    }

    public ScoreOverlay(int score) {
        this.setText(String.valueOf(score));
        this.setStyle("-fx-background-color: brown");
//        Text scoreToDisplay = createText(String.valueOf(score));
//        Circle scoreBubble = encircle(scoreToDisplay);
//
//        this.setFocusTraversable(true);
//        this.setPickOnBounds(false);
//        StackPane layout = new StackPane();
//        layout.setFocusTraversable(true);
//        layout.setPickOnBounds(false);
//        layout.getChildren().addAll(scoreBubble,scoreToDisplay);
//        this.getChildren().add(layout);
    }

    private Text createText(String string) {
        Text text = new Text(string);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setStyle(
                "-fx-font-family: \"Verdana\";" +
//                        "-fx-font-style: bold;" +
                        "-fx-font-size: 15px;"
        );

        return text;
    }

    private Circle encircle(Text text) {
        Circle circle = new Circle();
        circle.setFill(Color.ORCHID);
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
