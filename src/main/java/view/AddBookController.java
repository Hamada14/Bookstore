package view;

import client.BookClient;

import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import server.ResponseData;
import server.database.entities.author.Author;
import server.database.entities.book.BookBuilder;
import server.database.entities.publisher.Publisher;



public class AddBookController implements CustomController {

	@FXML private TextField title;
	@FXML private TextField publisherName;
	@FXML private TextField isbn;
	@FXML private TextField publicationYear;
	@FXML private TextField category;
	@FXML private TextField quantity;
	@FXML private TextField price;
	@FXML private TextField minimumThreshold;
	
	@FXML
	private void confirmBookAddition() {
		try {
			BookBuilder bookBuilder = new BookBuilder();
			float p = Float.valueOf(price.getText());
			int minimumQuantity = Integer.valueOf(minimumThreshold.getText());
			bookBuilder.setBookISBN(isbn.getText());
			bookBuilder.setBookTitle(title.getText());
			bookBuilder.setCategory(category.getText());
			bookBuilder.setPublicationYear(publicationYear.getText());
			bookBuilder.setSellingPrice(p);
			bookBuilder.setMinimumThreshold(minimumQuantity);
			Publisher publisher  = new Publisher(publisherName.getText());
			bookBuilder.setPublisher(publisher);
			ResponseData bookAdded = BookClient.getServer().addNewBook(bookBuilder, publisher);
			if(bookAdded.isSuccessful()) {
				BookStoreApp.showManager();
			}
		} catch(NumberFormatException e) {
			
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
		// TODO Auto-generated method stub
		
	}

}
