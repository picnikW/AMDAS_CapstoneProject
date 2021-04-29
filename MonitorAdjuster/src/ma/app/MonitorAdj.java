

package ma.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import ma.view.Controller;


public class MonitorAdj extends Application {
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/ma/view/monitorAdj.fxml"));
		// main screen
		SplitPane root = (SplitPane) loader.load();
		Controller Controller = loader.getController();
		Controller.start(primaryStage);

		Scene scene = new Scene(root, 727, 467);
	    primaryStage.setScene(scene);
		primaryStage.setTitle("Monitor D Adjust");
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);

	}

}
