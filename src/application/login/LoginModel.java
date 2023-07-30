package application.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbUtil.dbConnection;

public class LoginModel {
	
	Connection connection;
	
	// Connects to the database
	public LoginModel() {
		try {
			this.connection = dbConnection.getConnection();
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		if(this.connection == null) {
			System.exit(1);
		}
	}
	
	// Return true if there is a database connection
	public boolean isDatabaseConnected() {
		return this.connection != null;
	}
	
	/* Compares the inputed password with the database password
	 * returns true if it matches
	 * returns false if it doesn't
	 * */
	public boolean isLogin(String pass) throws Exception {
		PreparedStatement pr = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM login where password = ?";
		
		try {
			pr = this.connection.prepareStatement(sql);
			pr.setString(1, pass);
			
			rs = pr.executeQuery();
						
			if (rs.next()) {
				return true;
			}
			return false;
		}
		catch (SQLException ex) {
			return false;
		}
		
		finally {
			pr.close();
			rs.close();
		}
		
	}

}
