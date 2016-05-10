package views;

import controllers.UserController;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.User;

public class UserListView extends View {
    @FXML
    private ListView<User> userList;
    @FXML
    private TextField filterField;

    private FilteredList<User> filteredUsers;

    private UserController controller;

    public void refresh(){

    }

    @Override
    public void constructor() {
        controller = controllerFactory.GetUserController();
        filteredUsers = new FilteredList<>(controller.getUsers());

        userList.setItems(filteredUsers);

        filterField.textProperty().addListener(observable -> {
            filteredUsers.setPredicate(user ->
                    user.getName().toLowerCase().contains(filterField.getText().toLowerCase())
            );
        });

        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            controller.setSelectedUser(newValue);
            parent.showUserInfo();
        });
    }
}


