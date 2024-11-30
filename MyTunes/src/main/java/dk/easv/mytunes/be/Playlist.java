package dk.easv.mytunes.be;

import java.util.List;

public class Playlist {
  private int id;
  private String name;
  private List<Song> songs;

  public Playlist(int id, String name) {
    this.id = id;
    this.name = name;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public List<Song> getSongs() {
    return songs;
  }
  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }
  public void addSong(Song song) {
    songs.add(song);
  }
  public void removeSong(Song song) {
    songs.remove(song);
  }
  public String toString() {
    return name + " (" + songs.size() + "songs)";
  }
}