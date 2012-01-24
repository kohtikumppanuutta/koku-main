package fi.koku.tools.dataimport;

import java.util.Collection;
import java.util.LinkedHashSet;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;
import fi.koku.tools.dataimport.model.Employee;

public class InputParser {
  
  public static Collection<Employee> employeeFromKahva(CSVReader reader, WSCaller caller) throws Exception{
    Collection<String> employeeIDs = new LinkedHashSet<String>();
    Collection<Employee> employees = new LinkedHashSet<Employee>();
    String[] l;
    while ((l = reader.readNext()) != null) {

      // remove whitespace
      for (int i = 0; i < l.length; i++) {
        l[i] = l[i].trim();
      }

      User user = caller.getUserById(l[Columns.EMPLOYEE_FRMT_1_ID]);
      if (user == null) {
        throw new Exception("User by ID: " + l[Columns.EMPLOYEE_FRMT_1_ID] + " cannot be found");
      } else {
        // try to avoid duplicates
        if (!employeeIDs.contains(user.getUserId())) {        
          employees.add(new Employee(user, l[Columns.EMPLOYEE_FRMT_1_GROUP]));         
          employeeIDs.add(user.getUserId());  
        }
      }
    }
    return employees;
  }
  
  public static Collection<Employee> employeeFromCSV(CSVReader reader) throws Exception {
    Collection<String> userIDs = new LinkedHashSet<String>();
    Collection<Employee> employees = new LinkedHashSet<Employee>();
    String[] l;
    while ((l = reader.readNext()) != null) {

      // remove whitespace
      for (int i = 0; i < l.length; i++) {
        l[i] = l[i].trim();
      }

      // try to avoid duplicates
      if (!userIDs.contains(l[Columns.EMPLOYEE_ID])) {
        Employee employee = new Employee();
        employee.setUserId(l[Columns.EMPLOYEE_ID]);
        employee.setSsn(l[Columns.EMPLOYEE_PIC]);
        employee.setFirstName(l[Columns.EMPLOYEE_FIRSTNAME]);
        employee.setLastName(l[Columns.EMPLOYEE_LASTNAME]);
        employee.setEmail(l[Columns.EMPLOYEE_EMAIL]);
        employee.setGroup(l[Columns.EMPLOYEE_GROUP]);      
        employee.setUnit(l[Columns.EMPLOYEE_UNIT]);
        employee.setUnitGroup(l[Columns.EMPLOYEE_UNIT_GROUP]);
        employee.setJobTitle(l[Columns.EMPLOYEE_JOB_TITLE]);
        employees.add(employee);

        userIDs.add(employee.getUserId());
      }
    }
    return employees;
  } 
  
  // TODO add parsing for customers also
}