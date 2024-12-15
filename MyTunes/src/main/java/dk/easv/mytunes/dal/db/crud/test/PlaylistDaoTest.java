package dk.easv.mytunes.dal.db.crud.test;

import com.sun.java.accessibility.util.GUIInitializedListener;
import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.db.crud.entities.PlaylistDao;
import dk.easv.mytunes.pl.GuiComponents;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class PlaylistDaoTest implements PlaylistDao
{
  @Override public void deletePlaylist(Playlist playlist) throws SQLException
  {
    System.out.println("called dao");

  }

  @Override public void updatePlaylistName(Playlist playlist, String newName) throws SQLException
  {
    System.out.println("called dao");

  }

  @Override public List<Playlist> getAllPlaylists() throws SQLException
  {
    System.out.println("called dao get all playlists");

    return null;
  }

  @Override public void deleteSongFromPlaylist(Song song, Playlist playlist)
      throws SQLException
  {
    System.out.println("called dao");

  }

  @Override public void addSongToPlaylist(Song song, Playlist playlist)
      throws SQLException
  {
    System.out.println("called dao");

  }


  @Override public Playlist insertReturnPlaylist(Playlist playlist)
      throws SQLException
  {
    return null;
  }
}
