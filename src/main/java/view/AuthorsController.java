package view;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import server.ResponseData;
import server.database.entities.author.Author;
import javafx.scene.control.ComboBox;

public class AuthorsController implements CustomController {

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
		}
		authorsList.setItems(authorNames);
		if(authorNames.size() != 0) {
			authorsList.setValue(authorNames.get(0));
		}	
	}

	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub

	}

}
