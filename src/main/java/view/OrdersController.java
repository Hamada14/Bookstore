package view;

import java.net.URL;
import java.util.ResourceBundle;

import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import server.database.entities.Order;
import server.database.entities.ShoppingCart;

public class OrdersController implements CustomController,  Initializable{
     
	@FXML private Label totalPrices;
	@FXML private GridPane ordersPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ShoppingCart currentCart = BookStoreApp.getShoppingCart();
		for (Order order : currentCart) {
			
		}
	}
	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub
		
	}

	
}
