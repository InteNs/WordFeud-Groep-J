package views;

import enumerations.Role;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
        if (!userController.getCurrentUser().hasRole(Role.ADMINISTRATOR)) {
            checkAdmin.setDisable(true);
            checkPlayer.setDisable(true);
            checkObserver.setDisable(true);
            checkModerator.setDisable(true);
        }

        checkPlayer.setSelected(false);
        checkModerator.setSelected(false);
        checkObserver.setSelected(false);
        checkAdmin.setSelected(false);

        if (selectedUser.hasRole(Role.PLAYER)) {
            checkPlayer.setSelected(true);
        }
        if (selectedUser.hasRole(Role.MODERATOR)){
            checkModerator.setSelected(true);
        }
        if (selectedUser.hasRole(Role.ADMINISTRATOR)) {
            checkAdmin.setSelected(true);
        }
        if (selectedUser.hasRole(Role.OBSERVER)) {
            checkObserver.setSelected(true);
        }
    }

    public void setPlayer() {
        if (selectedUser.hasRole(Role.PLAYER)) {
            userController.removeRole(selectedUser, Role.PLAYER);
        } else {
            userController.setRole(selectedUser, Role.PLAYER);
        }
    }

    public void setAdmin() {
        if (selectedUser.hasRole(Role.ADMINISTRATOR)) {
            userController.removeRole(selectedUser, Role.ADMINISTRATOR);
        } else {
            userController.setRole(selectedUser, Role.ADMINISTRATOR);
        }
    }

    public void setModerator() {
        if (selectedUser.hasRole(Role.MODERATOR)) {
            userController.removeRole(selectedUser, Role.MODERATOR);
        } else {
            userController.setRole(selectedUser, Role.MODERATOR);
        }
    }

    public void setObserver() {
        if (selectedUser.hasRole(Role.OBSERVER)) {
            userController.removeRole(selectedUser, Role.OBSERVER);
        } else {
            userController.setRole(selectedUser, Role.OBSERVER);
        }
    }

    @Override
    public void refresh() {

    }
}
