package views;

import controllers.ControllerFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

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
    @FXML public VBox challengeView;
    @FXML public ProgressIndicator loadIndicator;
    @FXML public ToggleButton threadToggle;
    @FXML public ToolBar toolBar;
    @FXML public TabPane control;
    @FXML public SplitPane mainContent;
    @FXML public StackPane content;
    @FXML public Tab gameControlView;
    @FXML public Tab userListView;
    @FXML public Tab gameListView;
    @FXML public Pane wordInfoView;

    /*Declare your viewControllers here*/
    @FXML private UserListView userListViewController;
    @FXML private GameListView gameListViewController;
    @FXML private CompetitionInfoView competitionInfoViewController;
    @FXML private CompetitionListView competitionListViewController;
    @FXML private LoginView loginViewController;
    @FXML private WelcomeView welcomeViewController;
    @FXML private RegisterView registerViewController;
    @FXML private UserInfoView userInfoViewController;
    @FXML private GameBoardView gameBoardViewController;
    @FXML private CreateCompetitionView createCompetitionViewController;
    @FXML private PasswordChangeView passwordChangeViewController;
    @FXML private gameControlView gameControlViewController;
    @FXML private WordListView wordListViewController;
    @FXML private WordInfoView wordInfoViewController;
    @FXML private ChallengeView challengeViewController;

    private ControllerFactory controllerFactory;
    private ArrayList<View> views;

    private int controlIndex;
    private double dividerPos;
    private Main applicationLoader;
    private boolean isLive;

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
                gameControlViewController,
                wordListViewController,
                wordInfoViewController,
                challengeViewController

        ));
        views.forEach(view -> view.init(this));


    }

    public void login() {
        //load controllers
        controllerFactory.fetchControllers();
        controllerFactory.refreshControllers();
        //load views
        views.forEach(View::constructor);
        //enable control
        setControl(true);
        toolBar.setDisable(false);
        constructor();
        isLive = true;
        threadToggle.setSelected(true);
    }

    @FXML
    public void refresh() {

    }

    @Override
    public void clear() {

    }

    public ControllerFactory getControllerFactory() {
        return controllerFactory;
    }

    @FXML
    public void toggleControl(ActionEvent actionEvent) {
        setControl(!((ToggleButton) actionEvent.getSource()).isSelected());
    }

    @FXML
    public void logOut() {
        applicationLoader.loadApp();
    }

    /**
     * set the control (tab pane) visible or not (makes content fill window)
     * @param visible whether the control tabs should be visible
     */
    public void setControl(Boolean visible) {
        if (visible) {
            mainContent.getItems().add(controlIndex, control);
            mainContent.setDividerPositions(dividerPos);
        } else if (mainContent.getItems().contains(control)) {
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
        //disable control tab if no game selected
        gameControlView.setDisable(controllerFactory.getGameController().getSelectedGame() == null);
        controllerFactory.getGameController().selectedGameProperty().addListener((observable, oldValue, newValue) -> {
            gameControlView.setDisable(newValue == null);
        });

        //when controltab is selected, the gameboardview will show
        control.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == gameControlView) setContent(gameBoardView);
        });

        //define and start live reload thread
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        scheduler.scheduleAtFixedRate((() -> {
            if (!isLive) return;
            loadIndicator.setVisible(true);
            controllerFactory.fetchControllers();
            Platform.runLater(() -> {
                controllerFactory.refreshControllers();
                views.forEach(View::refresh);
                loadIndicator.setVisible(false);
            });
        }), 0, 2, SECONDS);
    }

    public void setApplicationLoader(Main applicationLoader) {
        this.applicationLoader = applicationLoader;
    }

    public void changePass() {
        setContent(passwordChangeView);
    }

    public GameBoardView getGameBoardView() {
        return gameBoardViewController;
    }

    public WordInfoView getWordInfoView() {
        return wordInfoViewController;
    }

    public ChallengeView getChallengeView(){
        return challengeViewController;
    }

    public void doThread(ActionEvent actionEvent) {
        isLive = ((ToggleButton)actionEvent.getSource()).isSelected();
    }
}