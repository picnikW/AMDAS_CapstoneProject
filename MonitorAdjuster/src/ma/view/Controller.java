package ma.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Controller {
	//Btns and Labels
	@FXML
	VBox rightCol;
	@FXML
	VBox leftCol;
	@FXML
	VBox userDDisplay;
	@FXML
	VBox userDInput;
	@FXML
	HBox editBtn;
	@FXML
	HBox confirmBtn;
	
	int DISPLAY_SCREEN = 0;
	int ADD_SCREEN = 1;
	int EDIT_SCREEN = 2;
	int screenId = DISPLAY_SCREEN;
	
	public void start(Stage mainStage) throws Exception {
		 mainStage.setTitle("AMDAS Welcome");
	        
	        mainStage.show();
	}
	
	public void DisplayData(Stage mainStage) {
                
				leftCol.getChildren().contains(userDDisplay); {
				leftCol.getChildren().remove(userDInput);
				leftCol.getChildren().add(editBtn);
				screenId = DISPLAY_SCREEN;
			}

	}
	
	public void toggleButtonDisplay() {
		if (rightCol.getChildren().contains(editBtn)) {
			rightCol.getChildren().remove(confirmBtn);

		} else {
			rightCol.getChildren().add(confirmBtn);
		}
	}
	
	public void changeToAddDisplay(ActionEvent e) {
		toggleButtonDisplay();
		rightCol.getChildren().remove(userDDisplay);
		rightCol.getChildren().add(userDInput);
		screenId = ADD_SCREEN;
		

	}

}
