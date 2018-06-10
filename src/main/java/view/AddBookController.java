package view;

import client.BookClient;

import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import server.ResponseData;
import server.database.entities.book.BookBuilder;
import server.database.entities.publisher.Publisher;
import server.errors.BookError;

public class AddBookController implements CustomController {

	private static final String ERROR_MESSAGE_TITLE = "Error!";
	private static final String ERROR_MESSAGE_HEADER = "Couldn't edit";
	
	private static final String SUCCESS_MESSAGE_TITLE = "Success!";
	private static final String BOOK_ADDED_SUCCESSFULY = "Book added successfully";
	
	@FXML private Label fullName;
	
	@FXML private TextField title;
	@FXML private TextField publisherName;
	@FXML private TextField isbn;
	@FXML private TextField publicationYear;
	@FXML private ComboBox<String> category;
	@FXML private TextField quantity;
	@FXML private TextField price;
	@FXML private TextField minimumThreshold;
	
	private static final ObservableList<String> categoriesList = FXCollections
			.observableArrayList(BookClient.getServer().getCategories(BookStoreApp.getUser().getIdentity()));
	
	@FXML
	private void confirmBookAddition() {
		try {
			BookBuilder bookBuilder = new BookBuilder();
			float p = Float.valueOf(price.getText());
			int minimumQuantity = Integer.valueOf(minimumThreshold.getText());
			int q = Integer.valueOf(quantity.getText());
			bookBuilder.setBookISBN(isbn.getText());
			bookBuilder.setBookTitle(title.getText());
			bookBuilder.setCategory(category.getValue());
			bookBuilder.setPublicationYear(publicationYear.getText());
			bookBuilder.setSellingPrice(p);
			bookBuilder.setQuantity(q);
			bookBuilder.setMinimumThreshold(minimumQuantity);
			Publisher publisher = new Publisher(publisherName.getText());
			bookBuilder.setPublisher(publisher);
			ResponseData bookAdded = BookClient.getServer().addNewBook(BookStoreApp.getUser().getIdentity(), bookBuilder);
			if (bookAdded.isSuccessful()) {
				BookStoreApp.showManager();
				BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESS_MESSAGE_TITLE, SUCCESS_MESSAGE_TITLE,
						BOOK_ADDED_SUCCESSFULY);
			} else {
				BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER,
						bookAdded.getError());
			}
		} catch (NumberFormatException e) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER,
					BookError.INVALID_NUMBERS.toString());
		}
	}

	@FXML
	private void backToManagerView() {
		BookStoreApp.showManager();
	}

	@FXML
	private void goHome() {
		BookStoreApp.showCustomer(true);
	}

	@Override
	public void initData(Parameters parameters) {
		title.clear();
		publisherName.clear();
		isbn.clear();
		publicationYear.clear();
		quantity.clear();
		price.clear();
		minimumThreshold.clear();
		category.setItems(categoriesList);
		category.setValue(categoriesList.get(0));
		fullName.setText(BookStoreApp.getUser().getFullName());
	}
}
