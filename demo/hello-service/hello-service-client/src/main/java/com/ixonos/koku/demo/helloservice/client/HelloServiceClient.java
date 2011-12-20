package com.ixonos.koku.demo.helloservice.client;

import java.util.Date;

import javax.naming.InitialContext;

import com.ixonos.koku.demo.helloservice.api.HelloServiceRemote;

public class HelloServiceClient {

  public static void main(String ... args) throws Exception {
    
    InitialContext ctx = new InitialContext();
    HelloServiceRemote hs = (HelloServiceRemote) ctx.lookup("HelloService");
    System.out.println("greeting: "+hs.sayHello("world (at "+new Date()+")"));
  }

}
