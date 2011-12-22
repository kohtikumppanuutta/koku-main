package fi.koku.tools.dataimport;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;
import fi.koku.calendar.CalendarUtil;
import fi.koku.services.entity.customer.v1.AddressType;
import fi.koku.services.entity.customer.v1.AddressesType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.ElectronicContactInfoType;
import fi.koku.services.entity.customer.v1.ElectronicContactInfosType;
import fi.koku.services.entity.customer.v1.PhoneNumberType;
import fi.koku.services.entity.customer.v1.PhoneNumbersType;
import fi.koku.tools.dataimport.model.ChildAndParents;
import fi.koku.tools.dataimport.model.Employee;

public class InputParser {
    
  private static final  String EMAIL = "email";
  private static final  String KOTIPUHELIN = "kotipuhelin";
  private static final  String GSM = "gsm";
  private static final  String VIRALLINEN = "virallinen";
  private static final  String ELOSSA = "elossa";
  private static final String TURVAKIELTO = "TURVAKIELTO";
  
  
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
        employees.add(employee);

        userIDs.add(employee.getUserId());
      }
    }
    return employees;
  } 
  
  public static Collection<ChildAndParents> effica(CSVReader reader) throws Exception {
    Collection<ChildAndParents> childAndParents = new LinkedHashSet<ChildAndParents>();
    
    String[] l;
    while ((l = reader.readNext()) != null) {

      // remove whitespace
      for (int i = 0; i < l.length; i++) {
        l[i] = l[i].trim();
      }
      
      ChildAndParents cap = new ChildAndParents();
     
      cap.setChildUnit(l[Columns.EFFICA_CHILD_UNIT]);
      cap.setChildGroup(l[Columns.EFFICA_CHILD_GROUP]);
      
      // Create child customer
      cap.setChild(createCustomer(l[Columns.EFFICA_CHILD_PIC], l[Columns.EFFICA_CHILD_BIRTH_DATE], l[Columns.EFFICA_CHILD_LASTNAME],
          Utils.getFirstName(l[Columns.EFFICA_CHILD_FIRSTNAMES]), l[Columns.EFFICA_CHILD_FIRSTNAMES],
          l[Columns.EFFICA_CHILD_KANSALAISUUSKOODI], l[Columns.EFFICA_CHILD_KUNTAKOODI],
          l[Columns.EFFICA_CHILD_KIELIKOODI], removeTurvaKielto(l[Columns.EFFICA_CHILD_ADDRESS]),
          l[Columns.EFFICA_CHILD_POST_OFFICE], l[Columns.EFFICA_CHILD_POST_CODE], l[Columns.EFFICA_CHILD_PHONE], null,
          null, null));

      // create 'päämies'
      cap.setPm_1(createCustomer(l[Columns.EFFICA_PM_PIC], getBirthDate(l[Columns.EFFICA_PM_PIC]), l[Columns.EFFICA_PM_LASTNAME],
          Utils.getFirstName(l[Columns.EFFICA_PM_FIRSTNAMES]), l[Columns.EFFICA_PM_FIRSTNAMES],
          l[Columns.EFFICA_PM_KANSALAISUUSKOODI], l[Columns.EFFICA_PM_KUNTAKOODI], l[Columns.EFFICA_PM_KIELIKOODI],
          l[Columns.EFFICA_PM_ADDRESS], l[Columns.EFFICA_PM_POST_OFFICE], l[Columns.EFFICA_PM_POST_CODE],
          l[Columns.EFFICA_PM_PHONE], l[Columns.EFFICA_PM_PHONE_2], l[Columns.EFFICA_PM_EMAIL],
          l[Columns.EFFICA_PM_EMAIL_2]));

      // Create 'puoliso'
      if (l[Columns.EFFICA_PM_2_PIC] != null && l[Columns.EFFICA_PM_2_PIC].length() > 0) {
        cap.setPm_2(createCustomer(l[Columns.EFFICA_PM_2_PIC], getBirthDate(l[Columns.EFFICA_PM_2_PIC]),
            l[Columns.EFFICA_PM_2_LASTNAME], Utils.getFirstName(l[Columns.EFFICA_PM_2_FIRSTNAMES]),
            l[Columns.EFFICA_PM_2_FIRSTNAMES], l[Columns.EFFICA_PM_2_KANSALAISUUSKOODI],
            l[Columns.EFFICA_PM_2_KUNTAKOODI], l[Columns.EFFICA_PM_2_KIELIKOODI], l[Columns.EFFICA_PM_2_ADDRESS],
            l[Columns.EFFICA_PM_2_POST_OFFICE], l[Columns.EFFICA_PM_2_POST_CODE], l[Columns.EFFICA_PM_2_PHONE], null,
            l[Columns.EFFICA_PM_2_EMAIL], l[Columns.EFFICA_PM_2_EMAIL_2]));
      } 
      
      childAndParents.add(cap);
    }
    return childAndParents;
  }
  
  private static CustomerType createCustomer(String pic, String syntymaaika, String lastName, String firstname, String firstnames,
      String kansalaisuuskoodi, String kuntakoodi, String kielikoodi, String katunimi, String postiToimipaikka,
      String postinumero, String puhelinnumero, String matkapuhelin, String email, String email2) throws Exception {

    CustomerType customer = new CustomerType();
    customer.setStatus(ELOSSA);
    customer.setStatusDate(CalendarUtil.getXmlDate(new Date()));
    customer.setHenkiloTunnus(pic);
    customer.setSyntymaPvm(parseBirthDateToCal(syntymaaika, pic));
    customer.setSukuNimi(lastName);
    customer.setEtuNimi(firstname);
    customer.setEtunimetNimi(firstnames);
    // TODO set real data
    if (kansalaisuuskoodi != null && kansalaisuuskoodi.length() < 4) {
      customer.setKansalaisuusKoodi(kansalaisuuskoodi);
    } else {
      customer.setKansalaisuusKoodi("FI");
    }
    // TODO these are not codes, just text, FIX?
    if (kuntakoodi.length() > 10) {
      kuntakoodi = kuntakoodi.substring(0, 10);
    }
    customer.setKuntaKoodi(kuntakoodi);
    // TODO these are not codes, just text, FIX?
    if (kielikoodi.length() > 10) {
      kielikoodi = kielikoodi.substring(0, 10);
    }
    customer.setKieliKoodi(kielikoodi);
    // TODO set real data
    customer.setTurvakieltoKytkin(false);

    AddressType address = new AddressType();
    // TODO set real data
    address.setAddressType(VIRALLINEN);
    address.setKatuNimi(katunimi);
    if (postiToimipaikka == null || postiToimipaikka.length() == 0) {
      address.setPostitoimipaikkaNimi("");
    } else {
      address.setPostitoimipaikkaNimi(postiToimipaikka);
    }
    if (postinumero == null || postinumero.length() == 0) {
      address.setPostinumeroKoodi("");
    } else {
      address.setPostinumeroKoodi(postinumero);
    }
    // TODO set real data
    address.setPostilokeroTeksti("");
    address.setMaatunnusKoodi("");

    AddressesType adresses = new AddressesType();
    adresses.getAddress().add(address);
    customer.setAddresses(adresses);

    PhoneNumbersType numbersType = new PhoneNumbersType();
    if (matkapuhelin != null && matkapuhelin.length() > 0) {
      addPhoneNumber(matkapuhelin, numbersType);
    } else if (puhelinnumero != null && puhelinnumero.length() > 0) {
      addPhoneNumber(puhelinnumero, numbersType);
    }
    customer.setPhoneNumbers(numbersType);

    ElectronicContactInfosType eContactInfos = new ElectronicContactInfosType();
    if (email != null && email.length() > 0) {
      addEmail(email, eContactInfos);
    } else if (email2 != null && email2.length() > 0) {
      addEmail(email2, eContactInfos);
    }           
    customer.setElectronicContactInfos(eContactInfos);

    return customer;
}
  
  private static String removeTurvaKielto(String text) {
    if (text != null && TURVAKIELTO.equals(text)) {
      return "";
    } else {
      return text;
    }
  }
  
  private static void addPhoneNumber(String puhelinnumero, PhoneNumbersType numbersType) {
    PhoneNumberType phoneNumberType = new PhoneNumberType();
    // TODO set real data
    phoneNumberType.setNumberClass(GSM);
    // TODO set real data
    phoneNumberType.setNumberType(KOTIPUHELIN);
    phoneNumberType.setPuhelinnumeroTeksti(puhelinnumero);
    numbersType.getPhone().add(phoneNumberType);
  }

  private static void addEmail(String email, ElectronicContactInfosType eContactInfos) {
    ElectronicContactInfoType eContact = new ElectronicContactInfoType();
    eContact.setContactInfoType(EMAIL);
    eContact.setContactInfo(email);
    eContactInfos.getEContactInfo().add(eContact);
  }

  private static String getBirthDate(String pic) throws Exception {

    if (pic == null) {
      throw new Exception("Pic is invalid: " + pic);
    }

    if (pic.contains("-")) {
      return pic.substring(0, pic.indexOf("-"));
    }

    else if (pic.contains("A") || pic.contains("a")) {
      String temp = pic.toLowerCase();
      return temp.substring(0, temp.indexOf("a"));
    }

    else {
      throw new Exception("Pic is invalid: " + pic);
    }
  }
  
  private static XMLGregorianCalendar parseBirthDateToCal(String syntymaaika, String pic) throws Exception {
    if (syntymaaika == null || syntymaaika.length() != 6) {
      throw new Exception("Syntymäaika was not valid: " + syntymaaika);
    }

    XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
    cal.setDay(Integer.parseInt(syntymaaika.substring(0, 2)));
    cal.setMonth(Integer.parseInt(syntymaaika.substring(2, 4)));
    int year = Integer.parseInt(syntymaaika.substring(4, 6));

    if (pic != null) {
      if (pic.contains("-")) {
        year = year + 1900;
      } else {
        year = year + 2000;
      }
    }
    cal.setYear(year);
    return cal;
  }
}
