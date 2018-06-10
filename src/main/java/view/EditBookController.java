package view;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import server.ResponseData;
import server.database.entities.book.Book;
import server.database.entities.book.BookBuilder;
import server.database.entities.publisher.Publisher;

public class EditBookController implements CustomController {

	private static final String ERROR_MESSAGE_TITLE = "Error!";
	private static final String ERROR_MESSAGE_HEADER = "Couldn't edit";
	private static final String INVALID_PRICE = "Invalid value for price";
	
	@FXML private Label fullName;
	
	@FXML private Label isbnLabel;
	@FXML private TextField title;
	@FXML private TextField publisherName;
	@FXML private TextField publicationYear;
	@FXML private ComboBox<String> category;
	@FXML private TextField quantity;
	@FXML private TextField price;
	@FXML private TextField minimumThreshold;
	
	private static final ObservableList<String> categoriesList = FXCollections
			.observableArrayList(BookClient.getServer().getCategories(BookStoreApp.getUser().getIdentity()));
	
	@FXML
	private void confirmBookEdit() {
		try {
			BookBuilder bookBuilder = new BookBuilder();
			float p = Float.valueOf(price.getText());
			int minimumQuantity = Integer.valueOf(minimumThreshold.getText());
			bookBuilder.setBookISBN(isbnLabel.getText());
			bookBuilder.setBookTitle(title.getText());
			bookBuilder.setCategory(category.getValue());
			bookBuilder.setPublisher(new Publisher(publisherName.getText()));
			bookBuilder.setPublicationYear(publicationYear.getText());
			bookBuilder.setSellingPrice(p);
			bookBuilder.setMinimumThreshold(minimumQuantity);
			ResponseData bookEdit = BookClient.getServer().editBook(BookStoreApp.getUser().getIdentity(), bookBuilder);
			if(bookEdit.isSuccessful()) {
				BookStoreApp.showManager();
			} else {
				BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, bookEdit.getError());

			}
		} catch(NumberFormatException e) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, INVALID_PRICE);
		}
	}
	
	
	@FXML 
	private void goHome() {
		BookStoreApp.showCustomer(true);
	}
	
	@FXML 
	private void backToManagerView() {
		BookStoreApp.showManager();
	}
	
	@Override
	public void initData(Parameters parameters) {
		Book book = parameters.getBook();
		isbnLabel.setText(book.getBookISBN());
		title.setText(book.getBookTitle());
		publisherName.setText(book.getPublisher().getName());
		publicationYear.setText(book.getPublicationYear());
		category.setItems(categoriesList);
		category.setValue(categoriesList.get(0));
		quantity.setText(Integer.toString(book.getQuantity()));
		price.setText(Float.toString(book.getSellingPrice()));
		minimumThreshold.setText(Integer.toString(book.getMinimumThreshold()));
        fullName.setText(BookStoreApp.getUser().getFullName());
	}
}
