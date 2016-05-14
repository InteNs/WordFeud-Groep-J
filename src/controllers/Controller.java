package controllers;

import database.access.CompetitionDAO;
import database.access.GameDAO;
import database.access.UserDAO;

public abstract class Controller {

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

    protected ControllerFactory controllerFactory;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected CompetitionDAO competitionDAO;

    public Controller(ControllerFactory factory) {
        this.controllerFactory = factory;
        competitionDAO = new CompetitionDAO();
        gameDAO = new GameDAO();
        userDAO = new UserDAO();
    }


    public abstract void refresh();
}
