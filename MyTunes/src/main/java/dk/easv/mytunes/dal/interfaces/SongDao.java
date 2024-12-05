package dk.easv.mytunes.dal.interfaces;

import dk.easv.mytunes.be.Song;
import java.sql.SQLException;
import java.util.List;

public interface SongDao
{
  void createSong(Song song) throws SQLException;
  void deleteSong(Song song) throws SQLException;
  void updateSong(Song song) throws SQLException;
  List<Song> getSongsByPlaylistId(int playlistId) throws SQLException;
  List<Song> getAllSongs() throws SQLException;

}
