package controllers;

import java.util.Arrays;
import java.util.List;

public class ControllerFactory {

    private static ControllerFactory controllerFactory = new ControllerFactory();

    private UserController userController;
    private GameController gameController;
    private CompetitionController competitionController;
    private SessionController sessionController;
    private WordController wordController;
    private FeedbackController feedbackController;

    public List<Controller> getControllers() {
        return Arrays.asList(
                GetUserController(),
                getGameController(),
                getCompetitionController(),
                getSessionController(),
                getWordController()
        );
    }

    public static ControllerFactory getInstance() {
        if (controllerFactory == null) controllerFactory = new ControllerFactory();
        return controllerFactory;
    }

    public static void destroyInstance() {
        controllerFactory = null;
    }

    public void resetControllers() {
        userController = null;
        gameController = null;
        competitionController = null;
        sessionController = null;
    }

    public void refreshControllers() {
        getControllers().forEach(Controller::refill);
        getControllers().forEach(Controller::refresh);
    }

    public void fetchControllers() {
        getControllers().forEach(Controller::fetch);
    }

    public UserController GetUserController(){
        if(userController == null)
            userController = new UserController();
        return userController;
    }

    public FeedbackController getFeedbackController(){
        if(feedbackController == null)
            feedbackController = new FeedbackController();
        return feedbackController;
    }

    public CompetitionController getCompetitionController() {
        if(competitionController == null)
            competitionController = new CompetitionController();
        return competitionController;
    }

    public GameController getGameController() {
        if (gameController == null)
            gameController = new GameController();
        return gameController;
    }

    public SessionController getSessionController() {
        if (sessionController == null)
            sessionController = new SessionController();
        return sessionController;
    }

    public WordController getWordController() {
        if (wordController== null)
            wordController = new WordController();
        return wordController;
    }
}
