package fi.koku.tools.dataimport.model;

import fi.arcusys.tampere.hrsoa.entity.User;

public class Employee extends User{

  private String group;
  
  public Employee() {
  }

  public Employee(User source, String group) {
    this.userId = source.getUserId();
    this.ssn = source.getSsn();
    this.firstName = source.getFirstName();
    this.lastName = source.getLastName();
    this.email = source.getEmail();
    this.group = group;
  }
  
  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }
  
  
  
}
