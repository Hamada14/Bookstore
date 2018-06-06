package view;

import client.alphabit.BookStoreApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class ManagerController implements CustomController {

	@FXML
	private void showPlaceNewOrderPanel() {
		PlaceOrderController placeBookOrder = new PlaceOrderController();
		placeBookOrder.showAndWait();
	}

	@FXML
	private void showPromotePanel() {

	}

	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub

	}

}
