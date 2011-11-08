package fi.koku.demo.ui.hello1;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class HelloServletFilter implements Filter {

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    System.out.println("HelloFilter: doFilter");
    chain.doFilter(request, response);
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    System.out.println("HelloFilter: init");
  }

  public void destroy() {
  }

}
