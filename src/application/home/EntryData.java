package application.home;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EntryData {

    private final StringProperty title;
    private long id; //variable to store the unique ID

    public EntryData(String titleIn) {
        this.title = new SimpleStringProperty(titleIn);
    }

    // Getter and setter for the title property
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String titleIn) {
        this.title.set(titleIn);
    }

    public StringProperty titleProperty() {
        return title;
    }

    // Getter and setter for the id property
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
