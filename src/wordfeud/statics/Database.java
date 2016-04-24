package wordfeud.statics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public final class Database {

	static private Connection con = null;
	static private Statement st = null;
	static private ResultSet rs = null;

	static private String url = "jdbc:mysql://77.172.146.212:3306/wordfeud";
	static private String user = "wordfeud";
	static private String password = "wordfeud01";

	public static Statement connect() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			return st;
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	public static ResultSet select(String query, ResultSet result) throws SQLException {
		/* Do not forget to call the close methode after using this methode! */
		try {
			st = connect();
			result = st.executeQuery(query);
			return result;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
			return result;
		}
	}

	public static ArrayList<String> select(String query,ArrayList<String> list) throws SQLException {
		try {
			st = connect();
			rs = st.executeQuery(query);
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			close();
			return list;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
			return null;
		} finally {
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

	public static String selectColumn(String query) {
		try {
			st = connect();
			rs = st.executeQuery(query);
			if (rs.next()) {
				return rs.getString(1);
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static void close() {
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
