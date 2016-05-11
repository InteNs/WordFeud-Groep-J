package views;

import controllers.*;

abstract class View {
    protected MainView parent;
    protected ControllerFactory controllerFactory;
    protected UserController userController;
    protected GameController gameController;
    protected CompetitionController competitionController;
    protected SessionController session;

    public abstract void refresh();
    public abstract void constructor();

    protected void init(MainView parent) {
        controllerFactory = parent.getControllerFactory();
        userController = controllerFactory.GetUserController();
        gameController = controllerFactory.getGameController();
        competitionController = controllerFactory.getCompetitionController();
        session = controllerFactory.getSessionController();

        this.parent = parent;
        constructor();
    }
}