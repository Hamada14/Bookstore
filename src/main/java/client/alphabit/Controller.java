package client.alphabit;

import java.io.IOException;

import javafx.stage.Stage;
import view.CustomController;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Controller implements ControlForm {

	private Stage primaryStage;
	private BorderPane view;
	private Scene scene;
	private CustomController controller;
	
	public Controller(Stage primaryStage, String fxml) {
		this.primaryStage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			view = ( BorderPane) loader.load();
			controller = loader.<CustomController>getController();
			if (controller == null) {
				System.out.println(fxml);
			}
			scene = new Scene(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show() {
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public CustomController getController() {
		// TODO Auto-generated method stub
		return controller;
	}
}
