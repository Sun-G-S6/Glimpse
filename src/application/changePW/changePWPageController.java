package application.changePW;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import dbUtil.dbConnection;
import application.user.UserCredentials;

import application.login.LoginPageController;

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
	
	/*	Takes input for new security question, new security answer, old password, new password, new password confirmation
	 * 	Checks if the old passwords match, and if the new passwords match
	 * 	Stores new security question, new security answer, new password, new password confirmation and redirects to home page
	 * */
	@FXML private void addNewQuesAndPassAction(ActionEvent event) {
		String sqlUpdate = "UPDATE login SET password = ?, securityQuestion = ?, securityAnswer = ?";
		
		try {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
			UserCredentials currentUser = new UserCredentials();
			LoginPageController loginController = new LoginPageController();
			
			String oldPassword = oldPassPF.getText();
	        String correctOldPassword = currentUser.getPasswordFromDatabase();
	        
	        if (oldPassword.equals(correctOldPassword) && newPassPF.getText().equals(confirmNewPassPF.getText())) {
	        	
	        	stmt.setString(1, confirmNewPassPF.getText());
	            stmt.setString(2, secQuesTF.getText());
	            stmt.setString(3, secAnsTF.getText());

	        	errorMsgLbl.setText("Update Successfull");
	            stmt.executeUpdate();
	            stmt.close();
	            conn.close();
	            
	            Stage currentStage = (Stage) errorMsgLbl.getScene().getWindow();
	            currentStage.close();
	            
	            loginController.userLogin();
	            
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
