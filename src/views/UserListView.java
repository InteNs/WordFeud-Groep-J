package views;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.User;

public class UserListView extends View {
    @FXML
    private ListView<User> userList;
    @FXML
    private ListView<User> currentUserList;
    @FXML
    private TextField filterField;

    private FilteredList<User> filteredUsers;

    public void refresh(){
    }

    @Override
    public void constructor() {
        filteredUsers = new FilteredList<>(userController.getUsers());

        userList.setItems(filteredUsers);

        filterField.textProperty().addListener(observable -> {
            filteredUsers.setPredicate(user ->
                    user.getName().toLowerCase().contains(filterField.getText().toLowerCase())
            );
        });

        session.currentUserProperty().addListener((observable, oldValue, newValue) -> {
            currentUserList.getItems().setAll(newValue);
        });

        currentUserList.setOnMouseClicked(e -> select(session.getCurrentUser()));

        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                select(newValue));
    }

    private void select(User user) {
        userController.setSelectedUser(user);
        parent.showUserInfo();
    }
}


