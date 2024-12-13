package dk.easv.mytunes.dal.db.crud.entities;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.db.crud.CrudDao;
import dk.easv.mytunes.dal.utils.RowMapper;
import dk.easv.mytunes.dal.utils.PlaylistRowMapper;

import java.sql.SQLException;
import java.util.List;

public class PlaylistDaoDB extends CrudDao<Playlist>
    implements PlaylistDao
{
  private final RowMapper<Playlist> playlistRowMapper;


  public PlaylistDaoDB() {
    super(Playlist.class);
    playlistRowMapper = new PlaylistRowMapper();
  }

  @Override public void createPlaylist(Playlist playlist) throws SQLException {
    this.insert("INSERT INTO playlist (name, is_main) VALUES (?, ?)", new Object[]{playlist.getName(), playlist.isMain()});
  }


  @Override public List<Playlist> getAllPlaylists() throws SQLException {
    return this.select("SELECT * FROM playlist", new Object[]{}, playlistRowMapper);
  }


  @Override public void updatePlaylistName(Playlist playlist, String newName) throws SQLException {
    this.update("Update playlist set name = ? where id = ?", new Object[]{newName, playlist.getId()});
  }


  @Override public void deletePlaylist(Playlist playlist) throws SQLException {
    this.delete("Delete from playlist where id = ?", new Object[]{playlist.getId()});
    this.delete("Delete from playlist_song where FK_playlist = ?", new Object[] {playlist.getId()});
  }


  @Override public Playlist getPlaylistById(int id) throws SQLException {
    return this.select("Select * from playlist where id=?", new Object[]{id}, playlistRowMapper).getFirst();
  }


  @Override public void deleteSongFromPlaylist(Song song, Playlist playlist) throws SQLException {
    this.delete("Delete from playlist_song where FK_song = ? AND FK_playlist = ?;", new Object[]{song.getId(), playlist.getId()});
  }


  @Override public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException {
    System.out.println("" +song.getId() +" "  + playlist.getId());
    this.insert("insert into playlist_song (FK_song, FK_playlist) values (?,?)", new Object[]{song.getId(), playlist.getId()});
  }

  @Override public Playlist getMainPlaylist() throws SQLException
  {
    List<Playlist> list = this.select("Select * from playlist where is_main=1", new Object[]{}, playlistRowMapper);
    if(list.size() == 1){
      return list.getFirst();
    }return null;
  }
}
