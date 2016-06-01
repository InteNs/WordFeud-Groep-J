package database;

public class DatabaseFactory {
    private Database database;

    public Database getDatabase() {
        if (database == null)
            database = new Database();
        return database;
    }
}
