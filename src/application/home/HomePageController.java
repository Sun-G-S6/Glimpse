package application.home;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.login.LoginPageController;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HomePageController implements Initializable{
	
	@FXML private Button changePWBtn;
	@FXML private Button logoutBtn;
	@FXML private Button saveBtn;
	@FXML private Button loadEntriesBtn;
	@FXML private TextField newTitleTF;
	@FXML private TextArea newEntryTA;
	@FXML private DatePicker newDateDP;
	@FXML private TableView<EntryData> entryTable;
	@FXML private TableColumn<EntryData, String> entryCol;
	
	private dbConnection dc;
	private ObservableList<EntryData> data;
	
	private String sqlLoad = "SELECT * FROM journalEntries";
	
	public void initialize (URL url, ResourceBundle  rb) {
		this.dc = new dbConnection();
	}
	
	@FXML public void loadJournalEntries(ActionEvent e) throws SQLException {
		try {
			Connection conn = dbConnection.getConnection();
			this.data = FXCollections.observableArrayList();
			
			ResultSet rs = conn.createStatement().executeQuery(sqlLoad);
			while (rs.next()) {
				this.data.add(new EntryData(rs.getString(4)));
				}
			} catch (SQLException ex) {
				System.err.println("Error " + ex);
			}
		
		this.entryCol.setCellValueFactory(new PropertyValueFactory<EntryData, String>("title"));
		
		this.entryTable.setItems(null);
		this.entryTable.setItems(this.data);
		
		}
	
	
	@FXML public void changePWAction(ActionEvent e ) {
		try {
				LoginPageController loginController = new LoginPageController();
				Stage stage = (Stage)this.changePWBtn.getScene().getWindow();
				stage.close();
				loginController.changePW();
			
		}catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	@FXML public void logoutAction(ActionEvent e) {
		try {
			
					Stage userStage = new Stage();
					FXMLLoader loader = new FXMLLoader();
					Pane root = (Pane)loader.load(getClass().getResource("/application/login/LoginPage.fxml").openStream());
					LoginPageController loginController = (LoginPageController)loader.getController();
					
					Scene scene = new Scene(root);
					userStage.setScene(scene);
					userStage.setTitle("Glimpse");
					userStage.setResizable(false);
					userStage.show();
				
		}catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	@FXML private void addEntryAction(ActionEvent e) {
		String sqlInsert = "INSERT INTO journalEntries(year, month, day, title, entry) VALUES (?,?,?,?,?)";
		
		try {
			Connection conn = dbConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sqlInsert);
			
			stmt.setInt(1, this.newDateDP.getValue().getYear());
			stmt.setInt(2, this.newDateDP.getValue().getMonthValue());
			stmt.setInt(3, this.newDateDP.getValue().getDayOfMonth());
			stmt.setString(4, this.newTitleTF.getText());
			stmt.setString(5, this.newEntryTA.getText());
			
			stmt.execute();
			stmt.close();
			conn.close();
			clearFields();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void clearFields() {
		this.newDateDP.setValue(null);
		this.newEntryTA.setText("");
		this.newTitleTF.setText("");
	}
	
}
