package database.access;

import database.Database;

abstract class DAO {
    protected Database database;

    public void close() {
        database.close();
    }

    public DAO() {
        this.database = Database.getInstance();
    }

    protected void printError(Exception e) {
        if(e instanceof NullPointerException)
            System.err.println(" ResultSet was null!");
        else
            e.printStackTrace();
    }
}
