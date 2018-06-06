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
	    	Dialog<Pair<String, String>> dialog = new Dialog<>();
	    	dialog.setTitle("Place new Order");
	    	dialog.setHeaderText("PLease Enter the following information");
   
	    	// Set the button types.
	    	ButtonType placeButtonType = new ButtonType("Place Order", ButtonData.OK_DONE);
	    	dialog.getDialogPane().getButtonTypes().addAll(placeButtonType, ButtonType.CANCEL);

	    	
	    	GridPane grid = new GridPane();
	    	grid.setHgap(10);
	    	grid.setVgap(10);
	    	grid.setPadding(new Insets(20, 150, 10, 10));       

	    	TextField isbnValue = new TextField();
	    	isbnValue.setPromptText("EX: CS1234");

	    	TextField quantityValue = new TextField();
	    	quantityValue.setPromptText("EX: 5");

	    	grid.add(new Label("BOOK ISBN:"), 0, 0);
	    	grid.add(isbnValue, 1, 0);
	    	grid.add(new Label("Quantity:"), 0, 1);
	    	grid.add(quantityValue, 1, 1);

	    	// Enable login button.
	    	Node placeButton = dialog.getDialogPane().lookupButton(placeButtonType);
	    	
	    	placeButton.addEventFilter(
	    		    ActionEvent.ACTION, 
	    		    event -> {
	    		       
	    		    }
	    		);
	  
	    	dialog.getDialogPane().setContent(grid);

	    	Platform.runLater(() -> isbnValue.requestFocus());
	    
	    	((ButtonBase) placeButton).setOnAction(
	    			 event -> {
	    				
	    			 }
	    			);
	 
	       dialog.showAndWait();
	    }
	    
	@FXML
	 private void showPromotePanel() {
	   

  }

	@Override
	public void initData(Parameters parameters) {
		// TODO Auto-generated method stub
		
	}

}
