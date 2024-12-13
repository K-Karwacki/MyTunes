package dk.easv.mytunes.be;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
  private int id;
  private String name;
  private List<Song> songs;
  private boolean isMain;

  public Playlist() {
    songs = new ArrayList<>();
    this.isMain = false;
  }
  public Playlist(String name){
    this.name = name;
    this.songs = new ArrayList<>();
    this.isMain = false;
  }

  public Playlist(String name, boolean isMain){
    this.name = name;
    this.songs = new ArrayList<>();
    this.isMain = true;
  }

  public Playlist(int id, String name) {
    this.id = id;
    this.name = name;
    this.songs = new ArrayList<>();
    this.isMain = false;
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
  public List<Song> getSongs() { return songs; }
  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }
  public void addSong(Song song) {
    if(songs.contains(song)){
      return;
    }
    songs.add(song);
  }
  public void removeSong(Song song) { if(songs.size()>0){songs.remove(song);};}
  public String toString() {
    return name + " (" + songs.size() + "songs)";
  }

  public boolean isMain() {
    return isMain;
  }
  public boolean isNotMain() {
    return !isMain;
  }

  public void setMain()
  {
    isMain = true;
  }
}