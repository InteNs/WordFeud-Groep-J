package controllers;

import database.access.CompetitionDAO;
import database.access.GameDAO;
import database.access.UserDAO;
import database.access.WordDAO;

import java.util.Observable;

public abstract class Controller extends Observable {

    public UserController getUserController() {
        return controllerFactory.GetUserController();
    }

    public GameController getGameController() {
        return controllerFactory.getGameController();
    }

    public CompetitionController getCompetitionController() {
        return controllerFactory.getCompetitionController();
    }

    public SessionController getSession() {
        return controllerFactory.getSessionController();
    }
    public WordController getWordController(){return controllerFactory.getWordController();}

    protected ControllerFactory controllerFactory;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected CompetitionDAO competitionDAO;
    protected WordDAO wordDAO;

    public Controller(ControllerFactory factory) {
        this.controllerFactory = factory;
        competitionDAO = new CompetitionDAO();
        gameDAO = new GameDAO();
        userDAO = new UserDAO();
        wordDAO = new WordDAO();
    }


    public abstract void refresh();

    public abstract void refill();

    public abstract void fetch();

}
