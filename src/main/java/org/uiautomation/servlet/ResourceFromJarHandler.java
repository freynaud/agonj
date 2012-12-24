package org.uiautomation.servlet;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ResourceFromJarHandler extends ResourceHandler {

  @Override
  public Resource getResource(String path) throws MalformedURLException {
    if (path.equals("/favicon.ico")){
      return null;
    } else {
      URL r = getClass().getResource(path);
      try {
        System.out.println("serving resource : "+r.toExternalForm());
        return Resource.newResource(r);
      } catch (IOException e) {
        return null;
      }
    }
  }
}
