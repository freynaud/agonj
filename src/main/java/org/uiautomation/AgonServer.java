package org.uiautomation;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.uiautomation.servlet.MainServlet;
import org.uiautomation.servlet.RedirectToGoogleOpenIdServlet;
import org.uiautomation.servlet.ResourceFromJarHandler;
import org.uiautomation.servlet.VerifyOpenIdResponseServlet;

public class AgonServer {

  public static void main(String[] args) throws Exception {

    String port = System.getenv("PORT");
    Server server = new Server(Integer.valueOf(port));

    // static files handler
    ResourceHandler resourceHandler = new ResourceFromJarHandler();
    resourceHandler.getMimeTypes().addMimeMapping("appcache", "text/cache-manifest");
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setResourceBase("/static/");

    // servlets
    ServletContextHandler servletHandler = new ServletContextHandler(server, "/agon", true, false);
    servletHandler.addServlet(HelloWorld.class, "/map/*");
    servletHandler.addServlet(MainServlet.class, "/map/Agon.html");
    servletHandler.addServlet(RedirectToGoogleOpenIdServlet.class, "/signin/*");
    servletHandler.addServlet(VerifyOpenIdResponseServlet.class, "/openid/*");
    servletHandler.addFilter(AuthFilter.class, "/*", FilterMapping.ALL);

    // wiring
    HandlerCollection handlerList = new HandlerCollection();
    handlerList.setHandlers(new Handler[]{servletHandler, resourceHandler});
    server.setHandler(handlerList);

    server.start();
    server.join();

  }
}
