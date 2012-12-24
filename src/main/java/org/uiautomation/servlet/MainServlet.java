package org.uiautomation.servlet;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.eclipse.jetty.server.Server;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA. User: freynaud Date: 24/12/2012 Time: 11:42 To change this template
 * use File | Settings | File Templates.
 */
public class MainServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/html; charset=utf-8");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);

    Template index= getTemplate();
    try {
      index.process(new HashMap<String,String>(),response.getWriter());
    } catch (TemplateException e) {
      e.printStackTrace();
    }
    response.getWriter().flush();
  }




  private Template getTemplate() throws IOException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(MainServlet.class, "/templates");

    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("index.ftl");
    return template;

  }

}
