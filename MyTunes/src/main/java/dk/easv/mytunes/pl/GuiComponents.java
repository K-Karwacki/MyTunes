package dk.easv.mytunes.pl;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.pl.models.LibraryModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.function.Consumer;

public abstract class GuiComponents
{

  public static void showAlert(Alert.AlertType alertType,String message) {
    // Show an alert dialog with the error message
    Alert alert = new Alert(alertType);
    //    alert.setTitle("");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showCreateEditPlaylistDialog(Playlist playlistToEdit, Consumer<String> playlistNameToSubmit){
    VBox vBox = new VBox(10);

    Text text;
    Button submitButton;
    String CONFIRMATION_TEXT;

    Label nameLabel = new Label("Playlist name:");
    TextField nameField = new TextField();



    if(playlistToEdit != null){
      nameField.setText(playlistToEdit.getName());
      text = new Text("Update playlist: " + playlistToEdit.getName());
      submitButton = new Button("Update");
      CONFIRMATION_TEXT = "Successfully updated playlist";
    }else{
      nameField.setText("");
      text = new Text("Create new playlist");
      submitButton = new Button("Submit");
      CONFIRMATION_TEXT = "Successfully created new playlist";
    }

    text.setFont(new Font(22));

    //Submit handling
    submitButton.setOnAction(event -> {
      String playlistName = nameField.getText();
      if(!playlistName.isEmpty()){
        playlistNameToSubmit.accept(playlistName);
        Stage stageToClose = (Stage) submitButton.getScene().getWindow();
        stageToClose.close();
        showAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION_TEXT);
      }else{
        showAlert(Alert.AlertType.ERROR, "Name field cannot be empty.");
      }
    });

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10));
    grid.setHgap(10);
    grid.setVgap(10);

    grid.add(nameLabel, 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(submitButton, 0, 1);

    vBox.getChildren().addAll(text, grid);
    Scene scene = new Scene(vBox, 400, 250);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
  }

  public static void showImportEditSongDialog(Song songToEdit, Consumer<Map<String, String>> songMapToSubmit){
    // Create the labels and input fields
    Label titleLabel = new Label("Title:");
    TextField titleField = new TextField();

    Label artistLabel = new Label("Artist:");
    TextField artistField = new TextField();

    Label categoryLabel = new Label("Category:");
    ComboBox<String> categoryComboBox = new ComboBox<>();
    categoryComboBox.getItems().addAll(LibraryModel.SONG_CATEGORIES);

    Label durationLabel = new Label("Duration:");
    TextField durationField = new TextField();
    durationField.setEditable(false);
    durationField.setDisable(true);

    Label fileLabel = new Label("File:");
    TextField fileField = new TextField();
    fileField.setEditable(false);
    fileField.setDisable(true);
    Button browseButton = new Button("Browse...");
    Button submitButton;

    String CONFIRMATION_TEXT;
    // Set up the file chooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

    if(songToEdit != null){
      titleField.setText(songToEdit.getTitle());
      artistField.setText(songToEdit.getArtist());
      categoryComboBox.setValue(songToEdit.getCategory());
      durationField.setText(songToEdit.getDuration());
      fileField.setText(songToEdit.getFilePath());
      submitButton = new Button("Update");
      CONFIRMATION_TEXT = "Successfully updated song";
    }else{
      submitButton = new Button("Submit");
      CONFIRMATION_TEXT = "Successfully imported song";
    }

    //Choose file handling
    browseButton.setOnAction(event -> {
      File file = fileChooser.showOpenDialog(new Stage());
      if (file != null) {
        System.out.println(file.getName());
        String artist;
        String songTitle;

        String[] parts = file.getName().split(" - ");
        if(parts.length > 1){
          artist = parts[0];  // Artist Name
          songTitle = parts[1].replace(".mp3", "");    // Song Title
        }else{
          artist = "";
          songTitle = parts[0].replace(".mp3", "");
        }

        artistField.setText(artist);
        titleField.setText(songTitle);
        fileField.setText(file.getAbsolutePath());
        try (FileInputStream inputStream = new FileInputStream(file)) {
          // Parse MP3 metadata
          Metadata metadata = new Metadata();
          Mp3Parser mp3Parser = new Mp3Parser();
          mp3Parser.parse(inputStream, new DefaultHandler(), metadata, new ParseContext());

          // Extract duration in seconds
          String durationStr = metadata.get("xmpDM:duration");
          if (durationStr != null) {
            double durationInSeconds = Double.parseDouble(durationStr);
            System.out.println(durationInSeconds);
            int minutes = (int) (durationInSeconds / 60);
            int seconds = (int) (durationInSeconds % 60);
            String durationFormatted = String.format("%d:%02d", minutes, seconds);
            durationField.setText(durationFormatted);
          } else {
            durationField.setText("");
          }
        } catch (Exception e) {
          durationLabel.setText("Error reading MP3 duration.");
          e.printStackTrace();
        }
      }
    });

    //Submit handling
    submitButton.setOnAction(event -> {
      Map<String, String> songMap = new HashMap<>();
      String title = titleField.getText();
      String artist = artistField.getText();
      String category = categoryComboBox.getSelectionModel().getSelectedItem();
      String duration = durationField.getText();
      String filePath = fileField.getText();
      songMap.put("title", title);
      songMap.put("artist", artist);
      songMap.put("duration", duration);
      songMap.put("category", category);
      songMap.put("path", filePath);


      if(filePath.isEmpty()){
        showAlert(Alert.AlertType.ERROR,"Please select a file.");
        return;
      }
      if(title.isEmpty()){
        showAlert(Alert.AlertType.ERROR,"Please provide title for your song.");
        return;
      }
      if(artist.isEmpty()){
        showAlert(Alert.AlertType.ERROR,"Please provide an artist for your song.");
        return;
      }
      if(category == null){
        showAlert(Alert.AlertType.ERROR,"Please select category for your song.");
        return;
      }


      //Try ro add a new song to database
      songMapToSubmit.accept(songMap);

      Stage stageToClose = (Stage) submitButton.getScene().getWindow();
      stageToClose.close();
      showAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION_TEXT);
    });


    VBox vBox = new VBox(10);
    Text text = new Text("Add new song");
    text.setFont(new Font(22));

    // Arrange elements in a grid layout
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10));
    grid.setHgap(10);
    grid.setVgap(10);

    grid.add(titleLabel, 0, 0);
    grid.add(titleField, 1, 0);
    grid.add(artistLabel, 0, 1);
    grid.add(artistField, 1, 1);
    grid.add(categoryLabel, 0, 2);
    grid.add(categoryComboBox, 1, 2);
    grid.add(durationLabel, 0, 3);
    grid.add(durationField, 1, 3);
    grid.add(fileLabel, 0, 4);
    grid.add(fileField, 1, 4);
    grid.add(browseButton, 2, 4);
    grid.add(submitButton, 1, 5);


    vBox.getChildren().add(text);
    vBox.getChildren().add(grid);
    // Set up the scene and stage
    Scene scene = new Scene(vBox, 400, 250);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
  }

  public static void showDeleteSongFromMainConfirmationDialog(Song song, Consumer<Boolean> confirm) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("confirmation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete song: "+song.getTitle() + " - " + song.getArtist()+ " from the library? (This will delete song from all playlists)");

    // Customize button options
    ButtonType yesButton = new ButtonType("Yes");
    ButtonType noButton = new ButtonType("No");
    alert.getButtonTypes().setAll(yesButton, noButton);

    // Show dialog and wait for response
    Platform.runLater(() -> {
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == yesButton) {
        confirm.accept(true); // User confirmed
      } else {
        confirm.accept(false); // User declined
      }
    });
  }

  public static void showDeleteSongFromPlaylistConfirmationDialog(Song song, Playlist playlist, Consumer<Boolean> confirm) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("confirmation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete song: " +song.getTitle() + " - " + song.getArtist() + "\n" + " from playlist: " + playlist.getName());

    // Customize button options
    ButtonType yesButton = new ButtonType("Yes");
    ButtonType noButton = new ButtonType("No");
    alert.getButtonTypes().setAll(yesButton, noButton);

    // Show dialog and wait for response
    Platform.runLater(() -> {
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == yesButton) {
        confirm.accept(true); // User confirmed
      } else {
        confirm.accept(false); // User declined
      }
    });
  }
}
