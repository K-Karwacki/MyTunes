package dk.easv.mytunes.pl.controllers;

import dk.easv.mytunes.MyTunes;
import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.be.Song;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable
{

  @FXML private HBox mp3Container;
  @FXML private HBox libraryContainer;




  @Override public void initialize(URL location, ResourceBundle resources)
  {
    //Load library controller
    LibraryController libraryController = getAndLoadLibraryController();

    //Load MP3PlayerController and set dependencies
    MediaPlayerController mediaPlayerController = getAndLoadMp3PlayerController();
    mediaPlayerController.setPlaylistListView(libraryController.getPlaylistListView());
    mediaPlayerController.setSongsOnPlaylistListView(libraryController.getSongOnPlaylistListView());
    mediaPlayerController.setLibrary(libraryController.getLibrary());
  }


  private MediaPlayerController getAndLoadMp3PlayerController() {
    FXMLLoader loader = new FXMLLoader(MyTunes.class.getResource("mp3_player.fxml"));
    try{
      mp3Container.getChildren().add(loader.load());
    }catch (IOException e){
      System.out.println("couldn't load a file: " + e.getMessage());
      e.printStackTrace();
    }
    return loader.getController();
  }

  private LibraryController getAndLoadLibraryController(){
    FXMLLoader loader = new FXMLLoader(MyTunes.class.getResource("library.fxml"));
    try{

      libraryContainer.getChildren().add(loader.load());
    }catch (IOException e){
      System.out.println("couldn't load a file: " + e.getMessage());
      e.printStackTrace();
    }
    return loader.getController();
  }





}
