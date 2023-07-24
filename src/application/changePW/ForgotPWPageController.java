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
import javafx.stage.Stage;
import dbUtil.dbConnection;
import application.login.LoginPageController;
import application.user.UserCredentials;

public class ForgotPWPageController implements Initializable {
	
	@FXML private Label secQuesLbl;
	@FXML private TextField secAnsTF;
	@FXML private PasswordField oldPassPF;
	@FXML private PasswordField newPassPF;
	@FXML private PasswordField confirmNewPassPF;
	@FXML private Button submitBtn;
	@FXML private Label errorMsgLbl;
	
	private dbConnection dc;
	private String sql = "SELCT * FROM login";
	
	UserCredentials currentUser = new UserCredentials();
	LoginPageController loginController = new LoginPageController();
	
	public void initialize(URL url, ResourceBundle rb) {
		this.dc = new dbConnection();
        secQuesLbl.setText(currentUser.getSecQuesFromDatabase());
	}
	
	
	@FXML private void submitForgotAction(ActionEvent event) {
		String sqlUpdate = "UPDATE login SET password = ?";
		
		try {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
			
			String inputAns = secAnsTF.getText();
	        String correctSecAns = currentUser.getSecAnsFromDatabase();
	        
	        if (inputAns.equals(correctSecAns) && newPassPF.getText().equals(confirmNewPassPF.getText())) {
	        	
	        	stmt.setString(1, confirmNewPassPF.getText());

	        	errorMsgLbl.setText("Update Successfull");
	            stmt.executeUpdate();
	            stmt.close();
	            conn.close();
	            
	            Stage currentStage = (Stage) errorMsgLbl.getScene().getWindow();
	            currentStage.close();
	            
	            loginController.userLogin();
	            
	        } else {
	        	errorMsgLbl.setText("Wrong answer and/or new passwords don't match");
	        	stmt.close();
	            conn.close();
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
