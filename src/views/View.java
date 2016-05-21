package views;

import controllers.*;
import resources.ResourceFactory;

abstract class View {
    protected MainView parent;
    protected ControllerFactory controllerFactory;
    protected UserController userController;
    protected GameController gameController;
    protected CompetitionController competitionController;
    protected SessionController session;
    protected ResourceFactory resourceFactory;

    public abstract void refresh();
    public abstract void constructor();

    protected void init(MainView parent) {
        controllerFactory = parent.getControllerFactory();
        userController = controllerFactory.GetUserController();
        gameController = controllerFactory.getGameController();
        competitionController = controllerFactory.getCompetitionController();
        session = controllerFactory.getSessionController();
        resourceFactory = new ResourceFactory();
        this.parent = parent;
    }
}