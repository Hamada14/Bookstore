package alphabit.view;

import alphabit.BookStoreUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


public class LoginController {

	@FXML private TextField userName;
	@FXML private PasswordField password;
	
	@FXML
	private void goToRegister() {
		BookStoreUI.showRegister();
	}
	
	@FXML
	private void signInAction() {
		System.out.println(userName);
	}
}
