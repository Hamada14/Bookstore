package view;

import client.BookClient;


import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import server.database.entities.user.Identity;
import javafx.scene.control.PasswordField;

public class LoginController implements CustomController {

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
		server.UserResponseData response = BookClient.getServer().loginUser(identity);
		if (response.isSuccessful()) {
			userName.clear();
			password.clear();
			BookStoreApp.setUser(response.getUser());
			BookStoreApp.showCustomer();
		} else {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_LOGIN_TITLE, null, response.getError());
		}
	}

	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub
		
	}
}
