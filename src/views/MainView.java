package views;

import controllers.Controller;
import controllers.ControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainView extends View implements Initializable {
    /*declare your view here if you need it*/
    @FXML private VBox loginView;
    @FXML private VBox welcomeView;
    @FXML private VBox registerView;
    @FXML private Pane userInfoView;
    @FXML private VBox gameBoardView;
    @FXML private VBox createCompetitionView;
    @FXML private VBox passwordChangeView;
    @FXML private ProgressIndicator loadIndicator;
    @FXML private ToolBar toolBar;
    @FXML private TabPane control;
    @FXML private SplitPane mainContent;
    @FXML private StackPane content;
    @FXML private Tab gameControlView;

    /*Declare your viewControllers here*/
    @FXML private UserListView userListViewController;
    @FXML private GameListView gameListViewController;
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
        setContent(welcomeView);
        setControl(true);
        toolBar.setDisable(false);
        controllerFactory.getControllers().forEach(Controller::refresh);
        views.forEach(View::constructor);
    }

    public void showRegisterView(){
    	setContent(registerView);
    }

    public void showLoginView(){
    	setContent(loginView);
    }

    public void showUserInfo(){
        setContent(userInfoView);
    }

    public void showCreateCompetition(){
    	setContent(createCompetitionView);
    }

    public void showPasswordChangeView(){
        passwordChangeViewController.refresh();
        setContent(passwordChangeView);
    }

    @FXML
    public void refresh() {
        loadIndicator.setVisible(true);
        controllerFactory.getControllers().forEach(Controller::refresh);
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
        this.showLoginView();
        toolBar.setDisable(true);
        this.setControl(false);
        loginViewController.refresh();
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
        //this will never be called in this view
    }

    public void showGameBoardView() {
        setContent(gameBoardView);
    }

    public void showGameControlView() {control.getSelectionModel().select(gameControlView);
    }

    public GameBoardView getGameBoardView (){
       return gameBoardViewController;
    }
}