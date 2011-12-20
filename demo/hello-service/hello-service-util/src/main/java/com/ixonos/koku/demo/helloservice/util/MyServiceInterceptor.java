package com.ixonos.koku.demo.helloservice.util;

import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyServiceInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(MyServiceInterceptor.class);
  
  @PostConstruct
  @PostActivate
  public void autowireBean(InvocationContext invocationContext) {
    logger.info("## autowireBean()");
  }
}
