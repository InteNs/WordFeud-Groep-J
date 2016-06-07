package views;

import enumerations.Role;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import models.Competition;
import models.User;

import java.util.Objects;
import java.util.function.Predicate;

public class UserListView extends View {
    @FXML private TextField currentUserField;
    @FXML private TextField filterField;
    @FXML private ListView<User> allUsersList;
    @FXML private ListView<User> compUsersList;
    @FXML private TitledPane compUsersPane;
    @FXML private TitledPane allUsersPane;
    @FXML private SplitPane lists;

    private FilteredList<User> filteredUsers;
    private Predicate<User> filterText, filterComp;

    public void refresh() {
        loadStats();
        if (competitionController.getSelectedCompetition() != null) {
            showCompUsers(competitionController.getSelectedCompetition(), false);
            //compUsersList.setItems(filteredUsers.filtered(filterComp));
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void constructor() {

        filteredUsers = new FilteredList<>(userController.getUsers());

        filterText = user ->
                user.getName().toLowerCase().contains(filterField.getText().toLowerCase());
        filterComp = user -> competitionController.getSelectedCompetition() != null
                && competitionController.getSelectedCompetition().getPlayers().contains(user);

        allUsersList.setItems(filteredUsers);

        if (!session.getCurrentUser().hasRole(Role.ADMINISTRATOR))
            lists.getItems().remove(allUsersPane);

        competitionController.selectedCompetitionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.deepEquals(oldValue)) {
                compUsersList.setItems(filteredUsers.filtered(filterComp));
                showCompUsers(newValue, true);
            }
        });

        filterField.textProperty().addListener(observable -> {
            filteredUsers.setPredicate(null);
            filteredUsers.setPredicate(filterText);
        });

        currentUserField.setText(session.getCurrentUser().toString());
        currentUserField.setOnMouseClicked(e -> select(session.getCurrentUser()));

        allUsersList.getSelectionModel().selectedItemProperty()
                .addListener((o, oldValue, newValue) -> {
                    if (newValue != null && !Objects.equals(oldValue, newValue)) select(newValue);
                });

        compUsersList.getSelectionModel().selectedItemProperty()
                .addListener((o, oldValue, newValue) -> {
                    if (newValue != null && !Objects.equals(oldValue, newValue))
                        selectChallenge(newValue);
                });
    }

    private void showCompUsers(Competition newValue, boolean isNew) {
        if (!newValue.getPlayers().isEmpty()) {
            if (!lists.getItems().contains(compUsersPane))
                lists.getItems().add(compUsersPane);

            compUsersPane.setText("Accounts binnen: " + newValue.toString());
            if (isNew) lists.setDividerPosition(0, 0.5);
        } else lists.getItems().remove(compUsersPane);
    }

    private void selectChallenge(User user) {
        parent.getChallengeView().clear();
        userController.setSelectedUser(user);
        parent.setContent(parent.challengeView);
    }

    private void select(User user) {
        userController.setSelectedUser(user);
        parent.setContent(parent.userInfoView);
    }

    private void loadStats(){
        userController.setAllStats();
    }
}


