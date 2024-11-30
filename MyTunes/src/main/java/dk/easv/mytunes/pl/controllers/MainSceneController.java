package dk.easv.mytunes.pl.controllers;

import dk.easv.mytunes.MyTunes;
import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MainException;
import dk.easv.mytunes.pl.models.PlayerModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.Position;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainSceneController implements Initializable
{
  private final PlayerModel playerModel = new PlayerModel();
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

  @Override public void initialize(URL location, ResourceBundle resources)
  {
    songListView.getChildrenUnmodifiable().forEach(node -> {
      ListCell<Song> n = (ListCell<Song>) node;
    });
//    songListView.setItems(playerModel.getSongObservableList());
//    playlistListView.setItems(playerModel.getPlaylistObservableList());
//    playlistListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }


  @FXML private void onClickOpenCreatePlaylist(ActionEvent actionEvent) {
//    TextField inputPlaylistName = new TextField();
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

    Label fileLabel = new Label("File:");
    TextField fileField = new TextField();
    fileField.setEditable(false);
    Button browseButton = new Button("Browse...");

    // Set up the file chooser
    FileChooser fileChooser = new FileChooser();
    browseButton.setOnAction(event -> {
      File file = fileChooser.showOpenDialog(new Stage());
      if (file != null) {
        fileField.setText(file.getAbsolutePath());
      }
    });

    // Create a submit button
    Button submitButton = new Button("Submit");

    //Submit handling
    submitButton.setOnAction(event -> {
      String title = titleField.getText();
      String artist = artistField.getText();
      String category = categoryComboBox.getSelectionModel().getSelectedItem();
      String filePath = fileField.getText();

      if(title.isEmpty()){
        showError("Title cannot be empty!");
        return;
      }
      if(artist.isEmpty()){
        showError("Artist cannot be empty!");
        return;
      }
      if(category.isEmpty()){
        showError("Category cannot be empty!");
        return;
      }
      if(filePath.isEmpty()){
        showError("Title cannot be empty!");
        return;
      }
      // Handle the input (for now, just print it)
      System.out.println("Title: " + title);
      System.out.println("Artist: " + artist);
      System.out.println("Category: " + category);
      System.out.println("File: " + filePath);
    });

    VBox vBox = new VBox(10);
//    vBox.setAlignment(Pos.CENTER);
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
    grid.add(fileLabel, 0, 3);
    grid.add(fileField, 1, 3);
    grid.add(browseButton, 2, 3);
    grid.add(submitButton, 1, 4);


    vBox.getChildren().add(text);
    vBox.getChildren().add(grid);
    // Set up the scene and stage
    Scene scene = new Scene(vBox, 400, 250);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
  }

  private void showError(String message) {
    // Show an alert dialog with the error message
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Input Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

}
