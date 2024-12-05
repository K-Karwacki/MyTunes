package dk.easv.mytunes.pl.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistModel {
  private final PlaylistManager playlistManager;
  private final SongModel songModel;

  private final Map<Playlist, ObservableList<Song>> playlistSongs;
  private final ObservableList<Playlist> playlists;

  public PlaylistModel(SongModel songModel){
    this.songModel = songModel;
    playlistManager = new PlaylistManager();
    playlistSongs = new HashMap<>();
    playlists = FXCollections.observableArrayList();

    populatePlaylists();
  }

  private void populatePlaylists(){
    playlists.setAll(playlistManager.getAllPlaylists());
    for (Playlist playlist : playlists) {
      List<Song> songsOnPlaylist = songModel.getSongsForPlaylist(playlist);
      playlist.addAllSongs(songsOnPlaylist);
      ObservableList<Song> observableList = FXCollections.observableArrayList();
      observableList.setAll(songsOnPlaylist);
      playlistSongs.put(playlist, observableList);
    }
  }

  public ObservableList<Playlist> getPlaylists() {
    return playlists;
  }

  public ObservableList<Song> getSongsForPlaylist(Playlist playlist){
    return playlistSongs.get(playlist);
  }

  public void removePlaylist(Playlist playlistToRemove){
    playlists.remove(playlistToRemove);
    playlistManager.deletePlaylist(playlistToRemove);
    playlistSongs.remove(playlistToRemove);
  }

  public void createPlaylist(Playlist newPlaylist){
    playlists.add(newPlaylist);
    playlistManager.insertNewPlaylist(newPlaylist);
    ObservableList<Song> observableList = FXCollections.observableArrayList();
    playlistSongs.put(newPlaylist, observableList);
  }

  public void removeSongFromPlaylist(Song song, Playlist playlist){
    playlist.removeSong(song);
    playlistSongs.get(playlist).remove(song);
    playlistManager.deleteSongFromPlaylist(song, playlist);
  }

  public void removeSongFromAllPlaylists(Song song){
    System.out.println(song);
    for (Playlist playlist : playlists) {
      playlist.removeSong(song);
      playlistSongs.get(playlist).remove(song);
      playlistManager.deleteSongFromPlaylist(song, playlist);
    }
  }

  public void addSongToPlaylist(Song selectedSong, Playlist selectedPlaylist) {
    selectedPlaylist.addSong(selectedSong);
    if(!playlistSongs.get(selectedPlaylist).contains(selectedSong)){
      playlistSongs.get(selectedPlaylist).add(selectedSong);
      playlistManager.addSongToPlaylist(selectedSong, selectedPlaylist);
    }
  }
}
