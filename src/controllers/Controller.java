package controllers;

public abstract class Controller {

    protected ControllerFactory controllerFactory;

    public Controller(ControllerFactory factory) {
        this.controllerFactory = factory;
    }

    public abstract void refresh();

    protected SessionController getSession() {
        return controllerFactory.getSessionController();
    }
}
