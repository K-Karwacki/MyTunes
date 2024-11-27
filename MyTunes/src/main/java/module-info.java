module dk.easv.mytunes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports dk.easv.mytunes.PL;
    opens dk.easv.mytunes.PL to javafx.fxml;
    exports dk.easv.mytunes;
    opens dk.easv.mytunes to javafx.fxml;
    exports dk.easv.mytunes.BE;
    opens dk.easv.mytunes.BE to javafx.fxml;
}