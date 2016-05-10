package views;

import controllers.ControllerFactory;
import controllers.SessionController;

abstract class View {
    protected MainView parent;
    protected ControllerFactory controllerFactory;

    public abstract void refresh();
    public abstract void constructor();

    protected SessionController getSession() {
        return controllerFactory.getSessionController();
    }

    protected void init(MainView parent) {
        this.controllerFactory = parent.getControllerFactory();
        this.parent = parent;
        constructor();
    }
}