package dk.easv.mytunes.BLL;

import dk.easv.mytunes.DAL.SongDAO;
import dk.easv.mytunes.BE.Song;
import java.util.List;
import java.sql.SQLException;

public class SongManager {
    private SongDAO songDAO;

    public SongManager(){
        this.songDAO = new SongDAO();
    }
    public List<Song> getAllSongs() throws SQLException {
        return songDAO.getAllSongs();
    }
    public void addSong(Song song) throws SQLException {
        songDAO.addSong(song);
    }
    public void removeSong(Song song) throws SQLException {
        songDAO.removeSong(song.getId());
    }
    public void updateSong(Song song) throws SQLException {
        songDAO.updateSong(song);
    }
}
