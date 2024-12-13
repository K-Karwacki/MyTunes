package dk.easv.mytunes.pl.controllers;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.pl.GuiComponents;
import dk.easv.mytunes.pl.models.LibraryModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibraryController implements Initializable
{
  private final LibraryModel libraryModel;
  @FXML private ListView<Song> songListView;
  @FXML private ListView<Playlist> playlistListView;
  @FXML private ListView<Song> songsOnPlaylistListView;
  @FXML private ComboBox<String> filterSongsComboBox;

  private Playlist playlistTemp;
  private FilteredList<Song> filteredSongListTemp;

  public LibraryController(){
    this.libraryModel = new LibraryModel();
  }

  @Override public void initialize(URL location, ResourceBundle resources)
  {
    filterSongsComboBox.setValue("Category");
    filterSongsComboBox.getItems().setAll(LibraryModel.SONG_CATEGORIES);  // Populate filter ComboBox with categories
    // Set filtering on the filtered list when category change
    filterSongsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      filteredSongListTemp.setPredicate(song -> {
        if ("All".equals(newValue)) {
          return true;
        }
        return song.getCategory().equals(newValue);
      });
    });

    playlistListView.setItems(libraryModel.getPlaylists()); // Populate playlistListView
    // Set double-click event on playlist list view to change current song list
    playlistListView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2 && playlistListView.getSelectionModel().getSelectedItem() != null){
        playlistTemp = playlistListView.getSelectionModel().getSelectedItem();
        filteredSongListTemp = new FilteredList<>(libraryModel.getLibrary().get(playlistTemp));
        songsOnPlaylistListView.setItems(filteredSongListTemp);
      }
    });

    songsOnPlaylistListView.setItems(null);
  }

  @FXML private void onClickOpenCreatePlaylist(ActionEvent actionEvent) {
    GuiComponents.showCreateEditPlaylistDialog(null, (playlistName -> libraryModel.createPlaylist(new Playlist(playlistName))));
  }

  @FXML private void onClickOpenEditSelectedPlaylist(ActionEvent actionEvent) {
    Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
    if( selectedPlaylist == null){
      GuiComponents.showAlert(Alert.AlertType.ERROR, "Select playlist to edit.");
      return;
    }
    GuiComponents.showCreateEditPlaylistDialog(selectedPlaylist, newPlaylistName ->{
      selectedPlaylist.setName(newPlaylistName);
      libraryModel.updatePlaylistName(selectedPlaylist, newPlaylistName);
      playlistListView.refresh();
    });
  }

  @FXML private void onClickRemoveSelectedSongFromSelectedPlaylist(ActionEvent actionEvent) {
    Song selectedSongOnPlaylist = songsOnPlaylistListView.getSelectionModel().getSelectedItem();

    if(playlistTemp == null || selectedSongOnPlaylist == null){
      GuiComponents.showAlert(Alert.AlertType.ERROR, "Select song and playlist.");
      return;
    }
    if(!playlistTemp.isMain()){
      GuiComponents.showDeleteSongFromPlaylistConfirmationDialog(selectedSongOnPlaylist, playlistTemp, response->{
        if(response){
          libraryModel.removeSongFromPlaylist(selectedSongOnPlaylist, playlistTemp);
          playlistListView.refresh();
        }
      });
      return;
    }
    GuiComponents.showDeleteSongFromMainConfirmationDialog(selectedSongOnPlaylist, response->{
      if(response){
        libraryModel.removeSongFromLibrary(selectedSongOnPlaylist);
        playlistListView.refresh();
      }
    });
  }

  @FXML private void onClickRemoveSelectedPlaylist(ActionEvent actionEvent) {
    Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
    if(selectedPlaylist == null){
      GuiComponents.showAlert(Alert.AlertType.ERROR, "Select playlist to remove.");
      return;
    }
    if(selectedPlaylist.isMain()){
      GuiComponents.showAlert(Alert.AlertType.ERROR, "Cannot remove main playlist.");
      return;
    }
    libraryModel.removePlaylist(selectedPlaylist);
    songsOnPlaylistListView.setItems(null);
  }

  @FXML private void onClickAddSelectedSongToSelectedPlaylist(ActionEvent actionEvent) {
    Song selectedSong = songsOnPlaylistListView.getSelectionModel().getSelectedItem();
    System.out.println(selectedSong);
    Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
    System.out.println(selectedPlaylist);
    AtomicBoolean isOnThePlaylist = new AtomicBoolean(false);
    if(selectedSong == null || selectedPlaylist == null){
      GuiComponents.showAlert(Alert.AlertType.ERROR, "Select playlist and song.");
      return;
    }
    if(selectedPlaylist.getSongs().contains(selectedSong)){
      GuiComponents.showAlert(Alert.AlertType.ERROR, "Song is already on the playlist");
      return;
    }

    selectedPlaylist.getSongs().forEach(song -> {
      if(song.getId() == selectedSong.getId()){
        GuiComponents.showAlert(Alert.AlertType.ERROR, "Song is already on the playlist");
        isOnThePlaylist.set(true);
      }
    });

    if(isOnThePlaylist.get()){
      return;
    }
    libraryModel.addSongToPlaylistLibrary(selectedSong, selectedPlaylist);
    playlistListView.refresh();
  }

  @FXML private void onClickOpenImportSong(ActionEvent actionEvent) {
    GuiComponents.showImportEditSongDialog(null, song-> {
      libraryModel.importNewSong(new Song(song.get("title"), song.get("artist"), song.get("category"), song.get("path"), song.get("duration")));
      playlistListView.refresh();
    });
  }

  @FXML private void onClickOpenEditSelectedSong(){
    Song selectedSong = songsOnPlaylistListView.getSelectionModel().getSelectedItem();
    GuiComponents.showImportEditSongDialog(selectedSong, songData->{
      libraryModel.updateSong(selectedSong, songData);
      songsOnPlaylistListView.refresh();
    });
  }



  // Return dependencies needed for media player
  public ListView<Song> getSongOnPlaylistListView() {
    return songsOnPlaylistListView;
  }
  public ListView<Playlist> getPlaylistListView()
  {
    return playlistListView;
  }
  public Map<Playlist, ObservableList<Song>> getLibrary() {
    return libraryModel.getLibrary();
  }
}
