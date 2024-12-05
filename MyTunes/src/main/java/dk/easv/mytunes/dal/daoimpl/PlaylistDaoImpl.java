package dk.easv.mytunes.dal.daoimpl;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.interfaces.GenericDao;
import dk.easv.mytunes.dal.interfaces.PlaylistDao;
import dk.easv.mytunes.dal.interfaces.RowMapper;
import dk.easv.mytunes.dal.utils.PlaylistRowMapper;

import java.sql.SQLException;
import java.util.List;

public class PlaylistDaoImpl extends GenericDao<Playlist>
    implements PlaylistDao
{
  private final RowMapper<Playlist> playlistRowMapper;


  public PlaylistDaoImpl() {
    super(Playlist.class);
    playlistRowMapper = new PlaylistRowMapper();
  }

  @Override public void createPlaylist(Playlist playlist) throws SQLException {
    this.insert("INSERT INTO playlist (name) VALUES (?)", new Object[]{playlist.getName()});
  }


  @Override public List<Playlist> getAllPlaylists() throws SQLException {
    return this.executeQuery("SELECT * FROM playlist", new Object[]{}, playlistRowMapper);
  }


  @Override public void updatePlaylist(Playlist p) throws SQLException {
    this.update("Update playlist set name = ? where id = ?", new Object[]{p.getName(), p.getId()});
  }


  @Override public void deletePlaylist(Playlist playlist) throws SQLException {
    this.delete("Delete from playlist where id = ?", new Object[]{playlist.getId()});
    this.delete("Delete from playlist_song where FK_playlist = ?", new Object[] {playlist.getId()});
  }


  @Override public Playlist getPlaylistById(int id) throws SQLException {
    return this.executeQuery("Select * from playlist where id=?", new Object[]{id}, playlistRowMapper).getFirst();
  }


  @Override public void updateNameById(int id, String newName) throws SQLException {
    this.update("update playlist set name = ? where id = ?", new Object[]{newName, id});
  }


  @Override public void deleteSongFromPlaylist(Song song, Playlist playlist) throws SQLException {
    this.delete("Delete from playlist_song where FK_song = ? AND FK_playlist = ?;", new Object[]{song.getId(), playlist.getId()});
  }


  @Override public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException {
    System.out.println("fired insert");
    this.insert("insert into playlist_song (FK_song, FK_playlist) values (?,?)", new Object[]{song.getId(), playlist.getId()});
  }
}
