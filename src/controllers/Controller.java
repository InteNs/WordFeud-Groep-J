package controllers;

public abstract class Controller {

    public abstract void refresh();

    protected ControllerFactory controllerFactory;

    protected SessionController getSession() {
        return controllerFactory.getSessionController();
    }

    public Controller(ControllerFactory factory) {
        this.controllerFactory = factory;
        refresh();
    }
}
