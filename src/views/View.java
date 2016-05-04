package views;

import controllers.CompetitionController;
import controllers.GameController;
import controllers.UserController;


abstract class View {
    protected MainView parent;

    /* declare your new Domain Controllers here*/
    protected static UserController userController;
    protected static CompetitionController competitionController;
    protected static GameController gameController;

    public abstract void refresh();
    public abstract void constructor();

    protected void init(MainView parent) {
        this.parent = parent;
        constructor();
    }
}