package org.uiautomation.servlet;

import org.eclipse.jetty.util.security.Credential;
import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdException;
import org.expressme.openid.OpenIdManager;
import org.uiautomation.Database;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class VerifyOpenIdResponseServlet extends OpenIdServlet {

  static final long ONE_HOUR = 3600000L;
  static final long TWO_HOUR = ONE_HOUR * 2L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String op = request.getParameter("op");
    if (op == null) {
      // check nonce:
      checkNonce(request.getParameter("openid.response_nonce"));
      // get authentication:
      byte[] mac_key = (byte[]) request.getSession().getAttribute(ATTR_MAC);
      String alias = (String) request.getSession().getAttribute(ATTR_ALIAS);
      Authentication
          authentication =
          getOpenIdManager(request).getAuthentication(request, mac_key, alias);
      String identity = authentication.getIdentity();
      String email = authentication.getEmail();
      String session = authenticate(identity, email);
      Cookie auth = new Cookie("id", session);
      if (session != null) {
        auth.setPath("/agon");
        auth.setMaxAge(365 * 24 * 3600);
      }
      response.addCookie(auth);
      response.sendRedirect("/agon/map");
    } else {
      throw new ServletException("Bad parameter op=" + op);
    }
  }

  private String authenticate(String identity, String email) {
    String user = Database.getUserName(email);
    if (user == null) {
      System.out.println(email + " doesn't have access.");
      return null;
    }
    String session = Credential.MD5.digest(email + identity);
    Database.createSession(user, session);
    return session;
  }

  private void checkNonce(String nonce) {
    // check response_nonce to prevent replay-attack:
    if (nonce == null || nonce.length() < 20) {
      throw new OpenIdException("Verify failed.");
    }
    long nonceTime = getNonceTime(nonce);
    long diff = System.currentTimeMillis() - nonceTime;
    if (diff < 0) {
      diff = (-diff);
    }
    if (diff > ONE_HOUR) {
      throw new OpenIdException("Bad nonce time.");
    }
    if (isNonceExist(nonce)) {
      throw new OpenIdException("Verify nonce failed.");
    }
    storeNonce(nonce, nonceTime + TWO_HOUR);
  }

  private boolean isNonceExist(String nonce) {
    // TODO: check if nonce is exist in database:
    return false;
  }

  private void storeNonce(String nonce, long expires) {
    // TODO: store nonce in database:
  }

  private long getNonceTime(String nonce) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
          .parse(nonce.substring(0, 19) + "+0000")
          .getTime();
    } catch (ParseException e) {
      throw new OpenIdException("Bad nonce time.");
    }
  }
}
