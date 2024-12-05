package dk.easv.mytunes.dal.interfaces;

import dk.easv.mytunes.dal.DBConnection;
import dk.easv.mytunes.dal.interfaces.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T>
{
  private Connection connection;
//  private RowMapper<T> rowMapper;
  private final Class<T> entityClass;

  public GenericDao(Class<T> entityClass){
    this.entityClass = entityClass;
    try{
      this.connection = DBConnection.getConnection();
    }catch (SQLException exception){
      System.out.println("Connection failed. -- " + exception.getMessage());
//      exception.printStackTrace();
    }
  }

  public List<T> executeQuery(String query, Object[] params, RowMapper<T> rowMapper) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

  public void insert(String query, Object[] params) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      stmt.executeUpdate();
    }
  }

  // Generic method for UPDATE
  public void update(String query, Object[] params) throws SQLException
  {
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      stmt.executeUpdate();
    }
  }

  // Generic method for DELETE
  public void delete(String query, Object[] params) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      stmt.executeUpdate();
    }
  }
}
