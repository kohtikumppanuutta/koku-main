package fi.koku.tools.dataimport.model;

import fi.arcusys.tampere.hrsoa.entity.User;

public class Employee extends User{

  private String group;
  private String unit;
  private String unitGroup;
  private String jobTitle;
  private String registry;
  
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

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getUnitGroup() {
    return unitGroup;
  }

  public void setUnitGroup(String unitGroup) {
    this.unitGroup = unitGroup;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getRegistry() {
    return registry;
  }

  public void setRegistry(String registry) {
    this.registry = registry;
  }
}