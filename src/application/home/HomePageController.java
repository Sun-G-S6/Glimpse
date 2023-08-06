package application.home;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import application.login.LoginPageController;
import application.view.ViewController;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class HomePageController implements Initializable{
	
	@FXML private Button changePWBtn;
	@FXML private Button logoutBtn;
	@FXML private Button saveBtn;
	@FXML private Button loadEntriesBtn;
	@FXML private TextField newTitleTF;
	@FXML private TextArea newEntryTA;
	@FXML private TableView<EntryData> entryTable;
	@FXML private TableColumn<EntryData, String> entryCol;
	@FXML private TextField searchTF;
	@FXML private JFXDatePicker newDateDP;
	@FXML private JFXTimePicker newTimeTP;
	@FXML private Button deleteBtn;
	@FXML private Button newEntryBtn;
	
	private dbConnection dc;
	private ObservableList<EntryData> data;
	private long selectedEntryId;
	private String sqlLoad = "SELECT * FROM journalEntries";
	
	/*	Initializes the home page by connection to the database, and loading the journal entries
	 * 	search the database if "Enter" key is pressed in search textField
	 * 	search the database if a cell in the entry table on the left side is clicked
	 * 	get the uniqueID of the entry when searched
	 * */
	public void initialize (URL url, ResourceBundle  rb) {
		this.dc = new dbConnection();
		 try {
		        loadJournalEntries(null);
		        
		        searchTF.setOnKeyPressed(event -> {
		            if (event.getCode() == KeyCode.ENTER) {
		                searchJournalEntry();
		            }
		        });
		        
		        entryCol.setCellFactory(tc -> {
		            TableCell<EntryData, String> cell = new TableCell<EntryData, String>() {
		                @Override
		                protected void updateItem(String item, boolean empty) {
		                    super.updateItem(item, empty);
		                    setText(empty ? null : item);
		                }
		            };

		            cell.setOnMouseClicked(e -> {
		                if (!cell.isEmpty()) {
		                    String selectedTitle = cell.getItem();
		                    searchTF.setText(selectedTitle);
		                    searchJournalEntry();
		                }
		            });

		            return cell;
		        });
		        
		        entryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
		            if (newValue != null) {
		                selectedEntryId = newValue.getId(); // Store the selected entry's ID in the selectedEntryId variable
		            }
		        });
		        
		    } catch (SQLException e) {
		        System.err.println("Error loading journal entries: " + e);
		    }
	}
	/*	Loads the journal entry by the uniqueID
	 * 	Creates a new cell on the table to the left with each entry
	 * 	Each cell is labeled after the journal's title
	 * 	Each cell stores their journal's uniqueID
	 * */
	@FXML private void loadJournalEntries(ActionEvent e) throws SQLException {
	    try {
	        Connection conn = dbConnection.getConnection();
	        this.data = FXCollections.observableArrayList();

	        ResultSet rs = conn.createStatement().executeQuery(sqlLoad);
	        while (rs.next()) {
	            EntryData entryData = new EntryData(rs.getString("title"));
	            entryData.setId(rs.getLong("id"));
	            this.data.add(entryData);
	        }
	    } catch (SQLException ex) {
	        System.err.println("Error " + ex);
	    }

	    this.entryCol.setCellValueFactory(new PropertyValueFactory<>("title"));

	    this.entryTable.setItems(null);
	    this.entryTable.setItems(this.data);
	}

	/*	Searches the database for the journal entry that matches the search TextField the best
	 * 	Shows the data in the entry fields
	 * */
	@FXML private void searchJournalEntry() {
	    String searchText = searchTF.getText().trim();

	    if (!searchText.isEmpty()) {
	        try {
	            Connection conn = dbConnection.getConnection();

	            String sqlSearch = "SELECT * FROM journalEntries WHERE title LIKE ?";
	            PreparedStatement preparedStatement = conn.prepareStatement(sqlSearch);
	            preparedStatement.setString(1, "%" + searchText + "%");

	            ResultSet rs = preparedStatement.executeQuery();

	            if (rs.next()) {
	                String title = rs.getString("title");
	                String entry = rs.getString("entry");
	                
	                // Retrieve the Unix Timestamp and convert it to LocalDateTime
	                long unixTimestamp = rs.getLong("dateTime");
	                LocalDateTime entryDateTime = LocalDateTime.ofEpochSecond(unixTimestamp, 0, ZoneOffset.UTC);
	                
	                newTitleTF.setText(title);
	                newEntryTA.setText(entry);
	                newDateDP.setValue(entryDateTime.toLocalDate());
	                newTimeTP.setValue(entryDateTime.toLocalTime());
	            } else {
	                clearFields();
	            }

	            preparedStatement.close();
	            conn.close();
	        } catch (SQLException ex) {
	            System.err.println("Error searching for journal entries: " + ex);
	        }
	    } else {
	        clearFields();
	    }
	}

	/*	closes the window and redirects to the changing password & security question page
	 * */
	@FXML public void changePWAction(ActionEvent e ) {
		try {
				ViewController view = new ViewController();
				view.closeWindow((Stage)this.changePWBtn.getScene().getWindow());
				LoginPageController loginController = new LoginPageController();
				loginController.changePW();
			
		}catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	/*	Closes the window and redirects to the Login page
	 * */
	@FXML public void logoutAction(ActionEvent e) {
		try {
			ViewController view = new ViewController();
			view.closeWindow((Stage)this.logoutBtn.getScene().getWindow());
			LoginPageController loginController = new LoginPageController();
			loginController.logoutAction();
			
		}catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	/*	uniqueID = selectedEntryID
	 * 	Adds new entry if there's no uniqueID(selectedEntryID) 
	 * 	Edits existing entry if there's a uniqueID
	 * */
	@FXML private void addEntryAction(ActionEvent e) {
	    String sqlInsert = "INSERT INTO journalEntries(dateTime, title, entry) VALUES (?,?,?)";
	    String sqlUpdate = "UPDATE journalEntries SET dateTime=?, title=?, entry=? WHERE id=?";

	    try {
	        Connection conn = dbConnection.getConnection();
	        PreparedStatement stmt;

	        LocalDateTime entryDateTime = LocalDateTime.of(newDateDP.getValue(), newTimeTP.getValue());	
	        long unixTimestamp = entryDateTime.toEpochSecond(ZoneOffset.UTC);

	        if (selectedEntryId == 0) {
	            // If selectedEntryId is 0, it means we're inserting a new entry
	            stmt = conn.prepareStatement(sqlInsert);
	            stmt.setLong(1, unixTimestamp);
	            stmt.setString(2, this.newTitleTF.getText());
	            stmt.setString(3, this.newEntryTA.getText());
	        } else {
	            // Otherwise, we're updating an existing entry
	            stmt = conn.prepareStatement(sqlUpdate);
	            stmt.setLong(1, unixTimestamp);
	            stmt.setString(2, this.newTitleTF.getText());
	            stmt.setString(3, this.newEntryTA.getText());
	            stmt.setLong(4, selectedEntryId);
	        }

	        stmt.execute();
	        stmt.close();
	        conn.close();
	        clearFields();
	        loadJournalEntries(null);

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	// assigns 0 to selectedEntryID so it can be registered as a new entry when adding
	@FXML private void addNewEntryBtn(ActionEvent e) {
		selectedEntryId = 0;
		clearFields();
		addEntryAction(null);
	}
	
	/*	uniqueID = selectedEntryID	
	 * 	Takes the uniqueID of the current journal entry,
	 * 	Searches the database for the uniqueID and deletes it
	 * 	Otherwise it does nothing
	 * */
	@FXML private void deleteEntry(ActionEvent e) {
	    String sqlDelete = "DELETE FROM journalEntries WHERE id=?";
	    long entryId = selectedEntryId;

	    try {
	        Connection conn = dbConnection.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sqlDelete);
	        stmt.setLong(1, entryId);
	        stmt.execute();
	        stmt.close();
	        conn.close();
	        loadJournalEntries(null); // Refresh the entryTable after deletion
	        clearFields();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	/*	Clears the input fields for the journal entry
	 * */
	private void clearFields() {
		this.newDateDP.setValue(null);
		this.newTimeTP.setValue(null);
		this.newEntryTA.setText("");
		this.newTitleTF.setText("");
	}
	
}
