package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.SongDAO;

import java.sql.SQLException;

public class SongManager
{
  private final SongDAO songDAO= new SongDAO();

  public void addNewSong (Song song) throws SQLException
  {
    songDAO.addSong(song);
  }

}
