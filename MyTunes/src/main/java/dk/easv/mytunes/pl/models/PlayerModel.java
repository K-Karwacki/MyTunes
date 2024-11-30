package dk.easv.mytunes.pl.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.PlaylistManager;
import dk.easv.mytunes.bll.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerModel
{
  private final SongManager songManager = new SongManager();
  private final PlaylistManager playlistManager = new PlaylistManager();

  private final ObservableList<Playlist> playlistObservableList = FXCollections.observableArrayList();
  private final ObservableList<Song> songObservableList = FXCollections.observableArrayList();

  public PlayerModel(){
    List<Playlist> l = new ArrayList<>();
    l.add(new Playlist(1,"name 1"));
    playlistObservableList.setAll(l);
  }

  public ObservableList<Playlist> getPlaylistObservableList() {
    return playlistObservableList;
  }

  public ObservableList<Song> getSongObservableList() {
    return songObservableList;
  }

  public void addNewSong(Song song) throws SQLException
  {
      songManager.addNewSong(song);
  }

  public void removePlaylist(Playlist playlist)
  {
    playlistManager.deletePlaylist(playlist);
    playlistObservableList.remove(playlist);
  }

  public void createPlaylist(String name)
  {
    playlistManager.createPlaylist(name);
  }
}
