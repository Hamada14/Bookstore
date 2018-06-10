package view;

import java.util.List;
import java.util.stream.Collectors;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import server.ResponseData;
import server.database.entities.author.Author;
import javafx.scene.control.ComboBox;

public class AuthorsController implements CustomController {

	private static final String SUCCESSFUL_TITLE = "Successful";
	private static final String FAIL_TITLE = "Error";
	
	private static final String DELETE_DONE_TEXT = "Deleted successfully";
	
	@FXML
	private TextField bookISBN;
	@FXML
	private TextField authorName;
	@FXML
	private ComboBox<String> authorsList;

	private ObservableList<String> authorNames = FXCollections.observableArrayList();
	
	private String usedISBN = "";


	@FXML
	private void addAuthor() {
		Author author = new Author(authorName.getText());
		ResponseData responseData = BookClient.getServer().addAuthor(author, usedISBN);
		if(!responseData.isSuccessful()) {
			
		} else {
			authorNames.add(author.getName());
			authorsList.setItems(authorNames);
		}
	}

	@FXML
	private void deleteAuthor() {
		ResponseData rs = BookClient.getServer().deleteAuthorReference(usedISBN, new Author(authorsList.getValue()));
		if(rs.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, DELETE_DONE_TEXT);
			authorsList.getItems().remove(usedISBN);
			authorsList.setValue("");
		} else {
			BookStoreApp.displayDialog(AlertType.ERROR, FAIL_TITLE, null, rs.getError());
		}
	}

	@FXML
	private void back() {
		BookStoreApp.showManager();
	}

	@FXML
	private void loadAuthors() {
		authorNames.clear();
		List<Author> authors = BookClient.getServer().getBookAuthors(bookISBN.getText());
		if (authors != null) {
			authorNames = FXCollections
					.observableArrayList(authors.stream().map(Author::getName).collect(Collectors.toList()));
			usedISBN = bookISBN.getText();
		}
		authorsList.setItems(authorNames);
		if(authorNames.size() != 0) {
			authorsList.setValue(authorNames.get(0));
		}	
	}

	@Override
	public void initData(Parameters parameters) {
	}
}
