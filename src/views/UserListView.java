package views;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.User;
import java.util.function.Predicate;

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
        Predicate<User> filterCurrent = user ->
                user.equals(session.getCurrentUser());
        Predicate<User> filterText = user ->
                user.getName().toLowerCase().contains(filterField.getText().toLowerCase());

        userList.setItems(filteredUsers);
        currentUserList.setItems(filteredUsers.filtered(filterCurrent));

        filterField.textProperty().addListener(observable ->
            filteredUsers.setPredicate(filterText)
        );

        currentUserList.setOnMouseClicked(e -> select(session.getCurrentUser()));

        userList.getSelectionModel().selectedItemProperty().addListener((o1, o2, newValue) ->
                select(newValue));
    }

    private void select(User user) {
        userController.setSelectedUser(user);
        parent.showUserInfo();
    }
}


