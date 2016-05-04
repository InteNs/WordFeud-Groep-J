package views;

import controllers.UserController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.User;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by cedric on 4/29/2016.
 */
public class UserListView extends View implements Initializable {
    private UserController userController;
    @FXML
    private ListView<User> userList;
    @FXML
    private TextField filterField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userController = new UserController();
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            search(oldValue, newValue);
        });
    }
    public void refresh(){
        userList.setItems(FXCollections.observableArrayList(userController.getUsers()));
    }
    private void search(String oldVal, String newVal) {
        if (oldVal != null && (newVal.length() < oldVal.length())) {
            //actor has deleted a character, so reset the search
            userList.getItems().clear();
            userList.getItems().addAll(userController.getUsers());
        }
        userList.getItems().clear();
        //add an item if any item that exists contains any value that has been searched for
        userController.getUsers().stream()
                .filter(entry -> entry.getName().contains(newVal))
                .forEach(entry -> userList.getItems().add(entry));
    }

}


