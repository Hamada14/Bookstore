package view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class NewQuantityController extends Dialog<Integer> {

	private static final String TITLE = "Available Quantity changed!";
	private static final String HEADER = " For book : %s max avaiable Qantity: %d";
	private static final String MESSAGE = "Please enter your new Quantity then checkout AGAIN";
	private static final String BUTTON_NAME = "Change";

	public NewQuantityController(String bookTitle, int oldQuantity) {

		intializeHeader(bookTitle, oldQuantity);
		TextField quantity = new TextField();
		this.getDialogPane().setContent(intializeInputDialog(quantity));

		ButtonType changeButtonType = new ButtonType(BUTTON_NAME, ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);
		Node changeButton = this.getDialogPane().lookupButton(changeButtonType);

		this.setResultConverter(dialogButton -> {
			if (dialogButton == changeButtonType) {
				try {
					return Integer.parseInt(quantity.getText());
				} catch (NumberFormatException e) {
					return -1;
				}
			}
			return -1;
		});
	}

	private void intializeHeader(String bookTitle, int oldQuantity) {
		this.setTitle(TITLE);
		String header = String.format(HEADER, bookTitle, oldQuantity);
		this.setHeaderText(header);
		this.setContentText(MESSAGE);
	}

	private GridPane intializeInputDialog(TextField quantity) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		grid.add(new Label("      "), 0, 0);
		grid.add(quantity, 1, 0);
		return grid;
	}
}
