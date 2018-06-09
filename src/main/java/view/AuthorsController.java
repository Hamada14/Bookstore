package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

public class AuthorsController implements CustomController {
	
	@FXML private TextField bookISBN;
	@FXML private TextField authorName;
	@FXML private ComboBox<String> authorsList;
	private ObservableList<String> authorNames ;
	
	@FXML
	private void addAuthor() {
		
	}
	
	@FXML
	private void deleteAuthor() {
		
	}
	
	@FXML
	private void back() {
		
	}
	
	@FXML
	private void loadAuthors() {
		
	}

	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub
		
	}

}
