package view;


import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import server.ResponseData;
import server.database.entities.User;
import server.database.entities.UserBuilder;

public class RegisterController implements CustomController{
		
	private static final String ERROR_MESSAGE_TITLE = "Couldn't Register";
	private static final String ERROR_MESSAGE_HEADER = "Please fix the following";
	
	private static final String SUCCESSFUL_TITLE = "Registered Successfully";
	private static final String SUCCESSFUL_TEXT = "Account registered successfully";
	
    private static final String EDIT_BUTTON_TEXT = "Edit User";
    
    //private static final String 
    
	private boolean firstTimeRegistered;
	
	@FXML private TextField userName;
	@FXML private TextField password;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField address;
	@FXML private TextField email;
	@FXML private TextField phoneNumber;
	@FXML private TextField passwordValue1;
	@FXML private TextField passwordValue2;
	@FXML private Label newPassword1;
	@FXML private Label newPassword2;
    @FXML private Button confirm;
	@FXML private Hyperlink signLink;
	
	
	
	@FXML
	private void gotoLogin() {
		BookStoreApp.showLogin();
	}

	@FXML
	private void registerUser() {
		User registeredUser = getUser();
		if (firstTimeRegistered) {
				ResponseData response = BookClient.getServer().addNewUser(registeredUser);
				if(!response.isSuccessful()) {
					BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, ERROR_MESSAGE_HEADER, response.getError());
				} else {
					BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, SUCCESSFUL_TEXT);
					clearTextFields();
					BookStoreApp.showLogin();
				}
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

	@Override
	public void initData(Parameters parameters) {
		firstTimeRegistered = parameters.getRegisterationMode();
		if (!firstTimeRegistered) {
			signLink.setVisible(false);
			confirm.setText(EDIT_BUTTON_TEXT);
			User currentUser = BookStoreApp.getUser();
			userName.setText(currentUser.getUserName());
			firstName.setText(currentUser.getFirstName());
			lastName.setText(currentUser.getLastName());
			email.setText(currentUser.getEmail());
			address.setText(currentUser.getAddress());
			phoneNumber.setText(currentUser.getEmail());
			newPassword1.setVisible(true);
			newPassword2.setVisible(true);
			passwordValue1.setVisible(true);
			passwordValue2.setVisible(true);
		}
		
	}
}
