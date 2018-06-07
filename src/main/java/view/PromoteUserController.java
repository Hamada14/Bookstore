package view;

import client.BookClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class PromoteUserController extends Dialog<Object> {

	private static final String TITLE = "Promote User";
	private static final String SECTION_MESSAGE = "PLease Enter the following information";
	private static final String USER_NAME_EXAMPLE = "Mohamed96";
	private static final String USER_NAME = "User Name";
	private static final String PROMOTE_USER = "Promote";
	
	private TextField userName;
	
	public PromoteUserController() {
		setTitle(TITLE);
		setHeaderText(SECTION_MESSAGE);
		initializeGrid();
		initializePromoteButtons();
	}
	
	private void initializePromoteButtons() {
		ButtonType placeButtonType = new ButtonType(PROMOTE_USER, ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(placeButtonType, ButtonType.CANCEL);
		Node placeButton = getDialogPane().lookupButton(placeButtonType);
		placeButton.addEventFilter(ActionEvent.ACTION, event -> {
			boolean isValidClick = promoteUser();
			if(!isValidClick) {
				event.consume();
			}
		});
	}
	
	private boolean promoteUser() {
		return BookClient.getServer().promoteUser(userName.getText());
	}
	
	private void initializeGrid() {
		userName = new TextField();
		userName.setPromptText(USER_NAME_EXAMPLE);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		grid.add(new Label(USER_NAME), 0, 0);
		grid.add(userName, 1, 0);
		
		Platform.runLater(() -> userName.requestFocus());
		
		getDialogPane().setContent(grid);
		
	}
}
