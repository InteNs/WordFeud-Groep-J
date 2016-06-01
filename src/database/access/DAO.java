package database.access;

import database.Database;
import database.DatabaseFactory;

abstract class DAO {
    protected Database database;

    public void close() {
        database.close();
    }

    public DAO(DatabaseFactory databaseFactory) {
        this.database = databaseFactory.getDatabase();
    }

    protected void printError(Exception e) {
        if(e instanceof NullPointerException)
            System.err.println(" ResultSet was null!");
        else
            e.printStackTrace();
    }
}
