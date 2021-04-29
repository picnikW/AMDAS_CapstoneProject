// Jenny Xu, Yuxiang Wang
package ma.view;

import java.util.Optional;

import com.fazecast.jSerialComm.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
	ObservableList<String> resolutionInputList = FXCollections
			.observableArrayList("1080p", "1440p", "4K");
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
	Button editBtn;
	@FXML
	Button okBtn;
	@FXML
	Button backBtn;
	@FXML
	HBox confirmBtns;
	@FXML
	HBox editBtns;
	@FXML
	TextField distanceInput;
	@FXML
	TextField sizeInput;
	@FXML
	private ChoiceBox<String> resolutionInput;
	@FXML
	Label distanceDisplay;
	@FXML
	Label sizeDisplay;
	@FXML
	Label resolutionDisplay;
	
	
	int DISPLAY_SCREEN = 0;
	int ADD_SCREEN = 1;
	int EDIT_SCREEN = 2;
	int screenId = DISPLAY_SCREEN;
	
	//Start the program
	public void start(Stage mainStage) throws Exception {
		 mainStage.setTitle("AMDAS Welcome");
			confirmBtns.getChildren().remove(backBtn);
			confirmBtns.getChildren().remove(okBtn);
			leftCol.getChildren().remove(userDInput);
			
//			SerialPort sp =SerialPort.getCommport("")
			
	        mainStage.show();
	}
	
	public void showData(Stage mainStage) {
			sizeDisplay.setText("-");
			distanceDisplay.setText("-");
			resolutionDisplay.setText("-");
		
	}
	
	@FXML
	private void initialize() {
			resolutionInput.setItems(resolutionInputList);
			
	}
	public void DisplayData(Stage mainStage) {
                
				leftCol.getChildren().contains(userDDisplay); {
				leftCol.getChildren().remove(userDInput);
				leftCol.getChildren().add(editBtn);
				screenId = DISPLAY_SCREEN;}
			}

	public void toggleButtonDisplay() {
		if (editBtns.getChildren().contains(editBtn)) {
			editBtns.getChildren().remove(editBtn);
			confirmBtns.getChildren().add(okBtn);
			confirmBtns.getChildren().add(backBtn);

		} else {
			editBtns.getChildren().add(editBtn);
			confirmBtns.getChildren().remove(okBtn);
			confirmBtns.getChildren().remove(backBtn);
			
		}
	}
	
	public void changeToEditDisplay(ActionEvent e) {
		toggleButtonDisplay();
		leftCol.getChildren().remove(userDDisplay);
		leftCol.getChildren().add(userDInput);
		screenId = EDIT_SCREEN;
	}
		
	public void changeToDataDisplay() {
		toggleButtonDisplay();
		leftCol.getChildren().remove(userDInput);
		leftCol.getChildren().add(userDDisplay);
					
	}
	
	public void handleChangeToSongDisplay(ActionEvent e) {
		resetInputs();
		changeToDataDisplay();
	}
	
	public void resetInputs() {
		sizeInput.setText("");
		distanceInput.setText("");
	}
	
	public String getInputContent() {
		String distance = distanceInput.getText();
		
		String size = sizeInput.getText();
		
		String resolution = resolutionInput.getValue();
		
		return distance+ "cms " + size + "inches " + resolution;
	}
	
	public void editData(ActionEvent e) {
		// get scene
		Node node = (Node) e.getSource();
		Stage thisStage = (Stage) node.getScene().getWindow();

		// check if Inputs is empty
		if (distanceInput.getText().isBlank() || sizeInput.getText().isBlank() || resolutionInput.getValue()== null) {
			// alert lack of essential inputs
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("Missing Distance Input or size input");
			error.setContentText("Press OK to try again");

			error.showAndWait();
			return;
		} else {
			String distance = distanceInput.getText();
			
			String size = sizeInput.getText();
			
			boolean intError = false;
				try {
					int sizeCheck = Integer.parseInt(size);
					int distanceCheck = Integer.parseInt(distance);
					
					// negative number: invalid input for year
					if(sizeCheck <= 0 || distanceCheck <=0) 
						intError = true;
				} 
				// alphanumeric characters: invalid input
				catch (NumberFormatException nfe) {
					intError = true;
				}
			
			
			// show error alert for invalid input
			if(intError) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setHeaderText("Numeric input is invalid");
				error.setContentText("Press OK to try again");

				error.showAndWait();
				return;
			}

			// confirm edit with alert
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(thisStage);
			alert.setTitle("Set user data");
			alert.setHeaderText("Are you sure you want to edit user data?");

			String content = getInputContent();
			alert.setContentText(content);
			Optional<ButtonType> result = alert.showAndWait();

			// get alert result
			if (result.get() == ButtonType.OK) {
				
				distanceDisplay.setText(distance);
				sizeDisplay.setText(size);
				resolutionDisplay.setText(resolutionInput.getValue());

				// reset text boxes
				resetInputs();			
				changeToDataDisplay();
				}
			}
		}
	
	public void autoMode(ActionEvent e) {
		double maxDis;
		double minDis;
		int unoInput;
		
		minDis = Float.parseFloat(sizeDisplay.getText()) * 2.54 * 8 / 12.85;
		maxDis = Float.parseFloat(sizeDisplay.getText()) * 2.54 * 8 / 4.222;
		
	}
	
	public void retinaMode(ActionEvent e) {
		double maxDis;
		double minDis;
		int unoInput;
		
		minDis = Float.parseFloat(sizeDisplay.getText()) * 2.54 * 8 / 12.85;
		maxDis = Float.parseFloat(sizeDisplay.getText()) * 2.54 * 8 / 4.222;
		
	}
	
	}
