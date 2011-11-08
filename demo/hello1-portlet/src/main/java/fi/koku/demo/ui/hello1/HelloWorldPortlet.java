package fi.koku.demo.ui.hello1;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ProcessAction;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Demo portlet
 * 
 * @author aspluma
 */
public class HelloWorldPortlet extends GenericPortlet {
  private static final Logger logger = LoggerFactory.getLogger(HelloWorldPortlet.class);
  
  public void init() {
    System.out.println("HelloWorldPortlet: init");
    logger.debug("init");
  }

  @Override
  protected void doHeaders(RenderRequest request, RenderResponse response) {
    super.doHeaders(request, response);
    System.out.println("HelloWorldPortlet: doHeaders");
//    PortalContext portalContext = request.getPortalContext();
//    String portalInfo = portalContext.getPortalInfo();
//    System.out.println("info: "+portalInfo);
    
    setHeaders(request, response, "x-myheader-2", "dynamic title", "/js/puppaduppa.js");
  }

  private void setHeaders(RenderRequest request, RenderResponse response, String hdr, String title, String js) {
    response.addProperty(hdr, "hei, vaan"); // set HTTP header
    
    // add HTML head elements
    Element e = response.createElement("title");
    e.setTextContent(title);
    response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, e);
    
    Element jsElement = response.createElement("script");
    jsElement.setAttribute("src", response.encodeURL((request.getContextPath() + js)));
    jsElement.setAttribute("type", "text/javascript");
    response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jsElement);
  }
 
  @RenderMode(name = "VIEW")
  public void view(RenderRequest request, RenderResponse response) throws IOException, PortletException {
    System.out.println("HelloWorldPortlet: render");
    
//    System.out.println("markup sup: "+request.getPortalContext().getProperty(PortalContext.MARKUP_HEAD_ELEMENT_SUPPORT));
//    System.out.println("render part: "+request.getAttribute(PortletRequest.RENDER_PART));

    PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/hello1.jsp");
    prd.include(request, response);
  }
  
  @ProcessAction(name="greetAction")
  public void greet(ActionRequest request, ActionResponse response)
      throws PortletException, IOException {
    System.out.println("HelloWorldPortlet: action");
    String msg = "Hello, "+request.getParameter("name");
    response.setRenderParameter("greeting", msg);
  }
  
}
