package dk.easv.mytunes.dal.interfaces;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;

import java.sql.SQLException;
import java.util.List;

public interface PlaylistDao
{
  Playlist getPlaylistById(int id) throws SQLException;
  void createPlaylist(Playlist playlist) throws SQLException;
  void deletePlaylist(Playlist playlist) throws SQLException;
  void updatePlaylist(Playlist playlist) throws SQLException;
  List<Playlist> getAllPlaylists() throws SQLException;
  void updateNameById(int id, String name) throws SQLException;
  void deleteSongFromPlaylist(Song song, Playlist playlist) throws SQLException;
  void addSongToPlaylist(Song song, Playlist playlist) throws SQLException;
}
