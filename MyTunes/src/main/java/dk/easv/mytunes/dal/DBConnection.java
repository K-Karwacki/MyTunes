package dk.easv.mytunes.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
  private static final String DB_URL = "jdbc:sqlserver://10.176.111.34:1433;databaseName=G7MyTunes;encrypt=true;trustServerCertificate=true;";
  private static final String DB_USER = "CSe2024b_e_14";
  private static final String DB_PASSWORD = "CSe2024bE14!24";
  private static Connection connection;

  public static Connection getConnection() throws SQLException{
    if(connection == null){
      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    return connection;
  }
}