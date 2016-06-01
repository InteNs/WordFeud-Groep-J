package controllers;

import database.DatabaseFactory;

import java.util.Arrays;
import java.util.List;

public class ControllerFactory {

    private UserController userController;
    private GameController gameController;
    private CompetitionController competitionController;
    private SessionController sessionController;
    private WordController wordController;

    private DatabaseFactory databaseFactory;

    public ControllerFactory() {
        this.databaseFactory = new DatabaseFactory();
    }

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
            userController = new UserController(this, databaseFactory);
        return userController;
    }

    public CompetitionController getCompetitionController() {
        if(competitionController == null)
            competitionController = new CompetitionController(this, databaseFactory);
        return competitionController;
    }

    public GameController getGameController() {
        if (gameController == null)
            gameController = new GameController(this, databaseFactory);
        return gameController;
    }

    public SessionController getSessionController() {
        if (sessionController == null)
            sessionController = new SessionController(this, databaseFactory);
        return sessionController;
    }

    public WordController getWordController() {
        if (wordController== null)
            wordController = new WordController(this, databaseFactory);
        return wordController;
    }
}
