package views;

import views.MainView;

/**
 * Created by markhavekes on 29/04/16.
 */
abstract class View {
    protected MainView parent;

    public abstract void refresh();

    public void setParent(MainView parent) {
        this.parent = parent;
    }

}