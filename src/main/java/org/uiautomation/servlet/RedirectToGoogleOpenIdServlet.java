package org.uiautomation.servlet;

import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdException;
import org.expressme.openid.OpenIdManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RedirectToGoogleOpenIdServlet extends OpenIdServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String op = request.getParameter("op");
    if ("Google".equals(op)) {
      // redirect to Google sign on page:
      Endpoint endpoint = getOpenIdManager(request).lookupEndpoint("Google");
      Association association = getOpenIdManager(request).lookupAssociation(endpoint);
      request.getSession().setAttribute(ATTR_MAC, association.getRawMacKey());
      request.getSession().setAttribute(ATTR_ALIAS, endpoint.getAlias());
      String url = getOpenIdManager(request).getAuthenticationUrl(endpoint, association);
      response.sendRedirect(url);
    } else {
      throw new ServletException("Bad parameter op=" + op);
    }
  }


}
