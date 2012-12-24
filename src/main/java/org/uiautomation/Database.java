package org.uiautomation;


import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

  public static Connection getConnection() throws URISyntaxException, SQLException {

    String s = System.getenv("DATABASE_URL");
    URI dbUri = new URI(s);

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

    return DriverManager.getConnection(dbUrl, username, password);
  }

  public static String getUserName(String email) {
    try {
      Connection connection = Database.getConnection();
      PreparedStatement stmt = connection.prepareStatement("SELECT name FROM users WHERE email=?");
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        return rs.getString(1);
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void createSession(String user, String session) {
    String insert = "INSERT INTO sessions(session, \"user\") VALUES (?, ?);";
    try {
      Connection connection = Database.getConnection();
      PreparedStatement stmt = connection.prepareStatement(insert);
      stmt.setString(1, session);
      stmt.setString(2, user);
      int res = stmt.executeUpdate();
      if (res != 1) {
        System.err.println("expected 1 row update, got " + res);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getUserForSession(String session) {
    String select = "SELECT \"user\" FROM sessions WHERE session=?";
    try {
      Connection con = Database.getConnection();
      PreparedStatement stmt = con.prepareStatement(select);
      stmt.setString(1, session);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        return rs.getString(1);
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

