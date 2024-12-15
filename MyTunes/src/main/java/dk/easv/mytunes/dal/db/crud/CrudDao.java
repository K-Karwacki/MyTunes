package dk.easv.mytunes.dal.db.crud;

import dk.easv.mytunes.be.Playlist;
import dk.easv.mytunes.dal.DBConnection;
import dk.easv.mytunes.dal.utils.RowMapper;

import java.sql.*;
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
    Connection connection = getConnection();
    T result = null;
    connection.setAutoCommit(false);
    try(PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      int affectedRows = stmt.executeUpdate();
      if(affectedRows == 0){
        throw new SQLException("No rows affected");
      }
      int generatedId = -1;

      try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
        if(generatedKeys.next()){
          generatedId = generatedKeys.getInt(1);
        }else {
          throw new SQLException("Added but id not returned");
        }
      }
      connection.commit();
    }catch (SQLException e){
      connection.rollback();
      throw e;
    }
  }

  protected <T> T insertReturn(String insertQuery, String returnQuery, Object[] params, RowMapper<T> rowMapper) throws SQLException{
    Connection connection = getConnection();
    T result = null;
    connection.setAutoCommit(false);
    try(PreparedStatement stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)){
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      int affectedRows = stmt.executeUpdate();
      if(affectedRows == 0){
        throw new SQLException("No rows affected");
      }
      int generatedId = -1;

      try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
        if(generatedKeys.next()){
          generatedId = generatedKeys.getInt(1);
        }else {
          throw new SQLException("Added but id not returned");
        }
      }

      try(PreparedStatement returnStmt = connection.prepareStatement(returnQuery)){
        returnStmt.setInt(1, generatedId);
        try(ResultSet returnSet = returnStmt.executeQuery()){
          if(returnSet.next()){
            result = rowMapper.mapRow(returnSet);
          }
        }
      }
      connection.commit();
    }catch (SQLException e){
      connection.rollback();
      throw e;
    }
    return result;
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
