package dk.easv.mytunes.PL;

import dk.easv.mytunes.BLL.LogicController;
import dk.easv.mytunes.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class TunesController
{
    private LogicController logicController;

    public TunesController(LogicController logicController)
    {
        this.logicController = logicController;
    }

    @FXML
    private void importSng() throws IOException
    {
        logicController.importWindow();
    }

    public void addSong(String details)
    {

    }
}