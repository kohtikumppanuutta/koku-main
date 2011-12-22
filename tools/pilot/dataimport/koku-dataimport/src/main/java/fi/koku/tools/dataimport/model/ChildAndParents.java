package fi.koku.tools.dataimport.model;

import fi.koku.services.entity.customer.v1.CustomerType;

public class ChildAndParents {

  private CustomerType child;
  private CustomerType pm_1;
  private CustomerType pm_2;
  private String childUnit;
  private String childGroup;

  public CustomerType getChild() {
    return child;
  }

  public void setChild(CustomerType child) {
    this.child = child;
  }

  public CustomerType getPm_1() {
    return pm_1;
  }

  public void setPm_1(CustomerType pm_1) {
    this.pm_1 = pm_1;
  }

  public CustomerType getPm_2() {
    return pm_2;
  }

  public void setPm_2(CustomerType pm_2) {
    this.pm_2 = pm_2;
  }

  public String getChildUnit() {
    return childUnit;
  }

  public void setChildUnit(String childUnit) {
    this.childUnit = childUnit;
  }

  public String getChildGroup() {
    return childGroup;
  }

  public void setChildGroup(String childGroup) {
    this.childGroup = childGroup;
  }
  
  

}
