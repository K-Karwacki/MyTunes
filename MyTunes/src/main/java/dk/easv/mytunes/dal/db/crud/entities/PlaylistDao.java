package dk.easv.mytunes.dal.db.crud.entities;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;

import java.sql.SQLException;
import java.util.List;

public interface PlaylistDao
{
  void deletePlaylist(Playlist playlist) throws SQLException;
  void updatePlaylistName(Playlist playlist, String newName) throws SQLException;
  List<Playlist> getAllPlaylists() throws SQLException;
  void deleteSongFromPlaylist(Song song, Playlist playlist) throws SQLException;
  void addSongToPlaylist(Song song, Playlist playlist) throws SQLException;
  Playlist insertReturnPlaylist(Playlist playlist) throws SQLException;
}
