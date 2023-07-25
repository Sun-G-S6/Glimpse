package application.home;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EntryData {
	
	private final StringProperty title;
	
	public EntryData(String titleIn) {
		this.title = new SimpleStringProperty(titleIn);
	}
	
	public String getTitle() {
		return title.get();
	}
	
	public StringProperty titleProperty() {
		return title;
	}
	
	public void setTitle(String titleIn) {
		this.title.set(titleIn);
	}
	
}
