package view;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import server.ResponseData;
import server.database.entities.User;
import server.database.entities.UserBuilder;

public class RegisterController implements CustomController {

	private static final String ERROR_MESSAGE_TITLE = "Registeration Error";
	private static final String ERROR_MESSAGE_HEADER = "Please fix the following";

	private static final String SUCCESSFUL_TITLE = "Registered Successfully";
	private static final String SUCCESSFUL_TEXT = "Account registered successfully";

	@FXML private TextField userName;
	@FXML private TextField password;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField email;
	@FXML private TextField phoneNumber;
	@FXML private TextField passwordValue1;
	@FXML private TextField passwordValue2;
	@FXML private Label newPassword1;
	@FXML private Label newPassword2;
	@FXML private TextField street;
	@FXML private TextField city;
	@FXML private ComboBox<String> country;


	@FXML private Button confirm;
	@FXML private Hyperlink signLink;
	 
	@FXML
	private void gotoLogin() {
		BookStoreApp.showLogin();
	}

	@FXML
	private void registerUser() {
		UserBuilder newUserBuilder = getUserBuilder();
		ResponseData response = BookClient.getServer().addNewUser(newUserBuilder);
		if (!response.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, response.getError());
		} else {
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, SUCCESSFUL_TEXT);
			clearTextFields();
			BookStoreApp.showLogin();
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
		userBuilder.setCountry(country.getValue() == null ? "" : country.getValue());
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
		country.setValue("");
	}

	@Override
	public void initData(Parameters parameters) {
		ObservableList<String> countriesList = FXCollections.observableList(User.getValidCountries());
		country.setItems(countriesList);
	}
}
