package dk.easv.mytunes.dal.db.crud.entities;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.db.crud.CrudDao;
import dk.easv.mytunes.dal.utils.SongRowMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class SongDaoDB extends CrudDao<Song> implements SongDao
{
  private final SongRowMapper songRowMapper;
  public SongDaoDB() {
    super(Song.class);
    songRowMapper = new SongRowMapper();
  }


  @Override public List<Song> getSongsByPlaylistId(int playlistId) throws SQLException
  {
    String query = "SELECT song.id, song.title, song.artist, song.category, song.duration, song.path FROM playlist_song ps INNER JOIN song ON ps.Fk_song = song.id WHERE ps.FK_playlist = ?;";
    return this.select(query, new Object[]{playlistId}, songRowMapper);
  }

  @Override public List<Song> getAllSongs() throws SQLException
  {
    return this.select("Select * from song", new Object[]{}, songRowMapper);
  }

  @Override public Song createReturnSong(Song song) throws SQLException
  {
    String encodedPath = URLEncoder.encode(song.getFilePath(),
        StandardCharsets.UTF_8);
      return this.insertReturn("insert into song (title, artist, category, duration,path) values (?, ?, ?, ?, ?);", "select * from song where id = ?",new Object[]{song.getTitle(), song.getArtist(), song.getCategory(), song.getDuration(), encodedPath}, songRowMapper);
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