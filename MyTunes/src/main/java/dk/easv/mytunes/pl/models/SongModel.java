package dk.easv.mytunes.pl.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SongModel
{
  private final SongManager songManager;
  private final ObservableList<Song> allSongs = FXCollections.observableArrayList();

  public SongModel(){
    songManager = new SongManager();
    allSongs.setAll(songManager.getAllSongs());
  }

  public ObservableList<Song> getAllSongs()
  {
    return allSongs;
  }

  public List<Song> getSongsForPlaylist(Playlist playlist){
    return songManager.getSongsForPlaylist(playlist);
  }

  public void addNew(Song song){
    songManager.addSong(song);
    allSongs.add(song);
  }

  public void removeSong(Song selectedItem) {
    allSongs.remove(selectedItem);
    songManager.deleteSong(selectedItem);
  }
}
