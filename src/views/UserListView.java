package views;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import models.Competition;
import models.User;
import java.util.function.Predicate;

public class UserListView extends View {
    @FXML private ListView<User> userList;
    @FXML private TextField currentUserField;
    @FXML private ListView<User> compUserList;
    @FXML private TitledPane compUserPane;
    @FXML private Accordion userLists;
    @FXML private TextField filterField;

    private FilteredList<User> filteredUsers;
    private Predicate<User> filterText, filterComp;

    public void refresh(){
        showCompUsers(competitionController.getSelectedCompetition());
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

        userList.setItems(filteredUsers);

        competitionController.selectedCompetitionProperty().addListener((observable, oldValue, newValue) -> {
            showCompUsers(newValue);
        });

        filterField.textProperty().addListener(observable ->
            filteredUsers.setPredicate(filterText)
        );

        currentUserField.setText(session.getCurrentUser().toString());
        currentUserField.setOnMouseClicked(e -> select(session.getCurrentUser()));

        userList.setOnMouseClicked(event -> select(userList.getSelectionModel().getSelectedItem()));

        compUserList.getSelectionModel().selectedItemProperty().addListener((o1, o2, newValue) ->
                selectChallenge(newValue)
        );
    }

    private void showCompUsers(Competition newValue) {
        userLists.getPanes().remove(compUserPane);
        if(newValue != null && !newValue.getPlayers().isEmpty()) {
            userLists.getPanes().add(compUserPane);
            userLists.setExpandedPane(compUserPane);
            compUserPane.setText("Accounts binnen: " + newValue.toString());
            compUserList.setItems((filteredUsers.filtered(filterComp)));
        }
    }

    private void selectChallenge(User user){
        parent.getChallengeView().setDefaultText();
        userController.setSelectedUser(user);
        parent.setContent(parent.challengeView);
    }

    private void select(User user) {
        userController.setSelectedUser(user);
        parent.setContent(parent.userInfoView);
    }
}


