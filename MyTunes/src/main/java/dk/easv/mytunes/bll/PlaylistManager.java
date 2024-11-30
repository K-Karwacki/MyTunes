package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.dal.IPlaylistDAO;
import dk.easv.mytunes.dal.PlaylistDAO;

public class PlaylistManager
{
  private final IPlaylistDAO playlistDAO = new PlaylistDAO();

  public void deletePlaylist(Playlist playlist)
  {
    playlistDAO.deletePlaylist(playlist);
  }

  public void createPlaylist(String name)
  {
    System.out.println("manager called");
    playlistDAO.createNewPlaylist(new Playlist(1,name));
  }
}
