package client.alphabit;

import java.io.IOException;

import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Controller implements ControlForm {

	private Stage primaryStage;
	private BorderPane view;
	private Scene scene;
	
	public Controller(Stage primaryStage, String fxml) {
		this.primaryStage = primaryStage;
		try {
			view = FXMLLoader.load(Controller.class.getResource(fxml));
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
}
