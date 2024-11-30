package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Playlist;

import java.util.List;

public interface IPlaylistDAO
{
  void createNewPlaylist(Playlist playlist);
  List<Playlist> getAllPlaylists();
  void updatePlaylist(Playlist playlist);
  void deletePlaylist(Playlist playlist);
}
