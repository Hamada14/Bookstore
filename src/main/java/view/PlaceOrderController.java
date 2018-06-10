package view;


import client.BookClient;
import client.alphabit.BookStoreApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import server.ResponseData;
import server.errors.OrderError;
import server.errors.SqlError;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class PlaceOrderController extends Dialog<Object> {
	
	private static final String PLACE_ORDER = "Place Order";
	private static final String TITLE = "Place new Order";
	private static final String SECTION_MESSAGE = "PLease Enter the following information";
	private static final String BOOK_NAME_EXAMPLE = "ISBN-10: 0-596-52068-9";
	private static final String QUANTITY_EXAMPLE = "EX: 5";
	private static final String ISBN = "ISBN";
	private static final String QUANTITY = "Quantity";
	
	private TextField isbn;
	private TextField quantity;
	
	public PlaceOrderController() {
		setTitle(TITLE);
		setHeaderText(SECTION_MESSAGE);
		initializeGrid();
		initializePlaceButton();
		initializeGrid();
	}
	
	private void initializePlaceButton() {
		ButtonType placeButtonType = new ButtonType(PLACE_ORDER, ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(placeButtonType, ButtonType.CANCEL);
		Node placeButton = getDialogPane().lookupButton(placeButtonType);
		placeButton.addEventFilter(ActionEvent.ACTION, event -> {
			ResponseData isValidClick = placeButtonFilter();
			if(!isValidClick.isSuccessful()) {
				BookStoreApp.displayDialog(AlertType.INFORMATION, SqlError.ERROR_MESSAGE_TITLE.toString(),
						OrderError.ERROR_MESSAGE_HEADER.toString(), isValidClick.getError());
				event.consume();
			}
		});
	}
	
	private ResponseData placeButtonFilter() {
		return BookClient.getServer().placeOrder(BookStoreApp.getUser().getIdentity(), isbn.getText(), quantity.getText());
	}
	
	private void initializeGrid() {
		isbn = new TextField();
		quantity = new TextField();
		isbn.setPromptText(BOOK_NAME_EXAMPLE);
		quantity.setPromptText(QUANTITY_EXAMPLE);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		grid.add(new Label(ISBN), 0, 0);
		grid.add(isbn, 1, 0);
		grid.add(new Label(QUANTITY), 0, 1);
		grid.add(quantity, 1, 1);
		
		Platform.runLater(() -> isbn.requestFocus());
		
		getDialogPane().setContent(grid);
		
	}
}
