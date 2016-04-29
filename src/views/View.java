package views;

import views.MainView;

abstract class View {
    protected MainView parent;

    public abstract void refresh();

    public void setParent(MainView parent) {
        this.parent = parent;
    }

}