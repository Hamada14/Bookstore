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
import server.database.entities.book.Book;
import server.errors.BookError;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class AuthorsController implements CustomController {

	private static final String SUCCESSFUL_TITLE = "Successful";
	private static final String FAIL_TITLE = "Error";
	
	private static final String DELETE_DONE_TEXT = "Deleted successfully";
	private static final String ADD_DONE_TEXT = "Added successfully";
	
	@FXML private Label fullName;
	
	@FXML
	private TextField bookISBN;
	@FXML
	private TextField authorName;
	@FXML
	private ComboBox<String> authorsList;

	private ObservableList<String> authorNames;
	
	private String usedISBN = "";


	@FXML
	private void addAuthor() {
		Author author = new Author(authorName.getText());
		ResponseData responseData = BookClient.getServer().addAuthor(BookStoreApp.getUser().getIdentity(), author, usedISBN);
		if(!responseData.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.ERROR, FAIL_TITLE, null, responseData.getError());
		} else {
			authorNames.add(author.getName());
			authorsList.setItems(authorNames);
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, ADD_DONE_TEXT);
		}
	}

	@FXML
	private void deleteAuthor() {
		String remove = authorsList.getValue();
		ResponseData rs = BookClient.getServer().deleteAuthorReference(BookStoreApp.getUser().getIdentity(), usedISBN, new Author(remove));
		if(rs.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, DELETE_DONE_TEXT);
			authorsList.getItems().remove(remove);
			authorNames.remove(remove);
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
		List<Author> authors = BookClient.getServer().getBookAuthors(BookStoreApp.getUser().getIdentity(), bookISBN.getText());
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
	
	@FXML
	private void goHome() {
		BookStoreApp.showCustomer(true);
	}

	@Override
	public void initData(Parameters parameters) {
		authorNames = FXCollections.observableArrayList();
		usedISBN = "";
        fullName.setText(BookStoreApp.getUser().getFullName());
	}
}
