package view;

import client.alphabit.BookStoreApp;


import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import server.database.entities.book.Book;
import server.database.entities.Order;
import server.database.entities.shoppingcart.ShoppingCart;

public class BookController implements CustomController {

	private static final String SUCCESSFUL_TITLE = "Successfully Added";
	private static final String ORDER_ADDED_MESSAGE = "Your order has been added";
	private static final String ERROR_IN_QUANTITY = "Must be Integer, change it to procced";
	private static final String INVALID_INPUT = "Invalid Input";
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
	@FXML
	private Label userName;
	private Book selectedBook;

	@Override
	public void initData(Parameters parameters) {
		selectedBook = parameters.getBook();
		title.setText(selectedBook.getBookTitle());
		category.setText(selectedBook.getCategory());
		isbn.setText(selectedBook.getBookISBN());
		publicationYear.setText(selectedBook.getPublicationYear());
		price.setText(String.valueOf(selectedBook.getSellingPrice()));
		userName.setText(BookStoreApp.getUser().getIdentity().getUserName());
		publisher.setText(selectedBook.getPublisherName());
//		author.setText(selectedBook.getAuthors().get(0).getName());

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
