package dk.easv.mytunes.pl.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.PlaylistService;
import dk.easv.mytunes.bll.SongService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class LibraryModel
{
  public static final List<String> SONG_CATEGORIES = Arrays.asList(
      "All",
      "Pop",
      "Rock",
      "Hip-Hop",
      "R&B",
      "Electronic",
      "Metal",
      "Rap",
      "Reggae",
      "Classical",
      "Jazz",
      "Country"
  );

  private final PlaylistService playlistService;
  private final SongService songService;


  private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
  private final ObservableList<Song> allSongsObservableList = FXCollections.observableArrayList();
  private final Map<Playlist, ObservableList<Song>> library;
  private Playlist mainPlaylist;


  public LibraryModel(){
    playlistService = new PlaylistService();
    songService = new SongService();
//    songModel = new SongModel();
    library = new HashMap<>();


    populateFromDatabase();
  }

  private void populateFromDatabase(){
    // Try fetch all songs from database
    List<Song> allSongs = songService.getAllSongs();
    if(allSongs == null){
      allSongsObservableList.setAll(FXCollections.observableArrayList());
    }else{
      allSongsObservableList.setAll(allSongs);
    }

    // Try fetch main playlist, if not found create one and save in database and put to library
    mainPlaylist = playlistService.getMainPlaylist();
    if(mainPlaylist == null){
      playlistService.insertNewPlaylist(new Playlist("Library", true));
      if(playlistService.getMainPlaylist() == null){
        mainPlaylist = new Playlist("Library", true);
      }else{
        mainPlaylist = playlistService.getMainPlaylist();
      }
    }
    playlists.add(mainPlaylist);
    mainPlaylist.setSongs(allSongsObservableList);
    library.put(mainPlaylist, allSongsObservableList);

    // Try fetch all playlists from database, and filter all that are not main playlist, populate them with songs and put to library
    List<Playlist> allPlaylists = playlistService.getAllPlaylists();

    if(allPlaylists != null){
      for (Playlist playlist : allPlaylists.stream().filter(Playlist::isNotMain).collect(
          Collectors.toList()))
      {
        List<Song> songsOnPlaylist = songService.getSongsForPlaylist(playlist);
        if(songsOnPlaylist != null){
          playlist.setSongs(songsOnPlaylist);
          library.put(playlist, FXCollections.observableArrayList(songsOnPlaylist));
        }
        playlists.add(playlist);
      }
    }
  }

  public ObservableList<Playlist> getPlaylists() {
    return playlists;
  }

  public void removePlaylist(Playlist playlistToRemove){
    playlistService.deletePlaylist(playlistToRemove);
    playlists.remove(playlistToRemove);
    library.get(playlistToRemove).removeAll();
    library.remove(playlistToRemove);
  }

  public void createPlaylist(Playlist newPlaylist){
    playlistService.insertNewPlaylist(newPlaylist);
    List<Playlist> list = playlistService.getAllPlaylists();
    if(list != null){
      playlists.add(list.getLast());
      library.put(list.getLast(), FXCollections.observableArrayList());
      return;
    }
    playlists.add(newPlaylist);
    library.put(newPlaylist, FXCollections.observableArrayList());
  }

  public void updatePlaylistName(Playlist selectedPlaylist, String newPlaylistName) {
    playlistService.updatePlaylistName(selectedPlaylist, newPlaylistName);
  }

  public void addSongToPlaylistLibrary(Song song, Playlist playlist) {
    playlist.addSong(song);
    if(!library.get(playlist).contains(song)){
      library.get(playlist).add(song);
      playlistService.addSongToPlaylist(song, playlist);
    }
  }

  public void removeSongFromLibrary(Song song){
    songService.deleteSong(song);
    removeSongFromAllPlaylists(song);
  }

  public void removeSongFromAllPlaylists(Song song){
    for (Playlist playlist : playlists) {
      removeSongFromPlaylist(song, playlist);
    }
  }

  public void removeSongFromPlaylist(Song song, Playlist playlist){
    playlist.removeSong(song);
    library.get(playlist).remove(song);
    playlistService.deleteSongFromPlaylist(song, playlist);
  }

  public Map<Playlist, ObservableList<Song>> getLibrary() {
    return library;
  }

  public void importNewSong(Song song) {
    songService.addSong(song);
    List<Song> refreshesSongs = songService.getAllSongs();
    if(refreshesSongs != null){
      allSongsObservableList.setAll(refreshesSongs);
      return;
    }
    allSongsObservableList.add(song);
  }

  public void updateSong(Song selectedSong, Map<String, String> updatedSongMap) {
    selectedSong.setTitle(updatedSongMap.get("title"));
    selectedSong.setArtist(updatedSongMap.get("artist"));
    selectedSong.setDuration(updatedSongMap.get("duration"));
    selectedSong.setFilePath(updatedSongMap.get("path"));
    selectedSong.setCategory(updatedSongMap.get("category"));
    songService.updateSong(new Song(selectedSong.getId(), updatedSongMap.get("title"), updatedSongMap.get("artist"), updatedSongMap.get("category"), updatedSongMap.get("path"), updatedSongMap.get("duration")));
    allSongsObservableList.setAll(songService.getAllSongs());
    mainPlaylist.setSongs(allSongsObservableList);
    for (Playlist playlist:playlists){
      if(playlist.isNotMain()){
        List<Song> songsOnPlaylist = songService.getSongsForPlaylist(playlist);
        if(songsOnPlaylist != null){
          playlist.setSongs(songsOnPlaylist);
          library.get(playlist).setAll(songsOnPlaylist);
        }
      }
    }
  }

}
