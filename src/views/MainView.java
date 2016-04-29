package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    @FXML
    private SplitPane content;
    @FXML
    private GameList gameListController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void switchLayout() {
        Collections.swap(content.getItems(), 0, 1);
    }

    @FXML
    public void refresh() {
        gameListController.refresh();
    }

}
