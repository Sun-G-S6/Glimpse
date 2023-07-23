package application.changePW;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class changePWPageController {
	
	private String pw = "p";
	
	@FXML TextField nameLbl;
	@FXML PasswordField pwField;
	@FXML Button loginBtn;
	@FXML Label loginStatusLbl;
	
	public void userLogin(ActionEvent e) throws IOException {
		checkLogin();
	}
	
	private void checkLogin() throws IOException {
		Main m = new Main();
		
		if(pw.equals("p") && pwField.getText().toString().equals(pw) ) {
			m.changeScene("changePWPage.fxml");
		}
		
		else if(pwField.getText().equals(pw)) {
			m.changeScene("homePage.fxml");
		}
		
		else if(pwField.getText().isEmpty()) {
			loginStatusLbl.setText("Please enter your password");
		}
		
		else {
			loginStatusLbl.setText("Wrong Password");
		}
	}
	
}
