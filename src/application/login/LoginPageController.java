package application.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.changePW.ChangePWPageController;
import application.home.HomePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import application.user.UserCredentials;


public class LoginPageController implements Initializable{
	
	LoginModel loginModel = new LoginModel();
	
	@FXML TextField nameLbl;
	@FXML PasswordField pwField;
	@FXML Button loginBtn;
	@FXML Label loginStatusLbl;
	@FXML Label dbStatusLbl;
	
	public void initialize(URL url, ResourceBundle rb) {
		if(this.loginModel.isDatabaseConnected()) {
			this.dbStatusLbl.setText("Connected to Database");
		}else {
			this.dbStatusLbl.setText("Not Connected to Database");
		}
	}
	
	@FXML public void loginAction(ActionEvent e ) {
		try {
			UserCredentials currentUser = new UserCredentials();
			
			if(!currentUser.getPasswordFromDatabase().equals("p") && this.loginModel.isLogin(this.pwField.getText())) {
				Stage stage = (Stage)this.loginBtn.getScene().getWindow();
				stage.close();
				userLogin();
			}
			else if (currentUser.getPasswordFromDatabase().equals("p") && this.loginModel.isLogin(this.pwField.getText()) ) {
				Stage stage = (Stage)this.loginBtn.getScene().getWindow();
				stage.close();
				firstTimeLogin();
			}
			else {
				this.loginStatusLbl.setText("Wrong Password");
			}
			
		}catch (Exception localException) {
			
		}
	}
	
	public void userLogin() {
		try {
			Stage userStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = (Pane)loader.load(getClass().getResource("/application/home/HomePage.fxml").openStream());
			HomePageController loginController = (HomePageController)loader.getController();
			
			Scene scene = new Scene(root);
			userStage.setScene(scene);
			userStage.setTitle("Home Page");
			userStage.setResizable(false);
			userStage.show();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void firstTimeLogin() {
		try {
			Stage userStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = (Pane)loader.load(getClass().getResource("/application/changePW/ChangePWPage.fxml").openStream());
			ChangePWPageController changePWController = (ChangePWPageController)loader.getController();
			
			Scene scene = new Scene(root);
			userStage.setScene(scene);
			userStage.setTitle("Change Password");
			userStage.setResizable(false);
			userStage.show();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
