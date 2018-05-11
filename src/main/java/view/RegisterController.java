package view;


import client.BookClient;
import client.alphabit.BookStoreUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import server.ResponseData;
import server.database.entities.User;
import server.database.entities.UserBuilder;

public class RegisterController {
		
	private static final String ERROR_MESSAGE_TITLE = "Couldn't Register";
	private static final String ERROR_MESSAGE_HEADER = "Please fix the following";
	
	private static final String SUCCESSFUL_TITLE = "Registered Successfully";
	private static final String SUCCESSFUL_TEXT = "Account registered successfully";
	
	@FXML private TextField password;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField address;
	@FXML private TextField email;
	@FXML private TextField phoneNumber;

	@FXML
	private void gotoLogin() {
		BookStoreUI.showLogin();
	}

	@FXML
	private void registerUser() {
		User registeredUser = getUser();
		ResponseData response = BookClient.getServer().addNewUser(registeredUser);
		if(!response.isSuccessful()) {
			displayErrorDialog(response.getError());
		} else {
			displaySuccessDialog();
			BookStoreUI.showLogin();
		}
	}
	
	private User getUser() {
		UserBuilder userBuilder = new UserBuilder();
		userBuilder.setFirstName(firstName.getText());
		userBuilder.setLastName(lastName.getText());
		userBuilder.setPassword(password.getText());
		userBuilder.setAddress(address.getText());
		userBuilder.setEmail(email.getText());
		userBuilder.setPhoneNumber(phoneNumber.getText());
		return userBuilder.buildUser();
	}
	
	private void displayErrorDialog(String errorMessage) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(ERROR_MESSAGE_TITLE);
		alert.setHeaderText(ERROR_MESSAGE_HEADER);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
	
	private void displaySuccessDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(SUCCESSFUL_TITLE);
		alert.setHeaderText(null);
		alert.setContentText(SUCCESSFUL_TEXT);

		alert.showAndWait();
	}
}
