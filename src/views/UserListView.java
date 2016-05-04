package views;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.User;

public class UserListView extends View {
    @FXML
    private ListView<User> userList;
    @FXML
    private TextField filterField;

    public void refresh(){
        userList.setItems(FXCollections.observableArrayList(userController.getUsers()));
    }

    @Override
    public void constructor() {

    }
}


