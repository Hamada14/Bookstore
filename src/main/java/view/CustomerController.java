package view;
import java.net.URL;


import java.util.ArrayList;
import java.util.ResourceBundle;

import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import javafx.scene.layout.VBox;
import server.database.entities.Book;


public class CustomerController implements Initializable, CustomController{

	//private Identity userIdentity;
	ObservableList<String> categoriesList = FXCollections.observableArrayList("All","Science","Art", "Geography", "Religion","History");
	@FXML private ChoiceBox<String> categories;
	@FXML private VBox booksLinks;
	@FXML private  Button loadMore;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		categories.setValue("All");		
		categories.setItems(categoriesList);
	}
	
	@FXML 
	private void searchBooks() {
		
		ArrayList<Book> books = new ArrayList<Book>();
		for (int i = 0; i < 10; i++) {	
			float x = 9.7f;
			Book book = new Book ("1234",new String("book" + i), "1960", x, "art", true);
			books.add(book);
		}
		viewBooks(books);
	}
	
	private void viewBooks(ArrayList<Book> books) {
		for (int i = 0; i < books.size(); i++) {
			BookHyperLink link = new BookHyperLink(books.get(i));
			link.setText(new String (i +". " + books.get(i).getBookTitle()));
			link.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent e) {
			    	Parameters params = new Parameters();
			    	params.setBook(link.getBook());
			    	BookStoreApp.showBookView();
			    	CustomController bookController = BookStoreApp.getBookViewController();
			    	bookController.initData(params);
			        System.out.println(link.getBook().getBookTitle());
			    }
			});
			booksLinks.getChildren().add(link);
		}
		
		loadMore.setVisible(true);
	}

	@Override
	public void initData(Parameters parameters) {
		
		
	}
	
	@FXML
	private void viewOrders() {
		BookStoreApp.showOrdersView();
	}
	
	@FXML 
	private void logOut() {
		BookStoreApp.setUser(null);
		BookStoreApp.showLogin();
	}
	
	@FXML 
	private void goToManagerView() {
		BookStoreApp.showManager();
	}
}
