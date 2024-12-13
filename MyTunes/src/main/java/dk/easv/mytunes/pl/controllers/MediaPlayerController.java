package dk.easv.mytunes.pl.controllers;

import dk.easv.mytunes.MyTunes;
import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.pl.models.MediaPlayerModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MediaPlayerController implements Initializable
{
  private final MediaPlayerModel mediaPlayerModel;

  @FXML Label currentSongLabel;
  @FXML Label currentArtistLabel;
  @FXML Label nextSongLbl;
  @FXML Label playlistLbl;
  @FXML Button playPauseBtn;
  @FXML ImageView playPauseImageView;
  @FXML ImageView muteUnMuteImageView;
  @FXML Slider volumeSlider;
  @FXML Slider timeSlider;
  @FXML Label totalTimeLabel;
  @FXML Label currentTimeLabel;
  @FXML ListView<Playlist> playlistListView;
  @FXML ListView<Song> songsOnPlaylistListView;

  private final Image playImage = new Image(
      String.valueOf(MyTunes.class.getResource("images/play.png")));

  private final Image pauseImage = new Image(
      String.valueOf(MyTunes.class.getResource("images/pause_button.png")));

  private final Image muteImage = new Image(
      String.valueOf(MyTunes.class.getResource("images/volume.png")));

  private final Image unMuteImage = new Image(
      String.valueOf(MyTunes.class.getResource("images/volume-mute.png")));

  // Initialize media player
  public MediaPlayerController(){
    mediaPlayerModel = new MediaPlayerModel();

    // Set on song change callback
    mediaPlayerModel.setOnSongChange(song -> {
      if(timeSlider.isDisable()){
        timeSlider.setDisable(false);
      }
      timeSlider.setMax(mediaPlayerModel.getTotalDuration().toSeconds());
      currentSongLabel.setText(song.getTitle());
      currentArtistLabel.setText(song.getArtist());
      playlistLbl.setText("Playlist: " + mediaPlayerModel.getCurrentPlaylist().getName());
      nextSongLbl.setText("Next: "+ mediaPlayerModel.getNextSong().getTitle() + " - " + mediaPlayerModel.getNextSong().getArtist());
      totalTimeLabel.setText(String.format("%02d:%02d", (int)mediaPlayerModel.getTotalDuration().toMinutes(), (int)mediaPlayerModel.getTotalDuration().toSeconds()%60));

      mediaPlayerModel.getCurrentDurationProperty().addListener((observable, oldValue, newValue) -> {
        if(!timeSlider.isValueChanging()){
          timeSlider.setValue(newValue.toSeconds());
          currentTimeLabel.setText(String.format("%02d:%02d", (int)mediaPlayerModel.getCurrentDuration().toMinutes(), (int)mediaPlayerModel.getCurrentDuration().toSeconds()%60));
        }
      });
    });
  }


  @Override public void initialize(URL location, ResourceBundle resources)
  {
    timeSlider.setDisable(true);

    mediaPlayerModel.setVolume(volumeSlider.getValue());
    volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      mediaPlayerModel.setVolume(Math.pow(newValue.doubleValue(), 2));
    });

    timeSlider.valueChangingProperty().addListener(((observable, oldValue, newValue) -> {
      if(!newValue){
        mediaPlayerModel.seek(Duration.seconds(timeSlider.getValue()));
      }
    }));


  }

  // Set dependencies
  public void setSongsOnPlaylistListView(ListView<Song> songsListView) {
    this.songsOnPlaylistListView = songsListView;
    songsOnPlaylistListView.setOnMouseClicked(event -> { // Add on double click event on song listview from selected playlist
      if (event.getClickCount()==2 && songsOnPlaylistListView.getSelectionModel().getSelectedItem() != null){
        Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
        if(mediaPlayerModel.getCurrentPlaylist() != selectedPlaylist){ // If song is selected from different playlist switch playlists
          mediaPlayerModel.setCurrentPlaylist(selectedPlaylist);
        }
        mediaPlayerModel.setCurrentSongIndex(songsOnPlaylistListView.getSelectionModel().getSelectedIndex()); // Sets current song index
        playPauseImageView.setImage(pauseImage);
        mediaPlayerModel.playCurrentSong(); // Plays song
      }
    });
  }

  public void setLibrary(Map<Playlist, ObservableList<Song>> library){
    mediaPlayerModel.setLibrary(library);
  }

  public void setPlaylistListView(ListView<Playlist> playlistListView){
    this.playlistListView = playlistListView;
  }

  @FXML private void onClickPlayAndPauseSong(){
    if(mediaPlayerModel.getCurrentSong() != null){
      if(!mediaPlayerModel.isPaused()){
        mediaPlayerModel.pause();
        playPauseImageView.setImage(playImage);
      }else{
        mediaPlayerModel.resume();
        playPauseImageView.setImage(pauseImage);
      }
    }
  }

  @FXML private void onClickPlayNextSong(){
    mediaPlayerModel.playNext();
  }

  @FXML private void onClickPlayPrevSong(){
    mediaPlayerModel.playPrevious();
  }

  @FXML private void onClickMuteUnMute(){
    if(!mediaPlayerModel.isMute()){
      mediaPlayerModel.mute();
      muteUnMuteImageView.setImage(unMuteImage);
    }else{
      mediaPlayerModel.unMute();
      muteUnMuteImageView.setImage(muteImage);
    }
  }


}
