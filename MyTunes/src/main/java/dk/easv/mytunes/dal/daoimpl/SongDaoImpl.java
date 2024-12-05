package dk.easv.mytunes.dal.daoimpl;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.interfaces.GenericDao;
import dk.easv.mytunes.dal.interfaces.SongDao;
import dk.easv.mytunes.dal.utils.SongRowMapper;

import java.sql.SQLException;
import java.util.List;

public class SongDaoImpl extends GenericDao<Song> implements SongDao
{
  private final SongRowMapper songRowMapper;
  public SongDaoImpl() {
    super(Song.class);
    songRowMapper = new SongRowMapper();
  }


  @Override public List<Song> getSongsByPlaylistId(int playlistId) throws SQLException
  {
    String query = "SELECT song.id, song.title, song.artist, song.category, song.duration, song.path FROM playlist_song ps INNER JOIN song ON ps.Fk_song = song.id WHERE ps.FK_playlist = ?;";
    return this.executeQuery(query, new Object[]{playlistId}, songRowMapper);
  }

  @Override public List<Song> getAllSongs() throws SQLException
  {
    return this.executeQuery("Select * from song", new Object[]{}, songRowMapper);
  }


  @Override public void createSong(Song song) throws SQLException
  {
    this.insert("insert into song (title, artist, category, path, duration) values (?, ?, ?, ?, ?);", new Object[]{song.getTitle(), song.getArtist(), song.getCategory(), song.getDuration(), song.getFilePath()});
  }

  @Override public void deleteSong(Song song) throws SQLException
  {
    this.delete("delete from song where id = ?", new Object[]{song.getId()});
  }

  @Override public void updateSong(Song s) throws SQLException
  {
    this.update("Update song set title = ?, artist = ?, category = ?, path = ?, duration = ? where id = ?", new Object[]{s.getTitle(), s.getArtist(), s.getCategory(),s.getFilePath(),s.getDuration(),s.getId()});
  }
}