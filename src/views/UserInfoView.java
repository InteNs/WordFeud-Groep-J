package views;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.CheckBox;
import models.User;

public class UserInfoView extends View {
    @FXML
    private Label userNameLabel;
    @FXML
    private ListView<String> myCompetitions;
    @FXML
    private CheckBox checkPlayer;
    @FXML
    private CheckBox checkModerator;
    @FXML
    private CheckBox checkAdmin;
    @FXML
    private CheckBox checkObserver;
    private User selectedUser;

    @Override
    public void constructor() {
        userController.selectedUserProperty().addListener((observable, oldValue, newValue) -> {
            userNameLabel.setText(newValue.toString());
            selectedUser = newValue;
            myCompetitions.setItems(FXCollections.observableArrayList(competitionController.getComps(newValue)));
            getRoles();
        });
    }


    public void getRoles() {
        if (!userController.getCurrentUser().hasRole(User.Role.administrator)) {
            checkAdmin.setDisable(true);
            checkPlayer.setDisable(true);
            checkObserver.setDisable(true);
            checkModerator.setDisable(true);
        }

        checkPlayer.setSelected(false);
        checkModerator.setSelected(false);
        checkObserver.setSelected(false);
        checkAdmin.setSelected(false);

        if (selectedUser.hasRole(User.Role.player)) {
            checkPlayer.setSelected(true);
        }
        if (selectedUser.hasRole(User.Role.moderator)){
            checkModerator.setSelected(true);
        }
        if (selectedUser.hasRole(User.Role.administrator)) {
            checkAdmin.setSelected(true);
        }
        if (selectedUser.hasRole(User.Role.observer)) {
            checkObserver.setSelected(true);
        }
    }

    public void setPlayer() {
        if (selectedUser.hasRole(User.Role.player)) {
            userController.removeRole(selectedUser, User.Role.player);
        } else {
            userController.setRole(selectedUser, User.Role.player);
        }
    }

    public void setAdmin() {
        if (selectedUser.hasRole(User.Role.administrator)) {
            userController.removeRole(selectedUser, User.Role.administrator);
        } else {
            userController.setRole(selectedUser, User.Role.administrator);
        }
    }

    public void setModerator() {
        if (selectedUser.hasRole(User.Role.moderator)) {
            userController.removeRole(selectedUser, User.Role.moderator);
        } else {
            userController.setRole(selectedUser, User.Role.moderator);
        }
    }


    public void setObserver() {
        if (selectedUser.hasRole(User.Role.observer)) {
            userController.removeRole(selectedUser, User.Role.observer);
        } else {
            userController.setRole(selectedUser, User.Role.observer);
        }
    }


    @Override

    public void refresh() {

    }
}
