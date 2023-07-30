package application.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbUtil.dbConnection;

public class UserCredentials {
	
	/*	Connects to the database
	 * 	returns: password from database
	 * */
	public String getPasswordFromDatabase() {
	    String sqlQuery = "SELECT password FROM login";

	    String correctOldPassword = null;

	    try {
	        Connection conn = dbConnection.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sqlQuery);

	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            correctOldPassword = rs.getString("password");
	        }

	        rs.close();
	        stmt.close();
	        conn.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return correctOldPassword;
	}
	
	/*	Connects to the database
	 * 	Returns: security question from database
	 * */
	public String getSecQuesFromDatabase() {
	    String sqlQuery = "SELECT securityQuestion FROM login";

	    String secQuestion = null;

	    try {
	        Connection conn = dbConnection.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sqlQuery);

	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	        	secQuestion = rs.getString("securityQuestion");
	        }

	        rs.close();
	        stmt.close();
	        conn.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return secQuestion;
	}
	
	/*	Connects to the database
	 * 	Returns: security answer from database
	 * */
	public String getSecAnsFromDatabase() {
	    String sqlQuery = "SELECT securityAnswer FROM login";

	    String secAnswer = null;

	    try {
	        Connection conn = dbConnection.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sqlQuery);

	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	        	secAnswer = rs.getString("securityAnswer");
	        }

	        rs.close();
	        stmt.close();
	        conn.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return secAnswer;
	}
}
