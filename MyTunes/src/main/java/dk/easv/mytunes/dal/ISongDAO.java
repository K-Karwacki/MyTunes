package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Song;

import java.util.List;

public interface ISongDAO
{
  void addNewSong(Song song);
  List<Song> getAllSongs();
  void removeSong(Song song);
  void updateSong(Song song);
}
