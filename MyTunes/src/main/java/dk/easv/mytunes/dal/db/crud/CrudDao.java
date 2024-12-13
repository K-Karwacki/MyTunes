package dk.easv.mytunes.dal.db.crud;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.dal.DBConnection;
import dk.easv.mytunes.dal.utils.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrudDao<T>
{
  private final Class<T> entityClass;

  public CrudDao(Class<T> entityClass){
    this.entityClass = entityClass;
  }

  private Connection getConnection(){
    return DBConnection.getConnection();
  }


  //Generic method for SELECT
  protected List<T> select(String query, Object[] params, RowMapper<T> rowMapper) throws SQLException {
    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      try (ResultSet rs = stmt.executeQuery()) {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
          results.add(rowMapper.mapRow(rs));
        }
        return results;
      }
    }
  }

  //Generic method for INSERT
  protected void insert(String query, Object[] params) throws SQLException {
    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      stmt.executeUpdate();
    }
  }

  // Generic method for UPDATE
  protected void update(String query, Object[] params) throws SQLException {
    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      stmt.executeUpdate();
    }
  }

  // Generic method for DELETE
  protected void delete(String query, Object[] params) throws SQLException {
    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      stmt.executeUpdate();
    }
  }
}
/*
* EXTEND TO DATA INTEGRITY VALIDATION OR SOMETHING
*
* */
