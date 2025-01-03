package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.db.crud.entities.SongDao;
import dk.easv.mytunes.dal.db.crud.entities.SongDaoDB;
import dk.easv.mytunes.dal.db.crud.test.SongDaoTest;
import dk.easv.mytunes.pl.GuiComponents;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class SongService
{
  private final SongDao songDao;

  public SongService(){
//    songDao = new SongDaoDB();
    songDao = new SongDaoTest();
  }

  public Song createReturnSong(Song song){
    if(song != null){
      try{
        return songDao.createReturnSong(song);
      }catch (SQLException e){
        System.out.println("SongDAO addSong failed: " + e.getMessage());
              e.printStackTrace();
      }
    }
    return null;
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
      e.printStackTrace();
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

  public void updateSong(Song selectedSong) {
    if(selectedSong != null){
      try{
        songDao.updateSong(selectedSong);
      }catch (SQLException e){
        e.printStackTrace();
      }
    }
  }
}
