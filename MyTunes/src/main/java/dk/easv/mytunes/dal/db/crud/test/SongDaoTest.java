package dk.easv.mytunes.dal.db.crud.test;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.db.crud.entities.SongDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDaoTest implements SongDao
{

  @Override public void createSong(Song song) throws SQLException
  {
    System.out.println("called dao create song");

  }

  @Override public void deleteSong(Song song) throws SQLException
  {
    System.out.println("called dao delete song ");

  }

  @Override public void updateSong(Song song) throws SQLException
  {
    System.out.println("called dao update song ");

  }

  @Override public List<Song> getSongsByPlaylistId(int playlistId)
      throws SQLException
  {
    System.out.println("called dao get songs by playlist id");

    return null;
  }

  @Override public List<Song> getAllSongs() throws SQLException
  {
    List<Song> songsInDB = new ArrayList<>();

    songsInDB.add(new Song("Blinding Lights", "The Weeknd", "Pop",  "C:/Music/BlindingLights.mp3","3:22"));
    songsInDB.add(new Song("Shape of You", "Ed Sheeran", "Pop",  "C:/Music/ShapeOfYou.mp3","4:24"));
    songsInDB.add(new Song("Bohemian Rhapsody", "Queen", "Rock", "C:/Music/BohemianRhapsody.mp3","5:54"));
    songsInDB.add(new Song("Imagine", "John Lennon", "Classic",  "C:/Music/Imagine.mp3","3:07"));
    songsInDB.add(new Song("Smells Like Teen Spirit", "Nirvana", "Grunge",  "C:/Music/TeenSpirit.mp3","5:01"));

    return null;
  }

  @Override public void updateArtistById(int id, String newArtistName)
      throws SQLException
  {
    System.out.println("called dao update artist by id");

  }

  @Override public Song createReturnSong(Song song) throws SQLException
  {
    return null;
  }
}
