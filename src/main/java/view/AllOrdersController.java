package view;

import java.sql.ResultSet;

import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class AllOrdersController implements CustomController {
	
	@FXML private TableView ordersTable;
	
	@FXML
	private void loadMore() {
	
	}
	
	@FXML
	private void backToManagerView() {
		ordersTable.getItems().clear();
		BookStoreApp.showManager();
	}
	
	@FXML
	private void goHome() {
		
	}

	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub
	}
	
	private void load() {
//		ResultSet BookClient.getServer().getAllOrders();
	}

}
