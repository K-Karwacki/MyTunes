package dk.easv.mytunes.pl.controllers;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.pl.models.PlaylistModel;
import dk.easv.mytunes.pl.models.SongModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
import java.net.URL;
import java.util.*;

public class MainSceneController implements Initializable
{
  private final SongModel songModel = new SongModel();
  private final PlaylistModel playlistModel = new PlaylistModel(songModel);
  public static final List<String> SONG_CATEGORIES = Arrays.asList(
      "Pop",
      "Rock",
      "Hip-Hop",
      "R&B",
      "Electronic",
      "Metal",
      "Rap",
      "Reggae",
      "Classical",
      "Jazz",
      "Country"
  );

  @FXML BorderPane mainPane;
  @FXML ListView<Song> songListView;
  @FXML ListView<Playlist> playlistListView;
  @FXML ListView<Song> songOnPlaylistListView;


  @Override public void initialize(URL location, ResourceBundle resources)
  {
    songListView.setItems(songModel.getAllSongs());
    playlistListView.setItems(playlistModel.getPlaylists());


    playlistListView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<Playlist>()
        {
          @Override public void changed(
              ObservableValue<? extends Playlist> observable, Playlist oldValue,
              Playlist newValue)
          {
            if(newValue != null){
              songOnPlaylistListView.setItems(playlistModel.getSongsForPlaylist(newValue));
            }
          }
        });
  }



  @FXML private void onClickOpenCreatePlaylist(ActionEvent actionEvent) {
    showCreatePlaylistDialog();
  }

  @FXML private void onClickOpenEditPlaylist(ActionEvent actionEvent) {
  }

  @FXML private void onClickRemoveSongFromPlaylist(ActionEvent actionEvent) {
    Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
    Song selectedSong = songOnPlaylistListView.getSelectionModel().getSelectedItem();
    if(selectedSong == null || selectedPlaylist == null){
      showAlert(Alert.AlertType.ERROR, "Select song and playlist.");
    }
    playlistModel.removeSongFromPlaylist(selectedSong, selectedPlaylist);
    refreshPlaylistListView();
  }
  @FXML private void onClickRemovePlaylist(ActionEvent actionEvent) {
    Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
    if(selectedPlaylist == null){
      showAlert(Alert.AlertType.ERROR, "Select playlist to remove.");
    }
    playlistModel.removePlaylist(selectedPlaylist);
  }

  @FXML private void onClickAddSongToPlaylist(ActionEvent actionEvent) {
    Song selectedSong = songListView.getSelectionModel().getSelectedItem();
    Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
    if(selectedSong == null || selectedPlaylist == null){
      showAlert(Alert.AlertType.ERROR, "Select playlist and song.");
      return;
    }
    playlistModel.addSongToPlaylist(selectedSong, selectedPlaylist);
    refreshPlaylistListView();

  }

  @FXML private void onClickRemoveSelectedSong(ActionEvent actionEvent) {
    Song selectedSong = songListView.getSelectionModel().getSelectedItem();
    if(selectedSong == null){
      showAlert(Alert.AlertType.ERROR, "Select song to remove.");
      return;
    }
    playlistModel.removeSongFromAllPlaylists(selectedSong);
    songModel.removeSong(selectedSong);
    refreshPlaylistListView();
  }

  @FXML private void onClickOpenAddNewSong(ActionEvent actionEvent) {
    showCreateSongDialog();
  }


  private void showCreateSongDialog(){
    // Create the labels and input fields
    Label titleLabel = new Label("Title:");
    TextField titleField = new TextField();

    Label artistLabel = new Label("Artist:");
    TextField artistField = new TextField();

    Label categoryLabel = new Label("Category:");
    ComboBox<String> categoryComboBox = new ComboBox<>();
    categoryComboBox.getItems().addAll(SONG_CATEGORIES);

    Label durationLabel = new Label("Duration:");
    TextField durationField = new TextField();
    durationField.setEditable(false);

    Label fileLabel = new Label("File:");
    TextField fileField = new TextField();
    fileField.setEditable(false);
    Button browseButton = new Button("Browse...");

    // Set up the file chooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

    //Choose file handling
    browseButton.setOnAction(event -> {
      File file = fileChooser.showOpenDialog(new Stage());
      if (file != null) {
        fileField.setText(file.getAbsolutePath());
        try (FileInputStream inputStream = new FileInputStream(file)) {
          // Parse MP3 metadata
          Metadata metadata = new Metadata();
          Mp3Parser mp3Parser = new Mp3Parser();
          mp3Parser.parse(inputStream, new DefaultHandler(), metadata, new ParseContext());

          // Extract duration in seconds
          String durationStr = metadata.get("xmpDM:duration");
          if (durationStr != null) {
            double durationInSeconds = Double.parseDouble(durationStr) / 1000.0;
            int minutes = (int) (durationInSeconds / 60);
            int seconds = (int) (durationInSeconds % 60);
            //            durationLabel.setText(String.format("Duration: %d:%02d", minutes, seconds));
            durationField.setText(minutes + ":" + seconds);
          } else {
            durationField.setText("");
          }
        } catch (Exception e) {
          durationLabel.setText("Error reading MP3 duration.");
          e.printStackTrace();
        }
      }
    });

    // Create a submit button
    Button submitButton = new Button("Submit");

    //Submit handling
    submitButton.setOnAction(event -> {
      String title = titleField.getText();
      String artist = artistField.getText();
      String category = categoryComboBox.getSelectionModel().getSelectedItem();
      String duration = durationField.getText();
      String filePath = fileField.getText();

      if(title.isEmpty()){
        showAlert(Alert.AlertType.ERROR,"Title cannot be empty!");
        return;
      }
      if(artist.isEmpty()){
        showAlert(Alert.AlertType.ERROR,"Artist cannot be empty!");
        return;
      }
      if(category.isEmpty()){
        showAlert(Alert.AlertType.ERROR,"Category cannot be empty!");
        return;
      }
      if(filePath.isEmpty()){
        showAlert(Alert.AlertType.ERROR,"Title cannot be empty!");
        return;
      }

      //Try ro add a new song to database
      songModel.addNew(new Song(title, artist, category, filePath.toLowerCase(
          Locale.ROOT), duration));
      Stage stageToClose = (Stage) submitButton.getScene().getWindow();
      stageToClose.close();
      showAlert(Alert.AlertType.CONFIRMATION, "Successfully created a new song and saved to database.");
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

  private void showCreatePlaylistDialog(){
    Label nameLabel = new Label("Playlist name:");
    TextField nameField = new TextField();

    VBox vBox = new VBox(10);
    Text text = new Text("Create new playlist");
    text.setFont(new Font(22));

    Button submitButton = new Button("Submit");


    //Submit handling
    submitButton.setOnAction(event -> {
      String playlistName = nameField.getText();
      System.out.println(playlistName);
      if(!playlistName.isEmpty()){
        try {
          playlistModel.createPlaylist(new Playlist(playlistName));
        }catch (Exception e){
          e.printStackTrace();
        }
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

  private void showAlert(Alert.AlertType alertType,String message) {
    // Show an alert dialog with the error message
    Alert alert = new Alert(alertType);
    //    alert.setTitle("");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void refreshPlaylistListView(){
    playlistListView.setCellFactory(param -> new ListCell<>()
    {
      @Override protected void updateItem(Playlist item, boolean empty)
      {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        }
        else {
          setText(item.toString());
          setOnMouseClicked(event -> {
            System.out.println("Mouse clicked on: " + item);
          });
        }
      }
    });
  }

}
