package fi.koku.demo.ui.hello1;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

/**
 * authorization portlet filter demo.
 * 
 * @author aspluma
 */
public class HelloPortletFilter implements RenderFilter, ActionFilter {
  private FilterConfig filterConfig;
  private boolean requireStrongAuth;

  public void init(FilterConfig filterConfig) throws PortletException {
    System.out.println("HelloPortletFilter: init");
    this.filterConfig = filterConfig;
    requireStrongAuth = Boolean.valueOf(filterConfig.getInitParameter("requireStrongAuthSession"));
    System.out.println(requireStrongAuth);
  }

  public void destroy() {
  }

  public void doFilter(ActionRequest request, ActionResponse response, FilterChain chain) throws IOException,
      PortletException {
    System.out.println("HelloPortletFilter: doFilter(action)");

    chain.doFilter(request, response);
  }

  public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException,
      PortletException {
    System.out.println("HelloPortletFilter: doFilter(render)");
    System.out.println(request.getAttribute("hasStrongAuthSession"));

    if(requireStrongAuth && request.getAttribute("hasStrongAuthSession") == null) {
      PortletRequestDispatcher prd = filterConfig.getPortletContext().getRequestDispatcher("/WEB-INF/jsp/weak.jsp");
      prd.include(request, response);
      return;
    }
    chain.doFilter(request, response);
  }

}
