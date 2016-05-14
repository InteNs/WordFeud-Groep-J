package database.access;

import database.Database;

abstract class DAO {
    protected Database database;

    public DAO() {
        database = new Database();
    }

    protected void printError(Exception e) {
        if(e instanceof NullPointerException)
            System.err.println(" ResultSet was null!");
        else
            e.printStackTrace();
    }
}
