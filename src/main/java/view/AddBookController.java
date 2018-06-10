package view;

import client.BookClient;

import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import server.ResponseData;
import server.database.entities.book.BookBuilder;
import server.database.entities.publisher.Publisher;



public class AddBookController implements CustomController {
	
	private static final String ERROR_MESSAGE_TITLE = "Error!";
	private static final String ERROR_MESSAGE_HEADER = "Couldn't edit";
	private static final String INVALID_PRICE = "Invalid value for price";
	
	@FXML private TextField title;
	@FXML private TextField publisherName;
	@FXML private TextField isbn;
	@FXML private TextField publicationYear;
	@FXML private ComboBox<String> category;
	@FXML private TextField quantity;
	@FXML private TextField price;
	@FXML private TextField minimumThreshold;
	
	private static final ObservableList<String> categoriesList = FXCollections
			.observableArrayList(BookClient.getServer().getCategories());
	
	
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
			Publisher publisher  = new Publisher(publisherName.getText());
			bookBuilder.setPublisher(publisher);
			ResponseData bookAdded = BookClient.getServer().addNewBook(bookBuilder);
			if(bookAdded.isSuccessful()) {
				System.out.println("Zwe");
				BookStoreApp.showManager();
			}
			System.out.println(bookAdded.getError());
		} catch(NumberFormatException e) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, INVALID_PRICE);
		}
	}
	
	
	@FXML 
	private void backToManagerView() {
		BookStoreApp.showManager();
	}
	
	@FXML
	private void goHome() {
		
	}

	@Override
	public void initData(Parameters parameters) {
		category.setItems(categoriesList);
		category.setValue(categoriesList.get(0));
	}
}
