package application.changePW;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import dbUtil.dbConnection;
import application.user.UserCredentials;

public class ChangePWPageController implements Initializable {
	
	private dbConnection dc;
	private String sql = "SELCT * FROM login";
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
	}
	
	@FXML private TextField secQuesTF;
	@FXML private TextField secAnsTF;
	@FXML private PasswordField oldPassPF;
	@FXML private PasswordField newPassPF;
	@FXML private PasswordField confirmNewPassPF;
	@FXML private Button submitBtn;
	@FXML private Label errorMsgLbl;
	
	@FXML private void addNewQuesAndPassAction(ActionEvent event) {
		String sqlInsert = "INSERT INTO login(password,securityQuestion,securityAnswer) VALUES (?,?,?)";
		
		try {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlInsert);
			UserCredentials currentUser = new UserCredentials();
			
			String oldPassword = oldPassPF.getText();
	        String correctOldPassword = currentUser.getPasswordFromDatabase();
	        
	        if (oldPassword.equals(correctOldPassword) && newPassPF.getText().equals(confirmNewPassPF.getText())) {
	        	
	        	stmt.setString(1, confirmNewPassPF.getText());
	            stmt.setString(2, secQuesTF.getText());
	            stmt.setString(3, secAnsTF.getText());

	            stmt.executeUpdate();
	            stmt.close();
	            conn.close();
	        } else {
	        	errorMsgLbl.setText("Wrong password and/or new passwords don't match");
	        	stmt.close();
	            conn.close();
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
