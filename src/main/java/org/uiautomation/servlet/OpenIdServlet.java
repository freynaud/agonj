package org.uiautomation.servlet;

import org.expressme.openid.OpenIdManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;


public abstract class OpenIdServlet extends HttpServlet {

  protected static final String ATTR_MAC = "openid_mac";
  protected static final String ATTR_ALIAS = "openid_alias";
  private OpenIdManager manager;

  protected synchronized OpenIdManager getOpenIdManager(HttpServletRequest request) {
    if (manager == null) {
      String scheme = request.getScheme(); // http
      String serverName = request.getServerName(); // hostname.com
      int serverPort = request.getServerPort(); // 80
      String contextPath = request.getContextPath(); // /mywebapp

      manager = new OpenIdManager();

      String realm = scheme + "://" + serverName + ":" + serverPort ;
      manager.setRealm(realm);
      manager.setReturnTo(realm + contextPath + "/openid/response"); // change to your servlet url
    }
    return manager;
  }
}
