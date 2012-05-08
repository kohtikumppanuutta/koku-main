/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
package fi.koku.encoding.controller;

import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.encoding.model.Test;

/**
 * Main controller (handler request that have no action)
 * 
 * @author Ixonos / tuomape
 *
 */
@Controller("mainController")
@RequestMapping(value = "VIEW")
public class MainController {


  @RenderMapping
  public String render(PortletSession session, RenderRequest req, Model model) {

      return "test";
  }

 
  @RenderMapping(params = "action=showText")
  public String show(PortletSession session, RenderResponse response,
      @RequestParam(value = "text", required = false) String text,
      Model model) {;
       
    Test t = new Test();
    t.setText(text );
    model.addAttribute("test",t );

    return "test";
  }

  @ActionMapping(params = "action=setText")
  public void fecthChild(PortletSession session, @ModelAttribute(value = "test") Test test,
      BindingResult bindingResult, ActionResponse response, SessionStatus sessionStatus) {
 
      response.setRenderParameter("action", "showText");
      response.setRenderParameter("text", test.getText());
    sessionStatus.setComplete();
  }


  @ModelAttribute("test")
  public Test getCommandObject() {
    return new Test();
  }

}
