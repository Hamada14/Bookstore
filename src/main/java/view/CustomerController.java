package view;

import java.net.URL;


import java.util.List;
import java.util.ResourceBundle;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import server.BooksResponseData;
import server.database.entities.Book;
import server.database.entities.user.Identity;

public class CustomerController implements Initializable, CustomController {

	private static final String ERROR_MESSAGE_TITLE = "Error while searching";
	ObservableList<String> categoriesList = FXCollections.observableArrayList(Book.BOOK_CATEGORIES);
	@FXML
	private ChoiceBox<String> categories;
	@FXML
	private VBox booksLinks;
	@FXML
	private Button loadMore;
	@FXML
	private Label userName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		categories.setValue(Book.BOOK_CATEGORIES[0]);
		categories.setItems(categoriesList);
	}

	@FXML
	private void searchBooks() {
		Identity identity = BookStoreApp.getUser().getIdentity();
		BooksResponseData response = BookClient.getServer().searchBook(identity, "", "");
		if (response.isSuccessful()) {
//			System.out.println("in customer " + response.getBooks().size());
			viewBooks(response.getBooks());
		} else {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null, response.getError());
		}	
	}

	private void viewBooks(List<Book> books) {
		for (int i = 0; i < books.size(); i++) {
			BookHyperLink link = new BookHyperLink(books.get(i));
			link.setText(new String(i + ". " + books.get(i).getBookTitle()));
			link.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					BookStoreApp.showBookView(link.getBook());
					link.setVisited(false);
					System.out.println(link.getBook().getBookTitle());
				}
			});
			booksLinks.getChildren().add(link);
		}

		loadMore.setVisible(true);
	}

	@Override
	public void initData(Parameters parameters) {
		userName.setText(BookStoreApp.getUser().getIdentity().getUserName());
	}

	@FXML
	private void viewOrders() {
		BookStoreApp.showOrdersView();
	}

	@FXML
	private void logOut() {
		BookStoreApp.setUser(null);
		BookStoreApp.showLogin();
		BookStoreApp.getShoppingCart().clearCart();
	}

	@FXML
	private void goToManagerView() {
		BookStoreApp.showManager();
	}

	@FXML
	private void goToInformationForm() {
		BookStoreApp.showEditProfile();
	}
}
