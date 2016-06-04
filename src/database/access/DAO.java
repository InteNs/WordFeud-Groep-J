package database.access;

import database.Database;

abstract class DAO {
    protected Database database;

    public void close() {
        //database.close();
    }

    public DAO() {
        this.database = Database.getInstance();
    }

    protected void printError(Exception e) {
        System.out.println(e.getClass());
        System.out.println(e.getMessage());
    }
}
