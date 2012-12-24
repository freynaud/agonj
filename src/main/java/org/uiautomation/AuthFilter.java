package org.uiautomation;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AuthFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request instanceof HttpServletRequest) {
      HttpServletRequest req = (HttpServletRequest) request;
      String servletPath = req.getServletPath();

      if (!isSignIn(req) && !isSignInPath(servletPath)) {
        ((HttpServletResponse) response).sendRedirect("/agon/signin/signin?op=Google");
      } else {
        chain.doFilter(request, response);
      }
    }
  }


  @Override
  public void destroy() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  private boolean isSignInPath(String path) {
    return path.contains("/signin") || path.contains("/openid");
  }

  private boolean isSignIn(HttpServletRequest request) {
    Cookie cookie = getCookie(request);
    if (cookie != null) {
      String session = cookie.getValue();
      String user = Database.getUserForSession(session);
      return user != null;

    }
    return false;
  }

  private Cookie getCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if ("id".equals(cookie.getName())) {
        return cookie;
      }
    }
    return null;
  }
}
