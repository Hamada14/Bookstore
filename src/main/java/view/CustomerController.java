package view;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

public class CustomerController implements Initializable{

	ObservableList<String> categoriesList = FXCollections.observableArrayList("All","Science","Art", "Geography", "Religion","History");
	@FXML private ChoiceBox<String> categories;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		categories.setValue("All");		
		categories.setItems(categoriesList);
	}
}
