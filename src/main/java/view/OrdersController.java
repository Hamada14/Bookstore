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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import server.database.entities.Order;
import server.database.entities.ShoppingCart;

public class OrdersController implements CustomController {

	private static final String CONFIRM_REMOVE = "Are you sure you want to delete this order?";
	private static final String LABEL_FONT = "Berlin Sans FB Demi Bold";
	private static final String DELETE_IMG = "../images/delete.png";
	@FXML
	private Label totalPrices;
	@FXML
	private GridPane ordersPane;

	private void showItems() {
		ShoppingCart currentCart = BookStoreApp.getShoppingCart();
		int index = 1;
		float totalPrice = 0f;
		for (Order order : currentCart) {
			Text indexTxt = new Text(Integer.toString(index));
			if (ordersPane == null) {
				System.out.println("here");
			}
			ordersPane.add(indexTxt, 0, index);

			Text titleTxt = new Text(order.getBook().getBookTitle());
			ordersPane.add(titleTxt, 1, index);

			Text priceTxt = new Text(Float.toString(order.getBook().getSellingPrice()));
			ordersPane.add(priceTxt, 2, index);
			totalPrice += order.getBook().getSellingPrice();

			Text quantityTxt = new Text(Integer.toString(order.getQuantity()));
			ordersPane.add(quantityTxt, 3, index);

			Image imageDecline = new Image(getClass().getResourceAsStream(DELETE_IMG), 20, 20, true, true);
			Button removeMe = new Button("", new ImageView(imageDecline));
			ordersPane.add(removeMe, 4, index);

			removeMe.setGraphic(new ImageView(imageDecline));
			removeMe.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Alert conf = new Alert(AlertType.CONFIRMATION, CONFIRM_REMOVE);
					Optional<ButtonType> result = conf.showAndWait();
					if (result.isPresent() && result.get().equals(ButtonType.OK)) {
						System.out.println(order.getBook().getBookTitle());
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

	}

}
