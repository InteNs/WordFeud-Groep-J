package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	private Connection con;
	private Statement st;
	private ResultSet rs;

	private String url = "jdbc:mysql://77.172.146.212:3306/wordfeud";
	private String user = "wordfeud";
	private String password = "wordfeud01";
	private String driver = "com.mysql.jdbc.Driver";

	private Connection connection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public Database() {
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a ResultSet of a table with conditions
	 * 
	 * @param table
	 *            table you want to be returned
	 * @param conditions
	 *            the mysql conditions (e.g WHERE id =...)
	 * @return
	 */
	public ResultSet select(String table, String conditions) {
		// Do not forget to call the close methode after using this methode!
		try {
			st = connection().createStatement();
			return st.executeQuery("SELECT * FROM " + table + " WHERE " + conditions);
		} catch (SQLException e) {
			e.printStackTrace();
			close();
			return null;
		}
	}

	/**
	 * Returns a ResultSet of a table
	 * 
	 * @param table
	 *            table you want to be returned
	 * @return
	 */
	public ResultSet select(String table) {
		// Do not forget to call the close methode after using this methode!
		try {
			st = connection().createStatement();
			return st.executeQuery("SELECT * FROM " + table);
		} catch (SQLException e) {
			e.printStackTrace();
			close();
			return null;
		}
	}

	/**
	 * Returns a list of a single column with conditions
	 * 
	 * @param table
	 *            the name of the table in the database
	 * @param column
	 *            the column you want to select
	 * @param condition
	 *            the mysql conditions (e.g WHERE id =...)
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> selectColumn(String table, String column, String condition) throws SQLException {
		ResultSet rs = select(table);
		ArrayList<String> list = new ArrayList<String>();
		st = connection().createStatement();
		rs = st.executeQuery("SELECT " + column + " FROM " + table + " WHERE " + condition);
		while (rs.next()) {
			list.add(rs.getString(1));
		}
		close();
		return list;
	}

	/**
	 * Returns a list of a single column
	 * 
	 * @param table
	 *            the name of the table in the database
	 * @param column
	 *            the column you want to select
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> selectColumn(String table, String column) throws SQLException {
		System.out.println("SELECT " + column + " FROM " + "table ");
		ResultSet rs = select(table);
		ArrayList<String> list = new ArrayList<String>();
		st = connection().createStatement();
		rs = st.executeQuery("SELECT " + column + " FROM " + table);
		while (rs.next()) {
			list.add(rs.getString(1));
		}
		close();
		return list;
	}

	/**
	 * Inserts record in to database
	 * 
	 * @param table
	 *            table name
	 * @param values
	 *            insert values (e.g name='foo',password='123')
	 * @return
	 */
	public boolean insert(String table, String values) {
		try {
			st = connection().createStatement();
			st.execute("INSERT INTO " + table + " VALUES (" + values + ")");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			close();
			return false;
		}
	}

	/**
	 * Updates a record
	 * 
	 * @param table
	 *            table to be updated
	 * @param columnValues
	 *            column(s) to be updated and the values
	 * @param conditions
	 *            the mysql conditions (e.g WHERE id =...)
	 * @return
	 */
	public boolean update(String table, String columnValues, String conditions) {
		try {
			st = connection().createStatement();
			st.executeUpdate("UPDATE " + table + " SET " + columnValues + " WHERE " + conditions);
			close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			close();
			return false;
		}

	}

	/**
	 * Deletes a record
	 * 
	 * @param table
	 *            table name
	 * @param conditions
	 *            the mysql conditions (e.g WHERE id =...)
	 * @return
	 */
	public boolean delete(String table, String conditions) {
		try {
			st = connection().createStatement();
			st.execute("DELETE FROM " + table + " WHERE " + conditions);
			close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			close();
			return false;
		}
	}

	/**
	 * returns a ResultSet
	 * 
	 * @param query
	 *            complete mysql query
	 * @return
	 */
	public ResultSet query(String query) {
		// Do not forget to call the close methode after using this methode!
		try {
			st = connection().createStatement();
			return st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			close();
			return null;
		}
	}

	/**
	 * Closes all connections to the database
	 */
	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

}