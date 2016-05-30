package views;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import models.User;
import java.util.function.Predicate;

public class UserListView extends View {
    @FXML private ListView<User> userList;
    @FXML private ListView<User> currentUserList;
    @FXML private ListView<User> compUserList;
    @FXML private TitledPane compUserPane;
    @FXML private Accordion userLists;
    @FXML private TextField filterField;

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
        Predicate<User> filterComp = user -> competitionController.getSelectedCompetition() != null
                && competitionController.getSelectedCompetition().getPlayers().contains(user);

        userList.setItems(filteredUsers);
        currentUserList.setItems(filteredUsers.filtered(filterCurrent));

        competitionController.selectedCompetitionProperty().addListener((observable, oldValue, newValue) -> {
            userLists.getPanes().remove(compUserPane);
            if(newValue != null && !newValue.getPlayers().isEmpty()) {
                userLists.getPanes().add(compUserPane);
                userLists.setExpandedPane(compUserPane);
                compUserPane.setText("Accounts binnen: " + newValue.toString());
                compUserList.setItems((filteredUsers.filtered(filterComp)));
            }
        });

        filterField.textProperty().addListener(observable ->
            filteredUsers.setPredicate(filterText)
        );

        currentUserList.setOnMouseClicked(e -> select(session.getCurrentUser()));

        userList.getSelectionModel().selectedItemProperty().addListener((o1, o2, newValue) ->
                select(newValue)
        );
        compUserList.getSelectionModel().selectedItemProperty().addListener((o1, o2, newValue) ->
        selectChallenge(newValue) 
        );
        
        
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


