package com.ixonos.koku.demo.helloservice.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.ixonos.koku.demo.helloservice.api.HelloServiceRemote;
//import com.ixonos.koku.demo.helloservice.util.MyServiceInterceptor;


@Stateless(mappedName="HelloService")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class HelloServiceBean implements HelloServiceRemote {
  private static final Logger logger = LoggerFactory.getLogger(HelloServiceBean.class);
  
  @Autowired
  private MessageBean mb;

  public HelloServiceBean() {
    logger.info("HelloServiceBean()");
  }

  public String sayHello(String who) {
    logger.info("sayHello()");
    logger.info("mb: "+mb.getMessage());
    return "hello, "+who;
  }

}
