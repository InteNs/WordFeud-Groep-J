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
import models.Competition;
import models.User;

public class UserInfoView extends View {
    @FXML
    private Label userNameLabel;
    @FXML
    private Label lNoStats;
    @FXML
    private Label passwordLabel;
    @FXML
    private ListView<Competition> myCompetitions;
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
            if (newValue == null) {
                return;
            }
            selectedUser = newValue;

            userNameLabel.setText( newValue.toString());
            passwordLabel.setText("wachtwoord: " + selectedUser.getPassword());
            myCompetitions.setItems(competitionController.getCompetitions(selectedUser));

            if (!session.getCurrentUser().hasRole(Role.ADMINISTRATOR)) {
                rolesPane.setVisible(false);
                passwordLabel.setVisible(false);
            }
            getRoles(selectedUser);
            setStats(selectedUser);
        });
    }

    private void setStats(User selectedUser) {
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

    private void getRoles(User selectedUser) {
        checkPlayer.setSelected(selectedUser.hasRole(Role.PLAYER));
        checkModerator.setSelected(selectedUser.hasRole(Role.MODERATOR));
        checkAdmin.setSelected(selectedUser.hasRole(Role.ADMINISTRATOR));
        checkObserver.setSelected(selectedUser.hasRole(Role.OBSERVER));
    }

    @FXML
    public void setPlayer() {
        userController.setRole(selectedUser, Role.PLAYER, checkPlayer.isSelected());
    }

    @FXML
    public void setAdmin() {
        userController.setRole(selectedUser, Role.ADMINISTRATOR, checkAdmin.isSelected());
    }

    @FXML
    public void setModerator() {
        userController.setRole(selectedUser, Role.MODERATOR, checkModerator.isSelected());
    }

    @FXML
    public void setObserver() {
        userController.setRole(selectedUser, Role.OBSERVER, checkObserver.isSelected());
    }

    @Override
    public void refresh() {

    }
}
