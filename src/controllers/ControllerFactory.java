package controllers;

import java.util.Arrays;
import java.util.List;

public class ControllerFactory {

    private UserController userController;
    private GameController gameController;
    private CompetitionController competitionController;
    private SessionController sessionController;
    private WordController wordController;

    public List<Controller> getControllers() {
        return Arrays.asList(
                GetUserController(),
                getGameController(),
                getCompetitionController(),
                getSessionController(),
                getWordController()
        );
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
            userController = new UserController(this);
        return userController;
    }

    public CompetitionController getCompetitionController(){
        if(competitionController == null)
            competitionController = new CompetitionController(this);
        return competitionController;
    }

    public GameController getGameController() {
        if (gameController == null)
            gameController = new GameController(this);
        return gameController;
    }

    public SessionController getSessionController() {
        if (sessionController == null) {
            sessionController = new SessionController(this);
        }
        return sessionController;
    }
    public WordController getWordController() {
        if (wordController== null) {
            wordController = new WordController(this);
        }
        return wordController;
    }
}
