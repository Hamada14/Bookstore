package view;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import server.ResponseData;
import server.database.entities.Identity;
import javafx.scene.control.PasswordField;

public class LoginController {

	private static final String ERROR_LOGIN_TITLE = "Login failed";

	@FXML private TextField userName;
	@FXML private PasswordField password;

	@FXML
	private void goToRegister() {
		BookStoreApp.showRegister();
	}

	@FXML
	private void signInAction() {
		Identity identity = new Identity(userName.getText(), password.getText());
		ResponseData response = BookClient.getServer().loginUser(identity);
		if (response.isSuccessful()) {
			userName.clear();
			password.clear();
			BookStoreApp.showCustomer(identity);
		} else {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_LOGIN_TITLE, null, response.getError());
		}
	}
}
