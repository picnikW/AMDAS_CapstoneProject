// Jenny Xu, Yuxiang Wang
package ma.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Controller {
	@FXML
	HBox buttons;
	@FXML
	Button backBtn;
	@FXML
	Button addBtn;
	@FXML
	Button editBtn;
	@FXML
	Button deleteBtn;
	@FXML
	Button submitBtn;
	@FXML
	HBox labels;
	@FXML
	VBox rightCol;
	@FXML
	VBox displaySong;
	@FXML
	Label emptyListMessage;
	@FXML
	VBox displayLabels;
	@FXML
	Label titleDisplay;
	@FXML
	Label artistDisplay;
	@FXML
	Label yearDisplay;
	@FXML
	Label albumDisplay;
	@FXML
	VBox inputScreen;
	@FXML
	Label yearInputLabel;
	@FXML
	Label albumInputLabel;
	@FXML
	TextField titleInput;
	@FXML
	TextField artistInput;
	@FXML
	TextField yearInput;
	@FXML
	TextField albumInput;

	int DISPLAY_SCREEN = 0;
	int ADD_SCREEN = 1;
	int EDIT_SCREEN = 2;
	int screenId = DISPLAY_SCREEN;

	public void start(Stage mainStage) throws Exception {
		// load songs from csv file
		List<Song> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/songList.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// values in csv separated by '|'
				String[] values = line.split("\\|");

				// convert null string to null type
				for (int i = 0; i < values.length; i++) {
					if (values[i].equals("null"))
						values[i] = null;
				}

				Song entry = new Song(values[0], values[1], Integer.parseInt(values[2]), values[3]);
				records.add(entry);
			}
		}

		// sort songs list
		Collections.sort(records);

		// load songs into observable list
		obsList = FXCollections.observableArrayList(records);
		listView.setItems(obsList);
		listView.setCellFactory(param -> new ListCell<Song>() {
			{
				setStyle("-fx-padding: 10 0 10 0; -fx-margin: 0");
			}

			@Override
			protected void updateItem(Song item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setGraphic(null);
				} else {
					HBox listItem = new HBox();
					listItem.setPrefWidth(listView.getWidth());

					Label songText = new Label();
					songText.setText(item.getSong());
					songText.setStyle("-fx-padding: 0 0 0 15");
					songText.setPrefWidth(listItem.getPrefWidth() * 0.6);
					songText.setTextAlignment(TextAlignment.LEFT);

					Label artistText = new Label();
					artistText.setText(item.getArtist());
					artistText.setStyle("-fx-padding: 0 0 0 15");
					artistText.setPrefWidth(listItem.getPrefWidth() * 0.4);
					artistText.setTextAlignment(TextAlignment.LEFT);

					listItem.getChildren().addAll(songText, artistText);

					setGraphic(listItem);
				}
			}
		});

		// set listener for items
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showSong(mainStage));

		// select first item
		listView.getSelectionModel().select(0);
		
		// list is empty
		if (listView.getSelectionModel().getSelectedItem() == null) {
			editBtn.setDisable(true);
			deleteBtn.setDisable(true);

			displaySong.getChildren().remove(displayLabels);
		} else {
			displaySong.getChildren().remove(emptyListMessage);
		}

		rightCol.getChildren().remove(inputScreen);
		buttons.getChildren().remove(backBtn);
		buttons.getChildren().remove(submitBtn);
		// on close listener
		mainStage.setOnCloseRequest(event -> {
			// write song list to csv file
			try {
				BufferedWriter myWriter = new BufferedWriter(new FileWriter("src/songList.csv"));
				ObservableList<Song> list = listView.getItems();
				for (Song item : list)
					myWriter.write(item + "\n");

				myWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		});
	}
	
	public void showSong(Stage mainStage) {
		Song song = listView.getSelectionModel().getSelectedItem();
		if (song == null)
			return;
		else {
			if (!rightCol.getChildren().contains(displaySong)) {
				rightCol.getChildren().remove(inputScreen);
				rightCol.getChildren().add(displaySong);
				screenId = DISPLAY_SCREEN;
			}

			String year = song.getYear();
			if(year == null)
				year = "-";
			String album = song.getAlbum();
			if(album == null)
				album = "-";
			
			titleDisplay.setText(song.getSong());
			artistDisplay.setText(song.getArtist());
			yearDisplay.setText(year);
			albumDisplay.setText(album);
		}
	}

	public void toggleButtonDisplay() {
		if (buttons.getChildren().contains(backBtn)) {
			buttons.getChildren().remove(backBtn);
			buttons.getChildren().remove(submitBtn); 
			buttons.getChildren().add(addBtn);
			buttons.getChildren().add(editBtn);
			buttons.getChildren().add(deleteBtn);
		} else {
			buttons.getChildren().add(backBtn);
			buttons.getChildren().add(submitBtn); 
			buttons.getChildren().remove(addBtn);
			buttons.getChildren().remove(editBtn);
			buttons.getChildren().remove(deleteBtn);
		}
	}

	public void addDisplayScreen() {
		if (!rightCol.getChildren().contains(displaySong)) {
			rightCol.getChildren().add(displaySong);
			screenId = DISPLAY_SCREEN;
		}
	}

	public void addInputScreen() {
		if (!rightCol.getChildren().contains(inputScreen)) {
			rightCol.getChildren().add(inputScreen);
			screenId = DISPLAY_SCREEN;
		}
	}
	
	public void changeToSongDisplay() {
		toggleButtonDisplay();
		rightCol.getChildren().remove(inputScreen);
		addDisplayScreen();
		
		// enable list view selection
		listView.setMouseTransparent(false); 
		listView.setFocusTraversable(true);
	}

	public void handleChangeToSongDisplay(ActionEvent e) {
		resetInputs();
		changeToSongDisplay();
	}

	public void changeToAddDisplay(ActionEvent e) {
		toggleButtonDisplay();
		rightCol.getChildren().remove(displaySong);
		rightCol.getChildren().add(inputScreen);
		
		// indicate optional text fields
		yearInputLabel.setText("Year (optional):");
		albumInputLabel.setText("Album (optional):");
		
		// disable list view selection
		listView.setMouseTransparent(true); 
		listView.setFocusTraversable(false);
		screenId = ADD_SCREEN;
	}

	public void changeToEditDisplay(ActionEvent e) {
		toggleButtonDisplay();
		rightCol.getChildren().remove(displaySong);
		rightCol.getChildren().add(inputScreen);
		
		yearInputLabel.setText("Year:");
		albumInputLabel.setText("Album:");
		
		// get selected song
		Song song = listView.getSelectionModel().getSelectedItem();
		
		// set text boxes to selected song values
		titleInput.setText(song.getSong());
		artistInput.setText(song.getArtist());
		String year = song.getYear();
		if(year == null)
			year = "";
		String album = song.getAlbum();
		if(album == null)
			album = "";
		yearInput.setText(year);
		albumInput.setText(album);
		
		listView.setMouseTransparent(true); // Disable selection
		listView.setFocusTraversable(false);
		screenId = EDIT_SCREEN;
	}

	public void deleteSong(ActionEvent e) {
		// get selected song
		Song song = listView.getSelectionModel().getSelectedItem();
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();

		// get scene
		Node node = (Node) e.getSource();
		Stage thisStage = (Stage) node.getScene().getWindow();

		// confirm delete with alert
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(thisStage);
		alert.setTitle("Delete song");
		alert.setHeaderText("Are you sure you want to delete this song?");

		String content = song.getSong();
		alert.setContentText(content);
		alert.showAndWait();

		// get alert result
		if (alert.getResult() == ButtonType.OK) {
			listView.getItems().remove(song);

			song = listView.getSelectionModel().getSelectedItem();
			// check if song list empty
			if (song == null) {
				displaySong.getChildren().remove(displayLabels);
				displaySong.getChildren().add(emptyListMessage);
				
				editBtn.setDisable(true);
				deleteBtn.setDisable(true);
			}
			// select next song
			else
				listView.getSelectionModel().select(selectedIndex);
		}
	}

	public void handleSubmit(ActionEvent e) {
		if (screenId == ADD_SCREEN) {
			addSong(e);
		} else if (screenId == EDIT_SCREEN) {
			editSong(e);
		}
	}
	
	public void resetInputs() {
		titleInput.setText("");
		artistInput.setText("");
		yearInput.setText("");
		albumInput.setText("");
	}
	
	public String getInputContent() {
		String year = yearInput.getText();
		if(year.equals(""))
			year = "-";
		String album = albumInput.getText();
		if(album.equals(""))
			album = "-";
		
		return "Title: " + titleInput.getText() + "\n" +
				"Artist: " + artistInput.getText() + "\n" +
				"Year: " + year + "\n" + 
				"Album: " + album;
	}
	
	public void addSong(ActionEvent e) {
		// get scene
		Node node = (Node) e.getSource();
		Stage thisStage = (Stage) node.getScene().getWindow();

		// check if title and artist is empty
		if (titleInput.getText().isBlank() || artistInput.getText().isBlank()) {
			// alert lack of essential inputs
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("Missing Title or Artist");
			error.setContentText("Press OK to try again");

			error.showAndWait();
			return;
		} else {
			// create new song
			// And make the year and album optional
			String title = titleInput.getText();
			String artist = artistInput.getText();
			String yearString = yearInput.getText();
			int year = -1;
			boolean yearError = false;
			
			// try to convert year input to integer
			if(!yearString.equals("")) {
				try {
					year = Integer.parseInt(yearString);
					
					// negative number: invalid input for year
					if(year <= 0) 
						yearError = true;
				} 
				// alphanumeric characters: invalid input for year
				catch (NumberFormatException nfe) {
					yearError = true;
				}
			}
			
			// show error alert for year input
			if(yearError) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setHeaderText("Year input is invalid");
				error.setContentText("Press OK to try again");

				error.showAndWait();
				return;
			}

			String album = albumInput.getText();
			if (album.equals(""))
				album = null;

			Song newSong = new Song(title, artist, year, album);

			// check for duplicate songs
			if (listView.getItems().contains(newSong)) {
				// alert lack of essential inputs
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setHeaderText("Song with this name and artist already exists");
				error.setContentText("Press OK to try again");

				error.showAndWait();
				return;
			}

			// confirm add with alert
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(thisStage);
			alert.setTitle("Add song");
			alert.setHeaderText("Are you sure you want to add this song?");

			String content = getInputContent();
			alert.setContentText(content);
			Optional<ButtonType> result = alert.showAndWait();

			// get alert result
			if (result.get() == ButtonType.OK) {

				listView.getItems().add(newSong);
				// select newly added song
				listView.getSelectionModel().select(newSong);

				// sort list
				FXCollections.sort(obsList);

				// reset text boxes
				resetInputs();
				
				changeToSongDisplay();
				
				// enable items if list was previously empty
				if(displaySong.getChildren().contains(emptyListMessage)) {
					displaySong.getChildren().remove(emptyListMessage);
					displaySong.getChildren().add(displayLabels);
					
					editBtn.setDisable(false);
					deleteBtn.setDisable(false);
				}
			}
		}
	}

	public void editSong(ActionEvent e) {
		// get scene
		Node node = (Node) e.getSource();
		Stage thisStage = (Stage) node.getScene().getWindow();
		
		// get selected song
		Song song = listView.getSelectionModel().getSelectedItem();
		String title = titleInput.getText();
		String artist = artistInput.getText();
		String yearString = yearInput.getText();
		int year = -1;
		boolean yearError = false;
		
		// try to convert year input to integer
		if(!yearString.equals("")) {
			try {
				year = Integer.parseInt(yearString);
				
				// negative number: invalid input for year
				if(year <= 0) 
					yearError = true;
			} 
			// alphanumeric characters: invalid input for year
			catch (NumberFormatException nfe) {
				yearError = true;
			}
		}
		
		// show error alert for year input
		if(yearError) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("Year input is invalid");
			error.setContentText("Press OK to try again");

			error.showAndWait();
			return;
		}

		String album = albumInput.getText();
		if (album.equals(""))
			album = null;

		Song editedSong = new Song(title, artist, year, album);
		
		// check for duplicate songs if title or artist were edited
		if ((!song.getSong().equals(title) || !song.getArtist().equals(artist)) && listView.getItems().contains(editedSong)) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("Song with this name and artist already exists");
			error.setContentText("Press OK to try again");

			error.showAndWait();
			return;
		}

		/// confirm add with alert
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(thisStage);
		alert.setTitle("Edit song");
		alert.setHeaderText("Are you sure you want to save these changes?");

		String content = getInputContent();
		alert.setContentText(content);
		alert.showAndWait();

		// get alert result
		if (alert.getResult() == ButtonType.OK) {
			// change selected song values
			song.setSong(title);
			song.setArtist(artist);
			yearString = String.valueOf(year);
			song.setYear(yearString);
			song.setAlbum(album);
			
			// Sorting the list
			FXCollections.sort(obsList);
			
			// reset text inputs
			resetInputs();
			
			changeToSongDisplay();
		}
	}
}
