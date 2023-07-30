package dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
	
	//location of the SQLite database
	private static final String SQCONN = "jdbc:sqlite:lib/glimpse.sqlite";
	
	//Connect to the database
	public static Connection getConnection() throws SQLException {
		
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection(SQCONN);
		}
		catch (ClassNotFoundException ex) {
			 ex.printStackTrace();
		}
		
		return null;
	}
	
}
