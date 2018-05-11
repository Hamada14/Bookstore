package view;

import client.BookClient;
import client.alphabit.BookStoreUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import server.ResponseData;
import server.database.entities.Identity;
import javafx.scene.control.PasswordField;


public class LoginController {

	private static final String ERROR_LOGIN_TITLE = "Login failed";
	
	@FXML private TextField email;
	@FXML private PasswordField password;
	
	@FXML
	private void goToRegister() {
		BookStoreUI.showRegister();
	}
	
	@FXML
	private void signInAction() {
		Identity identity = new Identity(email.getText(), password.getText());
		ResponseData response = BookClient.getServer().loginUser(identity);
		if(response.isSuccessful()) {
			email.clear();
			password.clear();
			if(BookClient.getServer().isManager(identity)) {
				BookStoreUI.showManager();	
			} else {
				BookStoreUI.showCustomer();	
			}
		} else {
			BookStoreUI.displayDialog(AlertType.ERROR, ERROR_LOGIN_TITLE, null, response.getError());
		}
	}
}
