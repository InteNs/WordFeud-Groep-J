package views;

import enumerations.Role;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.User;

public class UserInfoView extends View {
    @FXML
    private Label userNameLabel;
    @FXML
    private Label lNoStats;
    @FXML
    private Label passwordLabel;
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
    @FXML
    private Pane rolesPane;
    @FXML
    private PieChart winloseChart;

    private User selectedUser;


    @Override
    public void constructor() {
        userController.selectedUserProperty().addListener((observable, oldValue, newValue) -> {
            userNameLabel.setText( newValue.toString());
            passwordLabel.setText("wachtwoord: " + newValue.getPassWord());
            selectedUser = newValue;
            myCompetitions.setItems(competitionController.getCompetitions(newValue));
            getRoles();
            setStats();
        });
    }

    public void setStats() {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), lNoStats);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);

        if (selectedUser.getWins() == 0 && selectedUser.getLoses() == 0) {
            lNoStats.setVisible(true);
            winloseChart.setVisible(false);
            ft.play();

        } else {
            winloseChart.setOpacity(1);
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Gewonnen", selectedUser.getWins()),
                            new PieChart.Data("Verloren", selectedUser.getLoses()));
            winloseChart.setVisible(true);
            winloseChart.setData(pieChartData);
            lNoStats.setVisible(false);
        }
    }

    public void getRoles() {
        if (!session.getCurrentUser().hasRole(Role.ADMINISTRATOR)) {
            rolesPane.setVisible(false);
            passwordLabel.setVisible(false);
        }

        checkPlayer.setSelected(false);
        checkModerator.setSelected(false);
        checkObserver.setSelected(false);
        checkAdmin.setSelected(false);

        if (selectedUser.hasRole(Role.PLAYER)) {
            checkPlayer.setSelected(true);
        }
        if (selectedUser.hasRole(Role.MODERATOR)) {
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
