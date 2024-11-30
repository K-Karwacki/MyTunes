package dk.easv.mytunes.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
  private static final String DB_URL = "jdbc:sqlite:MyTunes.db";
  private static final String DB_USER = "user";
  private static final String DB_PASSWORD = "password";
  private static Connection connection;

  public static Connection getConnection() throws SQLException{
    if(connection == null){
      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    return connection;
  }
}