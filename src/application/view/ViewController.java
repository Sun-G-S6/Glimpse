package application.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewController {
	
	public void closeWindow(Stage stg) {
		stg.close();
	}
	
	public void showNewStage(Pane newStage) {
		Scene scene = new Scene(newStage);
		Stage userStage = new Stage();
		userStage.setScene(scene);
		userStage.setTitle("Glimpse");
		userStage.setResizable(false);
		userStage.show();
	}

}
