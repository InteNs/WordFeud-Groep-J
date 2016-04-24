package helpers;

import javafx.stage.Stage;

public class SnapHelper {
    public static void setSnappable(Stage stage, Stage main, Stage secondary) {
        stage.xProperty().addListener(e -> trySnap(main, secondary));
        stage.yProperty().addListener(e -> trySnap(main, secondary));
    }

    public static void snap(Stage main, Stage secondary) {
        secondary.setX(main.getX() + main.getWidth());
        secondary.setY(main.getY());
    }

    private static void trySnap(Stage main, Stage secondary) {
        double distanceX = Math.abs((main.getX()+main.getWidth())-secondary.getX());
        double distanceY = Math.abs(main.getY()-secondary.getY());
        if(distanceX <= 40 && distanceY <= 40) {
            snap(main, secondary);
            System.out.println(distanceX + distanceY);
        }
    }
}