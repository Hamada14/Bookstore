package view;

import java.util.Optional;


import client.alphabit.BookStoreApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.ButtonType;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import server.OrderResponseData;

import server.database.entities.Order;
import server.database.entities.shoppingcart.ShoppingCart;

public class OrdersController implements CustomController {
	private static final String CONFIRM_REMOVE = "Are you sure you want to delete this order?";
	private static final String DELETE_IMG = "../images/delete.png";
	private static final String ERROR_MESSAGE_TITLE = "New quantity invalid";
	private static final String ERROR_MESSAGE = "Please remove this order from card to proceed";
	private static final int ID_INDEX = 0;
	private static final int TITLE_INDEX = 1;
	private static final int PRICE_INDEX = 2;
	private static final int QUANTITY_INDEX = 3;
	private static final int DELETE_INDEX = 4;

	@FXML
	private Label fullName;
	
	@FXML
	private Label totalPrices;
	@FXML
	private GridPane ordersPane;
	@FXML
	private Label userName;

	private void showItems() {
		int index = 1;
		float totalPrice = 0f;
		ordersPane.getChildren().clear();
		ShoppingCart currentCart = BookStoreApp.getShoppingCart();
		for (Order order : currentCart) {
			Text indexTxt = new Text(Integer.toString(index));
			ordersPane.add(indexTxt, ID_INDEX, index);

			Text titleTxt = new Text(order.getBook().getBookTitle());
			ordersPane.add(titleTxt, TITLE_INDEX, index);

			float priceOfQuantity = order.getBook().getSellingPrice() * order.getQuantity();
			Text priceTxt = new Text(Float.toString(priceOfQuantity));
			ordersPane.add(priceTxt, PRICE_INDEX, index);
			totalPrice += priceOfQuantity;

			Text quantityTxt = new Text(Integer.toString(order.getQuantity()));
			ordersPane.add(quantityTxt, QUANTITY_INDEX, index);

			Image imageDecline = new Image(getClass().getResourceAsStream(DELETE_IMG), 20, 20, true, true);
			Button removeMe = new Button("", new ImageView(imageDecline));
			ordersPane.add(removeMe, DELETE_INDEX, index);

			removeMe.setGraphic(new ImageView(imageDecline));
			removeMe.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Alert conf = new Alert(AlertType.CONFIRMATION, CONFIRM_REMOVE);
					Optional<ButtonType> result = conf.showAndWait();
					if (result.isPresent() && result.get().equals(ButtonType.OK)) {
						currentCart.removeOrder(order);
						ordersPane.getChildren().clear();
						showItems();
					}
				}
			});

			index++;
		}
		totalPrices.setText(Float.toString(totalPrice));
	}

	@Override
	public void initData(Parameters parameters) {
		showItems();
        fullName.setText(BookStoreApp.getUser().getFullName());
	}

	@FXML
	private void goHome() {
		BookStoreApp.showCustomer(true);
	}

	@FXML
	private void checkOut() {
		Dialog<OrderResponseData> creditCardDialog = new CreditCardPController();
		creditCardDialog.showAndWait();
		OrderResponseData res = creditCardDialog.getResult();
		if (res != null) {
			String bookTitle = res.getOrder().getBook().getBookTitle();
			Dialog<Integer> newQuantityDialog = new NewQuantityController(bookTitle, res.getOldQuantity());
			newQuantityDialog.showAndWait();
			int newQuantity = newQuantityDialog.getResult();
			if (newQuantity != -1) {
				updateOrders(res.getOrder(), newQuantity);
			} else {
				BookStoreApp.displayDialog(AlertType.ERROR, ERROR_MESSAGE_TITLE, null, ERROR_MESSAGE);
			}

		}
	}

	private void updateOrders(Order order, int newQuantity) {
		ShoppingCart cart = BookStoreApp.getShoppingCart();
		order.setQuantity(newQuantity);
		cart.addOrder(order);
		showItems();
	}

}
