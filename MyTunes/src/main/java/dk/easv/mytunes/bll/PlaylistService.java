package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.db.crud.entities.PlaylistDao;
import dk.easv.mytunes.dal.db.crud.entities.PlaylistDaoDB;
import dk.easv.mytunes.dal.db.crud.test.PlaylistDaoTest;

import java.sql.SQLException;
import java.util.List;

public class PlaylistService
{
  private final PlaylistDao playlistDao;

  public PlaylistService(){
//    playlistDao = new PlaylistDaoDB();
    playlistDao = new PlaylistDaoTest();
  }

  public void deletePlaylist(Playlist playlist) {
    if(playlist!=null){
      try {
        playlistDao.deletePlaylist(playlist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public Playlist createNewPlaylist(Playlist newPlaylist) {
    if(newPlaylist != null){
      try{
        return playlistDao.insertReturnPlaylist(newPlaylist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
        e.printStackTrace();
      }
    }
    return null;
  }

  public List<Playlist> getAllPlaylists() {
    try{
      return playlistDao.getAllPlaylists();
    }catch (SQLException e){
      System.out.println("PlaylistDao failed. " + e.getMessage());
            e.printStackTrace();
    }
    return null;
  }

  public void deleteSongFromPlaylist(Song song, Playlist playlist){
    if(song != null && playlist != null){
      try{
        playlistDao.deleteSongFromPlaylist(song, playlist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
                e.printStackTrace();
      }
    }
  }

  public void addSongToPlaylist(Song selectedSong, Playlist selectedPlaylist) {
    if(selectedSong != null && selectedPlaylist != null){
      try{
        playlistDao.addSongToPlaylist(selectedSong, selectedPlaylist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public void updatePlaylistName(Playlist selectedPlaylist, String newPlaylistName) {
    if(selectedPlaylist != null && !newPlaylistName.isEmpty()){
      try{
        playlistDao.updatePlaylistName(selectedPlaylist, newPlaylistName);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
      }
    }
  }

}
