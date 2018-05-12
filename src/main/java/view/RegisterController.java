package view;


import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
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
	
	@FXML private TextField userName;
	@FXML private TextField password;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField address;
	@FXML private TextField email;
	@FXML private TextField phoneNumber;

	@FXML
	private void gotoLogin() {
		BookStoreApp.showLogin();
	}

	@FXML
	private void registerUser() {
		User registeredUser = getUser();
		ResponseData response = BookClient.getServer().addNewUser(registeredUser);
		if(!response.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, response.getError());
		} else {
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, SUCCESSFUL_TEXT);
			clearTextFields();
			BookStoreApp.showLogin();
		}
	}
	
	private User getUser() {
		UserBuilder userBuilder = new UserBuilder();
		userBuilder.setUserName(userName.getText());
		userBuilder.setFirstName(firstName.getText());
		userBuilder.setLastName(lastName.getText());
		userBuilder.setPassword(password.getText());
		userBuilder.setAddress(address.getText());
		userBuilder.setEmail(email.getText());
		userBuilder.setPhoneNumber(phoneNumber.getText());
		return userBuilder.buildUser();
	}
	

	private void clearTextFields() {
		userName.clear();
		firstName.clear();
		lastName.clear();
		password.clear();
		address.clear();
		email.clear();
		phoneNumber.clear();
	}
}
