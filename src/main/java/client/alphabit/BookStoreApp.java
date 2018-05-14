package client.alphabit;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import server.database.entities.Identity;
import server.database.entities.ShoppingCart;
import view.CustomController;

public class BookStoreApp extends Application {

	private static final String APP_TITLE = "Alphabet Bookstore";
	private static final String LOGIN_VIEW = "/LoginView.fxml";
	private static final String REGISTER_VIEW = "/RegisterView.fxml";
	private static final String MANAGER_VIEW = "/ManagerView.fxml";
	private static final String CUSTOMER_VIEW = "/CustomerView.fxml";
    private static final String BOOK_VIEW = "/BookView.fxml";
	private Stage primaryStage;
	private static ControlForm login, register, manager, customer, bookView;
	private static ShoppingCart currentCart;
	private static Identity userIdentity;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(APP_TITLE);
		login = new Controller(primaryStage, LOGIN_VIEW);
		register = new Controller(primaryStage, REGISTER_VIEW);
		manager = new Controller(primaryStage, MANAGER_VIEW);
		customer = new Controller(primaryStage, CUSTOMER_VIEW);
		bookView = new Controller(primaryStage, BOOK_VIEW);
		currentCart = new ShoppingCart();
		bookView.show();
		//showLogin();
	}
	
	public static void showLogin() {
		login.show();
	}
	
	public static void showRegister() {
		register.show();
	}

	public static void showBookView() {
		bookView.show();
	}
	
	public static CustomController getBookViewController() {
		return bookView.getController();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	

	public static void showCustomer(Identity userIdentity) {
		BookStoreApp.userIdentity = userIdentity;
		customer.show();
	}
	
	public static void showManager() {
		manager.show();
	}
	
	public static Identity getUser() {
		return userIdentity;
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
}
