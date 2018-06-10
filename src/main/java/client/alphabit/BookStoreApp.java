package client.alphabit;

import javafx.application.Application;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import server.database.entities.book.Book;
import server.database.entities.shoppingcart.ShoppingCart;
import server.database.entities.user.User;

public class BookStoreApp extends Application {

	private static final String APP_TITLE = "Alphabet Bookstore";
	private static final String LOGIN_VIEW = "/LoginView.fxml";
	private static final String REGISTER_VIEW = "/RegisterView.fxml";
	private static final String MANAGER_VIEW = "/ManagerView.fxml";
	private static final String CUSTOMER_VIEW = "/CustomerView.fxml";
	private static final String BOOK_VIEW = "/BookView.fxml";
	private static final String ORDERS_VIEW = "/OrdersView.fxml";
	private static final String EDIT_PROFILE_VIEW = "/EditProfileView.fxml";
	private static final String ADD_BOOK_VIEW = "/AddBook.fxml";
	private static final String ALL_ORDERS_VIEW = "/AllOrdersView.fxml";
	private static final String EDIT_BOOK_VIEW = "/EditBookView.fxml";
	private static final String AUTHORS_VIEW = "/AuthorsManagment.fxml";
	
	private Stage primaryStage;
	private static ControlForm login, register, manager, customer, bookView, ordersView, editProfileView, addBookView,
			editBookView, allOrdersView, authorsView;
	private static ShoppingCart currentCart;
	private static User currentUser;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(APP_TITLE);
		currentCart = new ShoppingCart();
		login = new Controller(primaryStage, LOGIN_VIEW);
		register = new Controller(primaryStage, REGISTER_VIEW);
		manager = new Controller(primaryStage, MANAGER_VIEW);
		customer = new Controller(primaryStage, CUSTOMER_VIEW);
		bookView = new Controller(primaryStage, BOOK_VIEW);
		addBookView = new Controller(primaryStage, ADD_BOOK_VIEW);
		ordersView = new Controller(primaryStage, ORDERS_VIEW);
		editProfileView = new Controller(primaryStage, EDIT_PROFILE_VIEW);
		allOrdersView = new Controller(primaryStage, ALL_ORDERS_VIEW);
		authorsView = new Controller(primaryStage, AUTHORS_VIEW);
		editBookView = new Controller(primaryStage, EDIT_BOOK_VIEW);
		showLogin();
//		showManager();
	}

	public static void showLogin() {
		login.getController().initData(null);
		login.show();
	}
	
	public static void showAuthorsView() { 
		authorsView.getController().initData(null);
		authorsView.show();
	}
	
	public static void showAllOrdersView() {
		allOrdersView.getController().initData(null);
		allOrdersView.show();
	}

	public static void showAddNewBook() {
		addBookView.getController().initData(new view.Parameters());
		addBookView.show();
	}

	public static void showRegister() {
		register.getController().initData(null);
		register.show();
	}

	public static void showEditProfile() {
		editProfileView.getController().initData(null);
		editProfileView.show();
	}

	public static void showBookView(Book book) {
		view.Parameters params = new view.Parameters();
		params.setBook(book);
		bookView.getController().initData(params);
		bookView.show();
	}

	public static void editBookView(Book book) {
		view.Parameters params = new view.Parameters();
		params.setBook(book);
		editBookView.getController().initData(params);
		editBookView.show();
	}
	
	public static void showOrdersView() {
		ordersView.getController().initData(null);
		ordersView.show();
	}

	public static void setUser(User user) {
		BookStoreApp.currentUser = user;
	}

	public static User getUser() {
		return currentUser;
	}

	public static void showCustomer(boolean editOrBuyMode) {
		view.Parameters params = new view.Parameters();
		params.setEditOrBuyMode(editOrBuyMode);
		customer.getController().initData(params);
		customer.show();
	}
	
	public static void logOut() {
		setUser(null);
		getShoppingCart().clearCart();
		showLogin();
	}

	public static void showManager() {
		manager.getController().initData(null);
		manager.show();
		// if (BookClient.getServer().isManager(currentUser.getIdentity())) {
		// manager.show();
		// } else {
		// displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null,
		// MANAGER_ERROR_MESSAGE);
		// }
	}

	public static ShoppingCart getShoppingCart() {
		return currentCart;
	}

	public static void displayDialog(AlertType alertType, String dialogTitle, String dialogHeader, String dialogText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(dialogTitle);
		alert.setHeaderText(dialogHeader);
		alert.setContentText(dialogText);
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
