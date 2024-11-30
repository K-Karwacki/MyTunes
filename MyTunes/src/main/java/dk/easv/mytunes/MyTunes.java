package dk.easv.mytunes;

import dk.easv.mytunes.pl.controllers.MainSceneController;
import dk.easv.mytunes.pl.models.PlayerModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyTunes extends Application {
    private final PlayerModel playerModel = new PlayerModel();
    private Parent mainView;
    private Parent createPlaylistView;

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_scene.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MyTunes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}