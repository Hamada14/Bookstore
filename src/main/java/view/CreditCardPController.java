package view;

import java.time.LocalDate;


import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import server.OrderResponseData;
import server.database.entities.shoppingcart.ShoppingCart;
import server.database.entities.shoppingcart.ShoppingCartModel;
import server.database.entities.user.Identity;

public class CreditCardPController extends Dialog<OrderResponseData>{
	
	private static final String TITLE = "Payment Step";
	private static final String SUCCESSFUL_TITLE = "CHECKOUT ..DONE!";
	private static final String SUCCESSFUL_TEXT = "Your shopping items has been bought";
	private static final String PAYMENT_IMG = "../images/Creditcard.png";
	private static final String ERROR_IN_CREDIT = "Enter correct credit number";
	private static final String ERROR_IN_DATE = "Enter correct expiry date";
	private static final String INVALID_INPUT = "Invalid Input";
	private static final int MIN_CREDITCARD_DIGITS = 8;
	private static final int MAX_CREDITCARD_DIGITS = 19;
	
	public CreditCardPController() {
		intializeHeader();
		
		DatePicker expiryDatePicker = new DatePicker(LocalDate.now());
		TextField creditCardNumber = new TextField();
		this.getDialogPane().setContent(initializeGridPane(creditCardNumber, expiryDatePicker));
		
		// Set the button types.
		ButtonType buyButtonType = new ButtonType("Buy", ButtonData.OK_DONE);
	    this.getDialogPane().getButtonTypes().addAll(buyButtonType, ButtonType.CANCEL);
	 // Enable login button.
	 	Node buyButton = this.getDialogPane().lookupButton(buyButtonType);

	 		buyButton.addEventFilter(ActionEvent.ACTION, event -> {

	 			if (!validateCreditCard(creditCardNumber.getText())) {
	 				BookStoreApp.displayDialog(AlertType.ERROR, INVALID_INPUT, null, ERROR_IN_CREDIT);
	 				event.consume();
	 			} else if (expiryDatePicker.getValue().isBefore(LocalDate.now())) {
	 				BookStoreApp.displayDialog(AlertType.ERROR, INVALID_INPUT, null, ERROR_IN_DATE);

	 			}
	 		});
	 		
	 		
	 		this.setResultConverter(dialogButton -> {
	 		    if (dialogButton == buyButtonType) {
	 		    	Identity identity = BookStoreApp.getUser().getIdentity();
	 		    	return checkOutCart(identity, BookStoreApp.getShoppingCart());
	 		    }
	 		    return null;
	 		});
	}
	
	
	private void intializeHeader() {
		this.setTitle(TITLE);
		this.setHeaderText("PLease Enter your Payment Method");
		ImageView creditCardView = new ImageView(this.getClass().getResource(PAYMENT_IMG).toString());
		creditCardView.setFitHeight(100);
		creditCardView.setFitWidth(100);
		creditCardView.setPreserveRatio(true);

		// Set the icon (must be included in the project).
		this.setGraphic(creditCardView);
	}
	
	private GridPane initializeGridPane(TextField creditCardNum, DatePicker expiryDatePicker) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		creditCardNum.setPromptText("Enter your Number");
		grid.add(new Label("Expiry Date:"), 0, 0);
		grid.add(expiryDatePicker, 1, 0);
		grid.add(new Label("Credit Card Number:"), 0, 1);
		grid.add(creditCardNum, 1, 1);

		return grid;

	}
	
	private OrderResponseData checkOutCart(Identity identity, ShoppingCart cart) {
		OrderResponseData res = BookClient.getServer().checkoutShoppingCart(identity, cart);
		if (res.isSuccessful()) {
			BookStoreApp.displayDialog(AlertType.INFORMATION, SUCCESSFUL_TITLE, null, SUCCESSFUL_TEXT);
			BookStoreApp.getShoppingCart().clearCart();
			BookStoreApp.showCustomer(true);
		} else {
			if (res.getError().equals(ShoppingCartModel.SHORTAGE_CODE)) {
				return res;
			} else {
				BookStoreApp.displayDialog(AlertType.ERROR, null, null, res.getError());
				return null;
			}
		}
		return null;
	}
	
	private static boolean validateCreditCard(String creditCardNum) {

		String pattern = "[0-9]+";
		if (creditCardNum.matches(pattern) && creditCardNum.length() >= MIN_CREDITCARD_DIGITS
				&& creditCardNum.length() <= MAX_CREDITCARD_DIGITS) {
			return performLuhnAlgorthim(creditCardNum);
		}
		return false;
	}

	private static boolean performLuhnAlgorthim(String creditCardNum) {

		int sum = 0;
		int[] intValues = new int[] { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 };
		for (int i = 0; i < creditCardNum.length(); i++) {
			if (i % 2 == 1) {
				sum += intValues[creditCardNum.charAt(i) - '0'];
			} else {
				sum += creditCardNum.charAt(i) - '0';
			}
		}
		if (sum % 10 == 0) {
			return true;
		}
		return false;
	}
}
