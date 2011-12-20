package com.ixonos.koku.demo.helloservice.impl;

import javax.ejb.Stateless;
import javax.jws.WebService;
import com.ixonos.services.hello.v1.HelloServicePortType;

@Stateless
@WebService(wsdlLocation="META-INF/wsdl/helloService.wsdl",
    endpointInterface="com.ixonos.services.hello.v1.HelloServicePortType",
    targetNamespace="http://services.ixonos.com/hello/v1",
    portName="helloService-soap11-port",
    serviceName="helloService"
)
public class HelloServiceEndpointBean implements HelloServicePortType {

  public String opGetGreeting(String addressee) {
    return "foobar";
  }

}
