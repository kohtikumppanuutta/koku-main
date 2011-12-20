package com.ixonos.koku.demo.helloservice.api;

import javax.ejb.Remote;

@Remote
public interface HelloServiceRemote {
  String sayHello(String who);
}
