package dk.easv.mytunes.dal.utils;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.interfaces.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SongRowMapper implements RowMapper<Song>
{
  @Override public Song mapRow(ResultSet resultSet) throws SQLException {
    Song song = new Song();
    song.setId(resultSet.getInt("id"));
    song.setTitle(resultSet.getString("title"));
    song.setArtist(resultSet.getString("artist"));
    song.setCategory(resultSet.getString("category"));
    song.setFilePath(resultSet.getString("path"));
    song.setDuration(resultSet.getString("duration"));
    return song;
  }
}
