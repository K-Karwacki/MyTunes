package dk.easv.mytunes.pl.models;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.PlaylistService;
import dk.easv.mytunes.bll.SongService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
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

  private final Map<Integer, Song> songsCache;
  private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
  private final ObservableList<Song> allSongs = FXCollections.observableArrayList();
  private final Map<Playlist, ObservableList<Song>> library;
  private Playlist mainPlaylist;


  public LibraryModel(){
    playlistService = new PlaylistService();
    songService = new SongService();
    library = new WeakHashMap<>();
    songsCache = new HashMap<>();


    populateFromDatabase();
  }

  private void populateFromDatabase(){
    List<Song> fetchedSongs = songService.getAllSongs();
    List<Playlist> fetchedPlaylists = playlistService.getAllPlaylists();

    if(fetchedSongs != null && fetchedPlaylists != null) {
      allSongs.setAll(fetchedSongs);
      mainPlaylist = fetchedPlaylists.stream().filter(Playlist::isMain).collect(Collectors.toList()).getFirst();

      if (mainPlaylist == null) {
        mainPlaylist = playlistService.createNewPlaylist(new Playlist("Library", true));
      }
      mainPlaylist.setSongs(allSongs);
      library.put(mainPlaylist, allSongs);
      playlists.add(mainPlaylist);

      List<Playlist> playlistsNotMain = fetchedPlaylists.stream().filter(Playlist::isNotMain).toList();

      for (Playlist playlist:playlistsNotMain){
        List<Song> songsOnPlaylist = songService.getSongsForPlaylist(playlist);
        if(songsOnPlaylist != null){
          playlist.setSongs(songsOnPlaylist);
          library.put(playlist, FXCollections.observableArrayList(songsOnPlaylist));
        }
        playlists.add(playlist);
      }
    }
    else{
      mainPlaylist = new Playlist("Library", true);
      mainPlaylist.setSongs(allSongs);
      playlists.add(mainPlaylist);
      library.put(mainPlaylist, allSongs);
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

  public void createNewPlaylistAndAddToLibrary(Playlist playlist){
    Playlist returnedPlaylist = playlistService.createNewPlaylist(playlist);
    if(returnedPlaylist!= null){
      playlists.add(returnedPlaylist);
      library.put(returnedPlaylist, FXCollections.observableArrayList());
      return;
    }
    playlists.add(playlist);
    library.put(playlist, FXCollections.observableArrayList());
  }

  public void updatePlaylistName(Playlist selectedPlaylist, String newPlaylistName) {
    playlistService.updatePlaylistName(selectedPlaylist, newPlaylistName);
  }

  public void addSongToPlaylistLibrary(Song song, Playlist playlist) {
    playlist.addSong(song);
    if(!library.get(playlist).contains(song)){
      for(Song songOnPl: library.get(playlist)){
        if(song.getId() != 0 && song.getId() == songOnPl.getId()){
          return;
        }
      }
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
    if(song.getId() == 0 && library.get(playlist).contains(song)){
      library.get(playlist).remove(song);
    }else{
      library.get(playlist).removeIf(songOnPl -> songOnPl.getId() == song.getId());
    }
    playlistService.deleteSongFromPlaylist(song, playlist);
  }

  public Map<Playlist, ObservableList<Song>> getLibrary() {
    return library;
  }

  public void importNewSong(Song song) {
    Song newSong = songService.createReturnSong(song);
    if(newSong != null){
      allSongs.add(newSong);
      return;
    }
    allSongs.add(song);
  }

  public void updateSong(Song selectedSong, Map<String, String> updatedSongMap) {
    selectedSong.setTitle(updatedSongMap.get("title"));
    selectedSong.setArtist(updatedSongMap.get("artist"));
    selectedSong.setDuration(updatedSongMap.get("duration"));
    selectedSong.setFilePath(updatedSongMap.get("path"));
    selectedSong.setCategory(updatedSongMap.get("category"));
    songService.updateSong(new Song(selectedSong.getId(), updatedSongMap.get("title"), updatedSongMap.get("artist"), updatedSongMap.get("category"), updatedSongMap.get("path"), updatedSongMap.get("duration")));

    List<Song> refreshedSongsList = songService.getAllSongs();
    if(refreshedSongsList != null){
      allSongs.setAll(refreshedSongsList);
      for (Playlist playlist:playlists){
        if(playlist.isNotMain()){
          List<Song> refreshedSongsOnPlaylist = songService.getSongsForPlaylist(playlist);
          if(refreshedSongsOnPlaylist != null){
            playlist.setSongs(refreshedSongsOnPlaylist);
            library.get(playlist).setAll(refreshedSongsList);
          }
        }
      }
    }
  }
}
