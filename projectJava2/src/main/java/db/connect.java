package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
	public static Connection getConnection() {
		Connection c = null;

		try {
			String url = "jdbc:mysql://localhost:3306/projectjava2";
			String username = "root";
			String password = "";
			c = DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return c;
	}

	public static void closeConnection(Connection c) {

		try {
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}