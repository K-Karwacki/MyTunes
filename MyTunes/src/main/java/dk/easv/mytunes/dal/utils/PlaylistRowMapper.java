package dk.easv.mytunes.dal.utils;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.dal.interfaces.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistRowMapper implements RowMapper<Playlist>
{
  @Override public Playlist mapRow(ResultSet resultSet) throws SQLException {
    Playlist playlist = new Playlist();
    playlist.setId(resultSet.getInt("id"));
    playlist.setName(resultSet.getString("name"));
    return playlist;
  }

}
