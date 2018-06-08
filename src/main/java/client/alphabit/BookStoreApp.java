package client.alphabit;

import client.BookClient;
import javafx.application.Application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import server.database.entities.Book;
import server.database.entities.Order;
import server.database.entities.ShoppingCart;
import server.database.entities.User;

public class BookStoreApp extends Application {

	private static final String ERROR_MESSAGE_TITLE = "Access Denied!";
	private static final String MANAGER_ERROR_MESSAGE = "You are not allowed to use this mode";
	private static final String APP_TITLE = "Alphabet Bookstore";
	private static final String LOGIN_VIEW = "/LoginView.fxml";
	private static final String REGISTER_VIEW = "/RegisterView.fxml";
	private static final String MANAGER_VIEW = "/ManagerView.fxml";
	private static final String CUSTOMER_VIEW = "/CustomerView.fxml";
    private static final String BOOK_VIEW = "/BookView.fxml";
    private static final String ORDERS_VIEW = "/OrdersView.fxml";
    private static final String EDIT_PROFILE_VIEW = "/EditProfileView.fxml";
	private static final String ADD_BOOK_VIEW = "/AddBook.fxml";
	private Stage primaryStage;
	private static ControlForm login, register, manager, customer, bookView, ordersView, editProfileView, addBookView;
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
		showLogin();
	}

	public static void showLogin() {
		login.show();
	}
	
	public static void showAddNewBook() {
		addBookView.show();
	}
	
	public static void showRegister() {
		register.show();
		register.getController().initData(null);
	}

	public static void showEditProfile() {
		editProfileView.show();
		editProfileView.getController().initData(null);
	}
	public static void showBookView(Book book) {
		bookView.show();
		view.Parameters params = new view.Parameters();
		params.setBook(book);
		bookView.getController().initData(params);
	}

	public static void showOrdersView() {
		ordersView.show();
		ordersView.getController().initData(null);
	}

	public static void setUser(User user) {
		BookStoreApp.currentUser = user;
	}

	public static User getUser() {
		return currentUser;
	}

	public static void showCustomer() {
		customer.show();
		customer.getController().initData(null);
	}

	public static void showManager() {
		if (BookClient.getServer().isManager(currentUser.getIdentity())) {
			manager.show();
		} else {
			displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null, MANAGER_ERROR_MESSAGE);
		}
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

	private static void pushSomeOrders() {
		// float x = 1f;
		// for (int i = 1; i <= 40; i++) {
		// Book book = new Book(Integer.toString(i), new String("boook" + i),"1960", x,
		// "arts", true);
		// currentCart.addOrder(new Order(i, book));
		// }
	}

	public static void main(String[] args) {
		launch(args);
	}
}
