package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.daoimpl.SongDaoImpl;
import dk.easv.mytunes.dal.interfaces.SongDao;

import java.sql.SQLException;
import java.util.List;

public class SongManager
{
  private final SongDao songDao;

  public SongManager(){
    songDao = new SongDaoImpl();
  }

  public void addSong (Song song){
    if(song != null){
      try{
        songDao.createSong(song);
      }catch (SQLException e){
        System.out.println("SongDAO addSong failed: " + e.getMessage());
        //      e.printStackTrace();
      }
    }
  }

  public void deleteSong(Song song){
    if(song != null){
      try{
        songDao.deleteSong(song);
      }catch (Exception e){
        System.out.println("SongDAO deleteSong failed: " + e.getMessage());
//        e.printStackTrace();
      }
    }
  }
  public List<Song> getAllSongs(){
    try{
      return songDao.getAllSongs();
    }catch (Exception e){
      System.out.println("SongDAO getAllSongs failed: " + e.getMessage());
//      e.printStackTrace();
    }
    return null;
  }


  public List<Song> getSongsForPlaylist(Playlist playlist){
    if(playlist != null){
      try{
        return songDao.getSongsByPlaylistId(playlist.getId());
      }catch (Exception e){
        System.out.println("SongDAO getSongsByPlaylistId failed: " + e.getMessage());
//        e.printStackTrace();
      }
    }
    return null;
  }

}
