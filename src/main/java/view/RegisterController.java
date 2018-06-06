package view;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import server.ResponseData;
import server.database.entities.User;
import server.database.entities.UserBuilder;

public class RegisterController implements CustomController {

	private static final String ERROR_MESSAGE_TITLE = "Registeraing Error";
	private static final String ERROR_MESSAGE_HEADER = "Please fix the following";

	private static final String SUCCESSFUL_TITLE = "Registered Successfully";
	private static final String SUCCESSFUL_TEXT = "Account registered successfully";

	private static final String EDIT_BUTTON_TEXT = "Edit User";

	private boolean firstTimeRegistered;

	@FXML private TextField userName;
	@FXML private TextField password;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField email;
	@FXML private TextField phoneNumber;
	@FXML private TextField street;
	@FXML private TextField city;
	@FXML private TextField country;

	@FXML private Button confirm;
	@FXML private Hyperlink signLink;

	@FXML
	private void gotoLogin() {
		BookStoreApp.showLogin();
	}

	@FXML
	private void registerUser() {
		UserBuilder newUserBuilder = getUserBuilder();
		if (firstTimeRegistered) {
			ResponseData response = BookClient.getServer().addNewUser(newUserBuilder);

			if (!response.isSuccessful()) {
				BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER,
						response.getError());
			} else {
				BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, SUCCESSFUL_TEXT);
				clearTextFields();
				BookStoreApp.showLogin();
			}
		}
	}

	private UserBuilder getUserBuilder() {
		UserBuilder userBuilder = new UserBuilder();
		userBuilder.setUserName(userName.getText());
		userBuilder.setFirstName(firstName.getText());
		userBuilder.setLastName(lastName.getText());
		userBuilder.setPassword(password.getText());
		userBuilder.setEmail(email.getText());
		userBuilder.setPhoneNumber(phoneNumber.getText());
		userBuilder.setStreet(street.getText());
		userBuilder.setCity(city.getText());
		userBuilder.setCountry(country.getText());
		return userBuilder;
	}

	private void clearTextFields() {
		userName.clear();
		firstName.clear();
		lastName.clear();
		password.clear();
		email.clear();
		phoneNumber.clear();
		street.clear();
		city.clear();
		country.clear();
	}

	@Override
	public void initData(Parameters parameters) {
		firstTimeRegistered = parameters.getRegisterationMode();
		if (!firstTimeRegistered) {
			signLink.setVisible(false);
			confirm.setText(EDIT_BUTTON_TEXT);
			User currentUser = BookStoreApp.getUser();
			userName.setText(currentUser.getIdentity().getUserName());
			firstName.setText(currentUser.getFirstName());
			lastName.setText(currentUser.getLastName());
			email.setText(currentUser.getEmail());
			phoneNumber.setText(currentUser.getEmail());
			street.setText(currentUser.getStreet());
			city.setText(currentUser.getCity());
			country.setText(currentUser.getCountry());
		}
	}
}
