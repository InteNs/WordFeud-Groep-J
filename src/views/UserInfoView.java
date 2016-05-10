package views;

import controllers.UserController;
import enumerations.Role;
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
    private UserController controller;

    @Override
    public void constructor() {
        controller = controllerFactory.GetUserController();
        controller.selectedUserProperty().addListener((observable, oldValue, newValue) -> {
            userNameLabel.setText(newValue.toString());
            selectedUser = newValue;
            myCompetitions.setItems(controllerFactory.getCompetitionController().getCompetitions(newValue));
            getRoles();
        });
    }

    public void getRoles() {
        if (!getSession().getCurrentUser().hasRole(Role.ADMINISTRATOR)) {
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
            controller.removeRole(selectedUser, Role.PLAYER);
        } else {
            controller.setRole(selectedUser, Role.PLAYER);
        }
    }

    public void setAdmin() {
        if (selectedUser.hasRole(Role.ADMINISTRATOR)) {
            controller.removeRole(selectedUser, Role.ADMINISTRATOR);
        } else {
            controller.setRole(selectedUser, Role.ADMINISTRATOR);
        }
    }

    public void setModerator() {
        if (selectedUser.hasRole(Role.MODERATOR)) {
            controller.removeRole(selectedUser, Role.MODERATOR);
        } else {
            controller.setRole(selectedUser, Role.MODERATOR);
        }
    }

    public void setObserver() {
        if (selectedUser.hasRole(Role.OBSERVER)) {
            controller.removeRole(selectedUser, Role.OBSERVER);
        } else {
            controller.setRole(selectedUser, Role.OBSERVER);
        }
    }

    @Override
    public void refresh() {

    }
}
