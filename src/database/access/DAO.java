package database.access;

import controllers.ControllerFactory;
import database.Database;
import database.ShittyDatabaseException;

import java.sql.ResultSet;

abstract class DAO {
    protected Database database;
    protected ControllerFactory controllerFactory;

    public void close() {
        //database.close();
    }

    public DAO() {
        this.database = Database.getInstance();
        this.controllerFactory = ControllerFactory.getInstance();
    }


    protected boolean recordsAreNull(Exception e, ResultSet... records) {
        if (e instanceof NullPointerException) {
            for (ResultSet record : records)
                if (record == null) {
                    printError(new ShittyDatabaseException());
                    return true;
                }
        }
        e.printStackTrace();
        return false;
    }

    protected void printError(Exception e) {
        controllerFactory.getFeedbackController().showError(e);
        e.printStackTrace();
    }
}
