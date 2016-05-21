package controllers;

import java.util.Arrays;
import java.util.List;

public class ControllerFactory {

    private UserController userController;
    private GameController gameController;
    private CompetitionController competitionController;
    private SessionController sessionController;

    public List<Controller> getControllers() {
        return Arrays.asList(
                GetUserController(),
                getGameController(),
                getCompetitionController(),
                getSessionController()
        );
    }

    public void resetControllers() {
        userController = null;
        gameController = null;
        competitionController = null;
        sessionController = null;
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
}
