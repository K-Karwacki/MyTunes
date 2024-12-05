package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.daoimpl.PlaylistDaoImpl;
import dk.easv.mytunes.dal.interfaces.PlaylistDao;

import java.sql.SQLException;
import java.util.List;

public class PlaylistManager
{
  private final PlaylistDao playlistDao;

  public PlaylistManager(){
    playlistDao = new PlaylistDaoImpl();
  }

  public void deletePlaylist(Playlist playlist) {
    if(playlist!=null){
      try {
        playlistDao.deletePlaylist(playlist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
//        e.printStackTrace();
      }
    }
//    playlistDAO.delete(playlist);
  }

  public void insertNewPlaylist(Playlist newPlaylist) {
    if(newPlaylist != null){
      try{
        playlistDao.createPlaylist(newPlaylist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
        //        e.printStackTrace();
      }
    }
  }

  public List<Playlist> getAllPlaylists() {
    try{
      return playlistDao.getAllPlaylists();
    }catch (SQLException e){
      System.out.println("PlaylistDao failed. " + e.getMessage());
      //      e.printStackTrace();
    }
    return null;
  }

  public void deleteSongFromPlaylist(Song song, Playlist playlist){
    if(song != null && playlist != null){
      try{
        playlistDao.deleteSongFromPlaylist(song, playlist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
        //        e.printStackTrace();
      }
    }
  }



  public void addSongToPlaylist(Song selectedSong, Playlist selectedPlaylist) {
    System.out.println(selectedPlaylist+""+ selectedSong);
    if(selectedSong != null && selectedPlaylist != null){
      try{
        playlistDao.addSongToPlaylist(selectedSong, selectedPlaylist);
      }catch (SQLException e){
        System.out.println("PlaylistDao failed. " + e.getMessage());
//        e.printStackTrace();
      }
    }
  }

}
