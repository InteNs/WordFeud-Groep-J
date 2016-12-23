package gui;

import helpers.SnapHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage mainStage) throws Exception {
        Stage manageStage = new Stage();
        manageStage.initOwner(mainStage);

        Parent rootMain = new FXMLLoader(getClass().getResource("MainWindow.fxml")).load();
        Parent rootManage = new FXMLLoader(getClass().getResource("ManageWindow.fxml")).load();

        mainStage.setTitle("Hoofdscherm");
        manageStage.setTitle("Managmentscherm");

        mainStage.setScene(new Scene(rootMain));
        manageStage.setScene(new Scene(rootManage));

        SnapHelper.setSnappable(mainStage, mainStage, manageStage);
        SnapHelper.setSnappable(manageStage, mainStage, manageStage);

        mainStage.show();
        manageStage.show();
        SnapHelper.snap(mainStage, manageStage);
    }
}