package view;

import client.BookClient;

import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import server.database.entities.book.Author;
import server.database.entities.book.Book;
import server.database.entities.book.Publisher;


public class AddBookController implements CustomController {

	@FXML private TextField title;
	@FXML private TextField publisherName;
	@FXML private TextField authorFName;
	@FXML private TextField authorLName;
	@FXML private TextField isbn;
	@FXML private TextField publicationYear;
	@FXML private TextField category;
	@FXML private TextField quantity;
	@FXML private TextField price;
	@FXML private TextField minimumThreshold;
	
	@FXML
	private void confirmBookAddition() {
		try {
			Book book = new Book();
			float p = Float.valueOf(price.getText());
			int minimumQuantity = Integer.valueOf(minimumThreshold.getText());
			book.setBookISBN(isbn.getText());
			book.setBookTitle(title.getText());
			book.setCategory(category.getText());
			book.setPublicationYear(publicationYear.getText());
			book.setSellingPrice(p);
			book.setMinimumThreshold(minimumQuantity);
			Author author = new Author(authorFName.getText(), authorLName.getText());
			Publisher publisher  = new Publisher(publisherName.getText());
			boolean bookAdded = BookClient.getServer().addNewBook(book, author, publisher);
			if(bookAdded) {
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
