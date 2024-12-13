package dk.easv.mytunes.be;

public class Song {
  private int id; // Unique identifier for the song
  private String title;
  private String artist;
  private String category;
  private String filePath;
  private String duration; // e.g., "4:23"

  // Constructor

  public Song()
  {

  }

  public Song(String title, String artist, String category, String filePath, String duration) {
    this.title = title;
    this.artist = artist;
    this.category = category;
    this.filePath = filePath;
    this.duration = duration;
  }

  public Song(int id, String title, String artist, String category, String filePath, String duration) {
    this.id = id;
    this.title = title;
    this.artist = artist;
    this.category = category;
    this.filePath = filePath;
    this.duration = duration;
  }


  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  @Override
  public String toString() {
    return getArtist() + " - " + getTitle() + " (" + getCategory() + ")";
  }
}