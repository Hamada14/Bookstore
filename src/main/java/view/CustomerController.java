package view;


import java.net.URL;


import java.util.ArrayList;


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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import server.BooksResponseData;
import server.database.entities.book.Book;
import server.database.entities.book.BookBuilder;
import server.database.entities.publisher.Publisher;
import server.database.entities.user.Identity;

public class CustomerController implements Initializable, CustomController {

	private static final int LIMIT = 16;
	private static final String TITLE_COL = "bookTitle";
	private static final String ERROR_MESSAGE_TITLE = "Error while searching";

	private static final String ERROR_YEAR= "invalid year, won't be considered";
	private static final String ERROR_PRICE= "invalid price, won't be considered";
	private static final ObservableList<String> categoriesList = FXCollections
			.observableArrayList(BookClient.getServer().getCategories());

	@FXML
	private MenuItem goToManagerModeButton;
	
	@FXML
	private ChoiceBox<String> categories;

	private ObservableList<BookTuple> ordersList;

	@FXML
	private Label fullName;
	
	@FXML
	private TableView<BookTuple> booksTable;
	@FXML
	private TableColumn<BookTuple, BookHyperLink> bookTitleCol;
	@FXML
	private Button loadMore;
	@FXML
	private TextField title;
	@FXML
	private TextField authorName;
	@FXML
	private TextField publisherName;
	@FXML
	private TextField publicationYear;
	@FXML
	private TextField isbn;
	@FXML
	private TextField price;

	private int offset = 0;
	
	private Book criteriaBook;
	
	/*true for buy, false for edit*/
	private boolean editOrBuyMode;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		categories.setValue(BookClient.getServer().getCategories().get(0));
		categories.setItems(categoriesList);
		bookTitleCol.setCellValueFactory(new PropertyValueFactory<BookTuple, BookHyperLink>(TITLE_COL));
		List<String> authors = new ArrayList<>();
		criteriaBook = new Book("", "", "", -1, "", "", 0, 0);
		refresh();

	}

	@FXML
	private void searchBooks() {
		refresh();
		setCriteria();
		loadBooks();
	}

	@FXML
	private void loadBooks() {
		Identity identity = BookStoreApp.getUser().getIdentity();
		BooksResponseData response = BookClient.getServer().advancedSearchBooks(identity, offset, LIMIT, criteriaBook);
		if (response.isSuccessful()) {
			viewBooks(response.getBooks());
			offset += response.getBooks().size();
		} else {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null, response.getError());
		}
	}

	private void refresh() {
		ordersList = FXCollections.observableArrayList();
		booksTable.setItems(ordersList);
		offset = 0;
	}

	private void viewBooks(List<Book> books) {
		for (Book book : books) {
			BookHyperLink titleLink = new BookHyperLink(book);
			titleLink.setText(new String(book.getBookTitle()));
			titleLink.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (editOrBuyMode) {
						BookStoreApp.showBookView(titleLink.getBook());
					}else {
						BookStoreApp.editBookView(titleLink.getBook());
					}
					titleLink.setVisited(false);
				}
			});
			ordersList.add(new BookTuple(titleLink));
		}
		booksTable.setItems(ordersList);
		loadMore.setVisible(true);
	}

	private void setCriteria() {
		criteriaBook.setBookTitle(title.getText());
		criteriaBook.setPublisher(new Publisher(publisherName.getText()));
		criteriaBook.setCategory(categories.getValue());
		criteriaBook.setBookISBN(isbn.getText());
		String yearValue = publicationYear.getText();
		if (yearValue.equals("") || BookBuilder.isValidPublicationYear(yearValue) != null) {
			criteriaBook.setPublicationYear(yearValue);
		} else {
			criteriaBook.setPublicationYear("");
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null, ERROR_YEAR);
		}

		if (price.getText().equals("")) {
			criteriaBook.setSellingPrice(-1);
		} else {

			float priceVal = -1;

			try {
				Float.parseFloat(price.getText());
				if (BookBuilder.isValidSellingPrice(priceVal) != null) {
					criteriaBook.setSellingPrice(priceVal);
				} else {
					criteriaBook.setSellingPrice(priceVal);
					BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null, ERROR_PRICE);
				}
			} catch (Exception e) {
				criteriaBook.setSellingPrice(priceVal);
				BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null, ERROR_PRICE);
			}

		}
	}

	private void clearSearchFields() {
		title.setText("");
		authorName.setText("");
		publisherName.setText("");
		isbn.setText("");
		price.setText("");
		publicationYear.setText("");
		criteriaBook = new Book("", "", "", -1, "", "", 0, 0);
		categories.setValue("");
	}


	@FXML
	private void viewOrders() {
		BookStoreApp.showOrdersView();
	}

	@FXML
	private void logOut() {
		BookStoreApp.logOut();
	}

	@FXML
	private void goToManagerView() {
		BookStoreApp.showManager();
	}

	@FXML
	private void goToInformationForm() {
		BookStoreApp.showEditProfile();
	}
	
	@Override
	public void initData(Parameters parameters) {
		editOrBuyMode = parameters.getEditOrBuyMode();
		clearSearchFields();
		refresh();
		boolean isManager = BookClient.getServer().isManager(BookStoreApp.getUser().getIdentity());
		goToManagerModeButton.setVisible(isManager);
        fullName.setText(BookStoreApp.getUser().getFullName());
	}
}
