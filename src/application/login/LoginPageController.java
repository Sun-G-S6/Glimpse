package application.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import application.user.UserCredentials;
import application.view.ViewController;
import application.changePW.ChangePWPageController;
import application.home.HomePageController;
import application.changePW.ForgotPWPageController;

public class LoginPageController implements Initializable{
	
	LoginModel loginModel = new LoginModel();
	
	@FXML PasswordField pwField;
	@FXML Button loginBtn;
	@FXML Label loginStatusLbl;
	@FXML Label dbStatusLbl;
	
	/*	Initializes the login page by connecting to the database and displays the connection status
	 * 	Listener event to execute login function if "Enter" key is pressed in the PasswordField
	 * */
	public void initialize(URL url, ResourceBundle rb) {
		if(this.loginModel.isDatabaseConnected()) {
			this.dbStatusLbl.setText("Connected to Database");
		}else {
			this.dbStatusLbl.setText("Not Connected to Database");
		}
		pwField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginAction(null);
            }
        });
	}
	
	/*	Closes stage runs the changing password function if the password from the database is 'p' (default password)
	 * 	OR
	 * 	Closes stage, runs the login function if the password from the database is not a 'p'
	 * */
	@FXML public void loginAction(ActionEvent e ) {
		try {
			UserCredentials currentUser = new UserCredentials();
			
			if(!currentUser.getPasswordFromDatabase().equals("p") && this.loginModel.isLogin(this.pwField.getText())) {
				ViewController view = new ViewController();
				view.closeWindow((Stage)this.loginBtn.getScene().getWindow());
				userLogin();
			}
			else if (currentUser.getPasswordFromDatabase().equals("p") && this.loginModel.isLogin(this.pwField.getText()) ) {
				ViewController view = new ViewController();
				view.closeWindow((Stage)this.loginBtn.getScene().getWindow());
				changePW();
			}
			else {
				this.loginStatusLbl.setText("Wrong Password");
			}
		}catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	/*	Closes the stage and runs the forgot password function
	 * */
	@FXML public void forgotAction(MouseEvent e ) {
		try {
			ViewController view = new ViewController();
			view.closeWindow((Stage)this.loginBtn.getScene().getWindow());
			forgotPW();
		}catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	// Login function that shows the home page
	public void userLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = (Pane)loader.load(getClass().getResource("/application/home/HomePage.fxml").openStream());
			ViewController view = new ViewController();
			view.showNewStage(root);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Changing password function that goes to the changing password page
	public void changePW() {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = (Pane)loader.load(getClass().getResource("/application/changePW/ChangePWPage.fxml").openStream());
			ViewController view = new ViewController();
			view.showNewStage(root);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Forgot password function that goes to the forgot password page
	public void forgotPW() {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = (Pane)loader.load(getClass().getResource("/application/changePW/ForgotPWPage.fxml").openStream());
			ViewController view = new ViewController();
			view.showNewStage(root);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*	Closes the window and redirects to the Login page
	 * */
	public void logoutAction() {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = (Pane)loader.load(getClass().getResource("/application/login/LoginPage.fxml").openStream());
			ViewController view = new ViewController();
			view.showNewStage(root);
		}catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
}
