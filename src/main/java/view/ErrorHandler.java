package view;

import java.sql.SQLException;

import client.alphabit.BookStoreApp;
import javafx.scene.control.Alert.AlertType;

public class ErrorHandler {

	public static String ERROR_TITLE = "OOPS..Something Happened";
	public static void displayError(SQLException error) {
		String message = error.getMessage();
		BookStoreApp.displayDialog(AlertType.ERROR, ERROR_TITLE, null, message);
	}
}
