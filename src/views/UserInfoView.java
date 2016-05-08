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
        if (!userController.getCurrentUser().hasRole("administrator")) {
            checkAdmin.setDisable(true);
            checkPlayer.setDisable(true);
            checkObserver.setDisable(true);
            checkModerator.setDisable(true);
        }

        checkPlayer.setSelected(false);
        checkModerator.setSelected(false);
        checkObserver.setSelected(false);
        checkAdmin.setSelected(false);

        if (selectedUser.hasRole("player")) {
            checkPlayer.setSelected(true);
        }
        if (selectedUser.hasRole("moderator")) {
            checkModerator.setSelected(true);
        }
        if (selectedUser.hasRole("administrator")) {
            checkAdmin.setSelected(true);
        }
        if (selectedUser.hasRole("observer")) {
            checkObserver.setSelected(true);
        }
    }

    public void setPlayer() {
        if (selectedUser.hasRole("player")) {
            userController.removeRole(selectedUser, "player");
        } else {
            userController.setRole(selectedUser, "player");
        }
    }

    public void setAdmin() {
        if (selectedUser.hasRole("administrator")) {
            userController.removeRole(selectedUser, "administrator");
        } else {
            userController.setRole(selectedUser, "administrator");
        }
    }

    public void setModerator() {
        if (selectedUser.hasRole("moderator")) {
            userController.removeRole(selectedUser, "moderator");
        } else {
            userController.setRole(selectedUser, "moderator");
        }
    }

    public void setObserver() {
        if (selectedUser.hasRole("observer")) {
            userController.removeRole(selectedUser, "observer");
        } else {
            userController.setRole(selectedUser, "observer");
        }
    }


    @Override

    public void refresh() {

    }
}
