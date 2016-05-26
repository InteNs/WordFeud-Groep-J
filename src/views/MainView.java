package views;

import controllers.ControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainView extends View implements Initializable {
    /*declare your view here if you need it*/
    @FXML public VBox loginView;
    @FXML public VBox welcomeView;
    @FXML public VBox registerView;
    @FXML public Pane userInfoView;
    @FXML public VBox gameBoardView;
    @FXML public VBox createCompetitionView;
    @FXML public VBox competitionInfoView;
    @FXML public VBox passwordChangeView;
    @FXML public ProgressIndicator loadIndicator;
    @FXML public ToolBar toolBar;
    @FXML public TabPane control;
    @FXML public SplitPane mainContent;
    @FXML public StackPane content;
    @FXML public Tab gameControlView;
    @FXML public Tab userListView;
    @FXML public Tab gameListView;

    /*Declare your viewControllers here*/
    @FXML private UserListView userListViewController;
    @FXML private GameListView gameListViewController;
    @FXML private CompetitionInfoView competitionInfoViewController;
    @FXML private CompetitionListView competitionListViewController;
    @FXML private LoginView    loginViewController;
    @FXML private WelcomeView  welcomeViewController;
    @FXML private RegisterView  registerViewController;
    @FXML private UserInfoView  userInfoViewController;
    @FXML private GameBoardView gameBoardViewController;
    @FXML private CreateCompetitionView createCompetitionViewController;
    @FXML private PasswordChangeView passwordChangeViewController;
    @FXML private gameControlView gameControlViewController;

    private ControllerFactory controllerFactory;
    private ArrayList<View> views;

    private int controlIndex;
    private double dividerPos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControl(false);
        setContent(loginView);
        controllerFactory = new ControllerFactory();
        /*
        Put your viewController in this list for it
        get access to parent, and have it's constructor called
        */
        views = new ArrayList<>(Arrays.asList(
                userListViewController,
                gameListViewController,
                competitionInfoViewController,
                competitionListViewController,
                loginViewController,
                welcomeViewController,
                registerViewController,
                userListViewController,
                gameBoardViewController,
                userInfoViewController,
                createCompetitionViewController,
                passwordChangeViewController,
                gameControlViewController
        ));
        views.forEach(view -> view.init(this));
    }

    public void login() {
        controllerFactory.refreshControllers();
        views.forEach(View::constructor);
        constructor();
    }

    @FXML
    public void refresh() {
        loadIndicator.setVisible(true);
        controllerFactory.refreshControllers();
        loadIndicator.setVisible(false);
    }

    public ControllerFactory getControllerFactory() {
        return controllerFactory;
    }

    @FXML
    public void toggleControl(ActionEvent actionEvent) {
        setControl(!((ToggleButton)actionEvent.getSource()).isSelected());
    }

    @FXML
    public void logOut() {
        this.setContent(loginView);
        this.setTab(gameListView);
        toolBar.setDisable(true);
        this.setControl(false);
        views.forEach(View::refresh);
        controllerFactory.resetControllers();
    }

    /**
     * set the control (tab pane) visible or not (makes content fill window)
     * @param visible whether the control tabs should be visible
     */
    public void setControl(Boolean visible) {
        if(visible){
            mainContent.getItems().add(controlIndex, control);
            mainContent.setDividerPositions(dividerPos);
        }
        else if(mainContent.getItems().contains(control)){
            controlIndex = mainContent.getItems().indexOf(control);
            dividerPos = mainContent.getDividerPositions()[0];
            mainContent.getItems().remove(control);
        }
    }

    public void setTab(Tab tab) {
        control.getSelectionModel().select(tab);
    }

    /**
     * set or add content to app's view (clears content if node == null)
     * @param node the node to set as content
     */
    public void setContent(Node node) {
        content.getChildren().clear();
        content.getChildren().add(node);
    }

    @Override
    public void constructor() {
        gameControlView.setDisable(controllerFactory.getGameController().getSelectedGame() == null);
        controllerFactory.getGameController().selectedGameProperty().addListener((observable, oldValue, newValue) -> {
            gameControlView.setDisable(newValue == null);
        });
        setContent(welcomeView);
        setControl(true);
        toolBar.setDisable(false);
        control.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == gameControlView) setContent(gameBoardView);
        });
    }

    public void changePass() {
        setContent(passwordChangeView);
    }

    public GameBoardView getGameBoardView (){
       return gameBoardViewController;
    }
}