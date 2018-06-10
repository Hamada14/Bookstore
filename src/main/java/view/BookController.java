package view;

import java.util.List;
import java.util.stream.Collectors;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

import server.database.entities.book.Book;
import server.database.entities.Order;
import server.database.entities.author.Author;
import server.database.entities.shoppingcart.ShoppingCart;

public class BookController implements CustomController {

	private static final String SUCCESSFUL_TITLE = "Successfully Added";
	private static final String ORDER_ADDED_MESSAGE = "Your order has been added";
	private static final String ERROR_IN_QUANTITY = "Must be Integer, change it to procced";
	private static final String INVALID_INPUT = "Invalid Input";
	

	@FXML private ComboBox<String> authorsComboName;
	
	private ObservableList<String> authorNames = FXCollections.observableArrayList();
	
	
	@FXML
	private Label fullName;
	
	@FXML
	private Label title;
	@FXML
	private Label category;
	@FXML
	private Label publisher;
	@FXML
	private Label author;
	@FXML
	private Label isbn;
	@FXML
	private Label publicationYear;
	@FXML
	private Label price;
	@FXML
	private TextField quantity;

	private Book selectedBook;

	@Override
	public void initData(Parameters parameters) {
		selectedBook = parameters.getBook();
		title.setText(selectedBook.getBookTitle());
		category.setText(selectedBook.getCategory());
		isbn.setText(selectedBook.getBookISBN());
		publicationYear.setText(selectedBook.getPublicationYear());
		price.setText(String.valueOf(selectedBook.getSellingPrice()));
		publisher.setText(selectedBook.getPublisher().getName());
		authorNames.clear();
		List<Author> authors = BookClient.getServer().getBookAuthors(BookStoreApp.getUser().getIdentity(), selectedBook.getBookISBN());
		if (authors != null) {
			authorNames = FXCollections
					.observableArrayList(authors.stream().map(Author::getName).collect(Collectors.toList()));
		}
		authorsComboName.setItems(authorNames);
		if(authorNames.size() != 0) {
			authorsComboName.setValue(authorNames.get(0));
		}	
        fullName.setText(BookStoreApp.getUser().getFullName());
	}
	

	@FXML
	private void goHome() {
		BookStoreApp.showCustomer(true);
	}

	@FXML
	private void addToCart() {
		String orderQuantity = quantity.getText();
		int result = 0;
		try {
			result = (int) Integer.valueOf(orderQuantity);
			ShoppingCart currentCart = BookStoreApp.getShoppingCart();
			currentCart.addOrder(new Order(result, selectedBook));
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, ORDER_ADDED_MESSAGE);
			quantity.setText("");
			BookStoreApp.showCustomer(true);
		} catch (Exception e) {
			e.printStackTrace();
			BookStoreApp.displayDialog(AlertType.ERROR, INVALID_INPUT, null, ERROR_IN_QUANTITY);
		}
	}
}
