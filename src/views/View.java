package views;

import controllers.*;
import resources.ResourceFactory;

public abstract class View {
    protected MainView parent;
    protected ControllerFactory controllerFactory;
    protected UserController userController;
    protected GameController gameController;
    protected CompetitionController competitionController;
    protected SessionController session;
    protected ResourceFactory resourceFactory;
    protected WordController wordController;

    public abstract void refresh();
    public abstract void clear();
    public abstract void constructor();

    protected void init(MainView parent) {
        controllerFactory = ControllerFactory.getInstance();
        userController = controllerFactory.GetUserController();
        gameController = controllerFactory.getGameController();
        competitionController = controllerFactory.getCompetitionController();
        wordController = controllerFactory.getWordController();
        session = controllerFactory.getSessionController();

        resourceFactory = new ResourceFactory();
        this.parent = parent;
    }
}