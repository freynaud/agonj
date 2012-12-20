package org.uiautomation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloWorld extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.getWriter().print("Hello Michael!\n");

    try {
      Connection connection = Database.getConnection();

      Statement stmt = connection.createStatement();
      stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
      stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
      while (rs.next()) {
        resp.getWriter().print("Read from DB: " + rs.getTimestamp("tick"));
      }
    } catch (Exception e) {
      resp.getWriter().print(e.getMessage());
    }
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new HelloWorld()), "/*");
    server.start();
    server.join();
  }
}
