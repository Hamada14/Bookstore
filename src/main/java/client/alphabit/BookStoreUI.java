package client.alphabit;

import javafx.application.Application;
import javafx.stage.Stage;

public class BookStoreUI extends Application {

	private static final String APP_TITLE = "Alphabet Bookstore";
	private static final String LOGIN_VIEW = "/LoginView.fxml";
	private static final String REGISTER_VIEW = "/RegisterView.fxml";
	private static final String MANAGER_VIEW = "/ManagerView.fxml";

	private Stage primaryStage;
	private static ControlForm login, register, manager;
	

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(APP_TITLE);
		login = new Controller(primaryStage, LOGIN_VIEW);
		register = new Controller(primaryStage, REGISTER_VIEW);
		manager = new Controller(primaryStage, MANAGER_VIEW);
		showLogin();
	}
	
	public static void showLogin() {
		login.show();
	}
	
	public static void showRegister() {
		register.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
