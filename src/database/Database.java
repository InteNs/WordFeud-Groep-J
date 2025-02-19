package database;


import com.mysql.jdbc.CommunicationsException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class Database {

    private final String URL = "jdbc:mysql://databases.aii.avans.nl:3306/2016_soprj4_wordfeud";
    private final String USERNAME = "42IN04SOj";
    private final String PASSWORD = "justificatie";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private static Database database = new Database();

    public static Database getInstance() {
        if (database == null) database = new Database();
        return database;
    }

    public static void destroyInstance() {
        database = null;
    }

    private Connection connection;
    private PreparedStatement statement;

    public Database() {
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception e) {
            System.err.println("download connector --> https://dev.mysql.com/downloads/connector/j/");
            e.printStackTrace();
        }
    }

    /**
     * close the database connection, this will automatically
     * close any open statement or resultSet made with this connection
     */
    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException ignored) {
        }
    }

    /**
     * execute a count query with x amount of question marks
     *
     * @param Query  must have the count result in the first column
     * @param values values to insert into question marks
     * @return the result of the count
     */
    public int count(String Query, Object... values) {
        try {
            setStatement(Query, values);
            ResultSet records = statement.executeQuery();
            records.next();
            return records.getInt(1);
        } catch (SQLException e) {
            printError(e);
            return 0;
        }
    }

    /**
     * execute a delete query with x amount of question marks
     *
     * @param query  defined in static SQL class
     * @param values values to insert into question marks
     * @return false if the execution failed
     */
    public boolean delete(String query, Object... values) {
        return execute(query, values);
    }

    /**
     * execute a delete query with x amount of question marks
     *
     * @param query  defined in static SQL class
     * @param values values to insert into question marks
     * @return false if the execution failed
     */
    public boolean delete(String query, Collection<Object> values) {
        return execute(query, values.toArray());
    }

    /**
     * execute an insert query with x amount of question marks
     *
     * @param query  defined in static SQL class
     * @param values values to insert into question marks
     * @return false if the execution failed
     */
    public boolean insert(String query, Object... values) {
        return execute(query, values);
    }

    /**
     * execute an insert query with x amount of question marks
     *
     * @param query  defined in static SQL class
     * @param values values to insert into question marks
     * @return false if the execution failed
     */
    public boolean insert(String query, Collection<Object> values) {
        return execute(query, values.toArray());
    }

    /**
     * execute an update query with x amount of question marks
     *
     * @param query  defined in static SQL class
     * @param values values to insert into question marks
     * @return false if the execution failed
     */
    public boolean update(String query, Object... values) {
        return execute(query, values);
    }

    /**
     * execute an update query with x amount of question marks
     *
     * @param query  defined in static SQL class
     * @param values values to insert into question marks
     * @return false if the execution failed
     */
    public boolean update(String query, Collection<Object> values) {
        return execute(query, values.toArray());
    }

    /**
     * CALL close() method on database after this method
     * execute a select query with x mount of question marks
     *
     * @param query  defined in static SQL class
     * @param values to insert into question marks
     * @return the resultset
     */
    public ResultSet select(String query, Object... values) throws SQLException {
        setStatement(query, values);
        return statement.executeQuery();
    }

    /**
     * executes a select query and returns the first row as string array
     *
     * @param query  defined in static SQL class
     * @param values to insert into question marks
     * @return list with strings representing the first column
     */
    public ArrayList<String> selectFirstColumn(String query, Object... values) {
        ArrayList<String> result = new ArrayList<>();
        try {
            setStatement(query, values);
            ResultSet records = statement.executeQuery();
            while (records.next()) {
                result.add(records.getString(1));
            }
        } catch (SQLException e) {
            printError(e);
        }
        return result;
    }

    /**
     * executes a select query and returns the first row as string
     *
     * @param query  defined in static SQL class
     * @param values to insert into question marks
     * @return String of first row
     */
    public String selectFirstColumnRow(String query, Object... values) {
        ArrayList<String> column = selectFirstColumn(query, values);
        if (column.size() > 0)
            return column.get(0);
        return null;
    }

    private void printError(Exception e) {
        try {
            System.err.println(e.getMessage() + "\nquery: " + statement.toString().split(":")[1]);
        } catch (Exception e1) {
            if (e instanceof CommunicationsException) System.err.println("no connection to database!");
        }
    }

    private Connection connection() throws SQLException {
        if (connection != null && !connection.isClosed()) return connection;
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        return connection;
    }

    private void setStatement(String query, Object... values) throws SQLException {
        statement = connection().prepareStatement(query);
        for (int i = 0; i < values.length; i++)
            statement.setObject(i + 1, values[i]);
    }

    private boolean execute(String query, Object... values) {
        try {
            setStatement(query, values);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            printError(e);
            return false;
        }
    }
}