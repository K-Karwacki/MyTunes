package dk.easv.mytunes.pl.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

public class MediaPlayerModel {
  private MediaPlayer mediaPlayer;
  private Map<Playlist, ObservableList<Song>> library;
  private Playlist currentPlaylist;
  private Song currentSong;
  private int currentIndex;
  private Consumer<Song> onSongChange; // Callback for updating UI when a song changes
  private boolean isPaused = false;
  private boolean isMuted = false;
  private double volume;
  private ReadOnlyObjectWrapper<Duration> currentDuration;
  private ReadOnlyObjectWrapper<Duration> totalDuration;

  // Initialize media player model
  public MediaPlayerModel() {
    this.currentPlaylist = null;
    this.currentSong = null;
    this.currentIndex = 0;
    this.library = null;
    this.currentDuration = new ReadOnlyObjectWrapper<>(Duration.ZERO);
    this.totalDuration = new ReadOnlyObjectWrapper<>(Duration.UNKNOWN);
  }

  public void setLibrary(Map<Playlist, ObservableList<Song>> library) {
    this.library = library;
  }

  public void playCurrentSong() {
    if (currentSong == null || currentPlaylist == null || library == null || currentIndex < 0) {
      return; // Nothing to play
    }

    stop(); // Stop any current playback

    String songPath = library.get(currentPlaylist).get(currentIndex).getFilePath();
    Media media = new Media(new File(songPath).toURI().toString());
    mediaPlayer = new MediaPlayer(media);

    if(isMuted){
      mediaPlayer.setMute(true);
    }

    mediaPlayer.setOnReady(()->{
      mediaPlayer.setVolume(volume);
      currentDuration.set(mediaPlayer.getCurrentTime());
      totalDuration.set(mediaPlayer.getTotalDuration());
      System.out.println(mediaPlayer.getTotalDuration().toSeconds());
      isPaused = false;
      notifySongChange();
    });

    // Track current playback time
    mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
      currentDuration.set(newTime != null ? newTime : Duration.ZERO);
    });

    mediaPlayer.setOnEndOfMedia(this::playNext); // Play the next song when the current one ends
    mediaPlayer.play();
  }

  public void playNext() {
    currentIndex = getNextIndex();// Wrap around to the first song
    playCurrentSong();
  }

  public void playPrevious() {
    currentIndex = getPrevIndex();// Wrap around to the last song
    playCurrentSong();
    isPaused = false;
  }

  public void pause() {
    if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
      mediaPlayer.pause();
      isPaused = true;
    }
  }

  public void resume() {
    if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
      mediaPlayer.play();
      isPaused = false;
    }
  }

  public void stop() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      mediaPlayer.dispose();
      mediaPlayer = null;
      isPaused = false;
    }
  }

  public boolean isPaused()
  {
    return isPaused;
  }

  public void setOnSongChange(Consumer<Song> onSongChange) {
    this.onSongChange = onSongChange;
  }

  private void notifySongChange() {
    if (onSongChange != null && library != null) {
      onSongChange.accept(library.get(currentPlaylist).get(currentIndex));
    }
  }


  public void setCurrentSongIndex(int index) {
    currentIndex = index;
    if(library != null){
      currentSong = library.get(currentPlaylist).get(index);
    }
  }

  public void setCurrentPlaylist(Playlist playlist) {
    currentPlaylist = playlist;
  }

  public Playlist getCurrentPlaylist() {
    return currentPlaylist;
  }

  public int getNextIndex(){
    if(library != null && currentPlaylist != null){
      return (currentIndex + 1) % library.get(currentPlaylist).size();
    }
    return -1;
  }

  public int getPrevIndex(){
    if(library != null && currentPlaylist != null){
      return (currentIndex - 1 + library.get(currentPlaylist).size()) % library.get(currentPlaylist).size();
    }
    return -1;
  }

  public int getCurrentIndex() {
    return currentIndex;
  }

  public Song getCurrentSong() {
    if(library.get(currentPlaylist) != null){
      return library.get(currentPlaylist).get(currentIndex);
    }
    return null;
  }

  public Song getPreviousSong() {
    if (library.get(currentPlaylist).isEmpty()) {
      return null;
    }
    int previousIndex = (currentIndex - 1 + library.get(currentPlaylist).size()) % library.get(currentPlaylist).size();
    return library.get(currentPlaylist).get(previousIndex);
  }

  public Song getNextSong() {
    if (library.get(currentPlaylist).isEmpty()) {
      return null;
    }
    int nextIndex = getNextIndex();
    return library.get(currentPlaylist).get(nextIndex);
  }

  public void mute(){
    isMuted = true;
    if(mediaPlayer != null){
      mediaPlayer.setMute(true);
    }
  }

  public void unMute(){
    isMuted = false;
    if(mediaPlayer != null){
      mediaPlayer.setMute(false);
    }
  }

  public boolean isMute() {
    if(mediaPlayer != null){
      return mediaPlayer.isMute();
    }
    return isMuted;
  }

  public void setVolume(double volume){
    if(mediaPlayer != null){
      mediaPlayer.setVolume(volume);
//      System.out.println(mediaPlayer.volumeProperty().set);
    }
    this.volume = volume;
  }

  public ReadOnlyObjectProperty<Duration> getCurrentDurationProperty(){
    return currentDuration.getReadOnlyProperty();
  }

  public ReadOnlyObjectProperty<Duration> getTotalDurationProperty(){
    return totalDuration.getReadOnlyProperty();
  }

  public Duration getCurrentDuration(){
    return currentDuration.get();
  }

  public Duration getTotalDuration(){
    return totalDuration.get();
  }

  public void seek(Duration duration){
    mediaPlayer.seek(duration);
  }

}