package view;


import alphabit.BookStoreUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterController {
		
	@FXML private TextField userName;
	@FXML private TextField password;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField address;
	@FXML private TextField email;

	@FXML
	private void gotoLogin() {
		BookStoreUI.showLogin();
	}

}
