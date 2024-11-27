package dk.easv.mytunes.PL;

import dk.easv.mytunes.BLL.LogicController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ImportController
{
    private LogicController logicController;
    @FXML
    private TextField title;
    @FXML
    private TextField artist;
    @FXML
    private TextField path;
    @FXML
    private ComboBox genre;

    public ImportController(LogicController logicController)
    {
        this.logicController = logicController;
    }

    @FXML
    private void importSong()
    {
        if(!title.getText().isEmpty() && !artist.getText().isEmpty() && !path.getText().isEmpty())
        {
            logicController.addSong(title.getText() + " " + artist.getText() + " " + path.getText());
            Stage stage = (Stage) title.getScene().getWindow();
            stage.close();
        }
    }
}
