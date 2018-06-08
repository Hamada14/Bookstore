package view;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import server.ResponseData;
import server.UserResponseData;
import server.database.entities.user.Identity;
import server.database.entities.user.User;
import server.database.entities.user.UserBuilder;
import server.database.entities.user.UserModel;

public class EditProfileController implements CustomController {

	private static final String ERROR_MESSAGE_TITLE = "Update Errors";
	private static final String ERROR_MESSAGE_HEADER = "Please fix the following";

	private static final String SUCCESSFUL_TITLE = "UPDATE ..DONE!";
	private static final String SUCCESSFUL_TEXT = "Changes saved correctly";
	private static final String PASSWORD_ERROR = "The new passwords don't match";
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField street;
	@FXML
	private TextField city;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField oldPassword;
	@FXML
	private TextField newPassword1;
	@FXML
	private TextField newPassword2;

	@FXML
	private Label userName;
	@FXML
	private ComboBox<String> country;

	@Override
	public void initData(Parameters parameters) {
		User currentUser = BookStoreApp.getUser();
		userName.setText(currentUser.getIdentity().getUserName());
		firstName.setText(currentUser.getFirstName());
		lastName.setText(currentUser.getLastName());
		street.setText(currentUser.getStreet());
		city.setText(currentUser.getCity());
		phoneNumber.setText(currentUser.getPhoneNumber());
		ObservableList<String> countriesList = FXCollections.observableList(UserModel.getValidCountries());
		country.setItems(countriesList);
		country.setValue(currentUser.getCountry());
	}

	@FXML
	private void changePersonalInfo() {
		UserBuilder modifiedUserBuilder = getModifiedData();
		UserResponseData response = BookClient.getServer().editUserInformation(modifiedUserBuilder);
		if (!response.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, response.getError());
		} else {
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, SUCCESSFUL_TEXT);
			BookStoreApp.setUser(response.getUser());
			BookStoreApp.showCustomer();
		}
	}

	@FXML
	private void updatePassword() {
		String newPasswordVal = newPassword1.getText();
		if (newPasswordVal.equals(newPassword2.getText())) {
			Identity currentIdentity = BookStoreApp.getUser().getIdentity();
			ResponseData response = BookClient.getServer().editUserIdentity(currentIdentity, newPasswordVal);
			if (!response.isSuccessful()) {
				BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER,
						response.getError());
			} else {
				BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, SUCCESSFUL_TEXT);
				BookStoreApp.getUser().getIdentity().setPassword(newPasswordVal);
				BookStoreApp.showCustomer();
			}
		} else {
			BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, PASSWORD_ERROR);
		}
	}

	@FXML
	private void goHome() {
		BookStoreApp.showCustomer();
	}

	private UserBuilder getModifiedData() {
		User user = BookStoreApp.getUser();
		UserBuilder modifiedUserBuilder = new UserBuilder();
		modifiedUserBuilder.setFirstName(firstName.getText());
		modifiedUserBuilder.setLastName(lastName.getText());
		modifiedUserBuilder.setPhoneNumber(phoneNumber.getText());
		modifiedUserBuilder.setStreet(street.getText());
		modifiedUserBuilder.setCity(city.getText());
		modifiedUserBuilder.setCountry(country.getValue());
		modifiedUserBuilder.setEmail(user.getEmail());
		modifiedUserBuilder.setUserName(user.getIdentity().getUserName());
		modifiedUserBuilder.setPassword(user.getIdentity().getPassword());
		return modifiedUserBuilder;
	}
}
