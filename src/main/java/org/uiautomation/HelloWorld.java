package org.uiautomation;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdException;
import org.expressme.openid.OpenIdManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class HelloWorld extends HttpServlet {


  private OpenIdManager manager;



  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String s = request.getParameter("markers");
    JSONObject o = new JSONObject();
    try {
      o.put("title","My title");
    } catch (JSONException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);

    response.getWriter().print(o.toString());
    response.getWriter().flush();
  }



  private void db() throws URISyntaxException, SQLException {
    Connection connection = Database.getConnection();

    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
    stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
    stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
    ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
    while (rs.next()) {
      //resp.getWriter().print("Read from DB: " + rs.getTimestamp("tick"));
    }
  }

  private Template configFreemarker() throws IOException {
    Configuration cfg = new Configuration();
// Specify the data source where the template files come from.
// Here I set a file directory for it:
    cfg.setClassForTemplateLoading(HelloWorld.class, "/templates");
// Specify how templates will see the data-model. This is an advanced topic...
// but just use this:
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("login.ftl");
    return template;

  }




  public static void main(String[] args) throws Exception {
    /*Template template =new HelloWorld().configFreemarker();

    Map root = new HashMap();
    // Put string ``user'' into the root
    root.put("googleURL", "Big Joe");

    Writer out = new OutputStreamWriter(System.out);
    template.process(root, out);
    out.flush();*/

    String port = System.getenv("PORT");

    Server server = new Server(Integer.valueOf(port));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new HelloWorld()), "/*");
    server.start();
    server.join();


  }
}
