package database.access;

import database.Database;

abstract class DAO {
    protected static Database database = new Database();

    protected static void printError(Exception e) {
        if(e instanceof NullPointerException)
            System.err.println(" ResultSet was null!");
        else
            e.printStackTrace();
    }
}
