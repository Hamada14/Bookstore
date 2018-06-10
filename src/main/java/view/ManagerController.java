package view;


import client.BookClient;

import client.alphabit.BookStoreApp;
import javafx.fxml.FXML;
import server.BookStoreServer;
import server.database.report.ReportViewer;



public class ManagerController implements CustomController {
	
	@FXML
	private void showPlaceNewOrderPanel() {
		PlaceOrderController placeBookOrder = new PlaceOrderController();
		placeBookOrder.showAndWait();
	}
	
	@FXML
	private void showAddNewBookPanel() {
		BookStoreApp.showAddNewBook();
	}

	@FXML
	private void showPromoteUser() {
		PromoteUserController promoteUserController = new PromoteUserController();
		promoteUserController.showAndWait();
	}

	@FXML
	private void createReport() {
		BookStoreServer server = BookClient.getServer();
		ReportViewer writer = new ReportViewer(server);
		writer.viewReport();
	}
	
	@FXML
	private void goToOrdersManagement() {
		BookStoreApp.showAllOrdersView();
	}
	
	@FXML
	private void goToCustomerView() {
		BookStoreApp.showCustomer();
	}
	
	@FXML void logOut() {
		BookStoreApp.logOut();
	}
	
	@FXML
	private void goToAuthorsManagement() {
		BookStoreApp.showAuthorsView();
	}
	
	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub
	}
}
