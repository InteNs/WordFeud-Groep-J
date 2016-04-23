package wordfeud.statics;

import java.net.DatagramSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.WebRowSet;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.sun.corba.se.impl.util.Version;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import javafx.scene.chart.PieChart.Data;

public final class Database {

	static private Connection con = null;
	static private Statement st = null;
	static private ResultSet rs = null;

	static private String url = "jdbc:mysql://77.172.146.212:3306/wordfeud";
	static private String user = "wordfeud";
	static private String password = "wordfeud01";

	public Database() {

	}

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

	public static ArrayList<String> query(String query) throws SQLException {
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			st = connect();
			rs = st.executeQuery(query);
			while (rs.next()) {
				resultList.add(rs.getString(1));
			}
			rs.close();
			return resultList;
		} catch (SQLException e) {
			rs.close();
			System.out.println(e.getMessage());
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

	public static ArrayList<String> query(String query, String column) throws SQLException {
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			st = connect();
			rs = st.executeQuery(query);
			while (rs.next()) {
				resultList.add(rs.getString(column));
			}
			rs.close();
			return resultList;
		} catch (SQLException e) {
			rs.close();
			System.out.println(e.getMessage());
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

	public static String readColumn(String query) {
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

}
