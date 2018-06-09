package view;

import java.util.List;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import server.database.entities.Order;

public class AllOrdersController implements CustomController {

	private static final int LIMIT = 10;
	private static final String ISBN_COL = "bookISBN";
	private static final String QUANTITY_COL = "quantity";
	private static final String CONFIRMATION_COL = "confirm";
	private static final String CONFIRM = "Confirm";
	
	private int offset = 0;
	
	private ObservableList<OrderTuple> ordersList;
	
	@FXML private TableView<OrderTuple> ordersTable;
	@FXML private TableColumn<OrderTuple, String> bookISBNCol;
	@FXML private TableColumn<OrderTuple, Integer> quantityCol;
	@FXML private TableColumn<OrderTuple, Hyperlink> confirmation;
	
	@FXML
	private void loadMore() {
		load();
	}
	
	@FXML
	private void backToManagerView() {
		ordersTable.getItems().clear();
		ordersList.clear();
		offset = 0;
		BookStoreApp.showManager();
	}
	
	@FXML
	private void goHome() {
		
	}
	
	@FXML
	private void refresh() {
		ordersList = FXCollections.observableArrayList();
		offset = 0;
		load();
	}

	@Override
	public void initData(Parameters parameters) {
		bookISBNCol.setCellValueFactory(new PropertyValueFactory<OrderTuple, String>(ISBN_COL));
        quantityCol.setCellValueFactory(new PropertyValueFactory<OrderTuple, Integer>(QUANTITY_COL));
        confirmation.setCellValueFactory(new PropertyValueFactory<OrderTuple, Hyperlink>(CONFIRMATION_COL));
        refresh();
	}
	
	
	
	private void load() {
		List<Order> orders = BookClient.getServer().getOrders(offset, LIMIT);
		offset += orders.size();
		fillTabel(orders);
	}
	
	private void fillTabel(List<Order> orders) {
		for(Order order : orders) {
			Hyperlink confirmH = new Hyperlink();
			confirmH.setText(CONFIRM);
			confirmH.addEventHandler(ActionEvent.ACTION, event -> {
				deleteOrder(order.getOrderId());
			});
			ordersList.add(new OrderTuple(order, confirmH));
		}
		ordersTable.setItems(ordersList);
	}
	
	private void deleteOrder(int orderId) {
		for(int i = 0; i < ordersList.size(); i++) {
			if(orderId == ordersList.get(i).getId()) {
				ordersList.remove(i);
				BookClient.getServer().deleteOrder(orderId);
				return;
			}
		}
	}

}
