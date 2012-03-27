package fi.koku.tools.dataimport;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import fi.arcusys.tampere.hrsoa.entity.User;
import fi.arcusys.tampere.hrsoa.ws.ldap.LdapService;
import fi.koku.calendar.CalendarUtil;
import fi.koku.services.common.kahva.LdapServiceFactory;
import fi.koku.services.entity.community.v1.CommunitiesType;
import fi.koku.services.entity.community.v1.CommunityQueryCriteriaType;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberPicsType;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.community.v1.MembersType;
import fi.koku.services.entity.customer.v1.AddressType;
import fi.koku.services.entity.customer.v1.AddressesType;
import fi.koku.services.entity.customer.v1.AuditInfoType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.ElectronicContactInfoType;
import fi.koku.services.entity.customer.v1.ElectronicContactInfosType;
import fi.koku.services.entity.customer.v1.PhoneNumberType;
import fi.koku.services.entity.customer.v1.PhoneNumbersType;

public class WSCaller {
  
  private static final String DEPENDANT = "dependant";
  private static final String GUARDIAN = "guardian";
  private static final String EMAIL = "email";
  private static final String KOTIPUHELIN = "kotipuhelin";
  private static final String GSM = "gsm";
  private static final String VIRALLINEN = "virallinen";
  private static final String ELOSSA = "elossa";
  private static final String GUARDIAN_COMMUNITY = "guardian_community";
  private static final String CUSTOMER_SERVICE_USER_ID = "marko";
  private static final String CUSTOMER_SERVICE_PASSWORD = "marko";
  
  //private static final String CUSTOMER_ENDPOINT = "http://kohtikumppanuutta-dev.dmz:8180/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";
  //private static final String CUSTOMER_ENDPOINT = "http://localhost:35353/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";
  //private static final String CUSTOMER_ENDPOINT = "http://localhost:23232/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";
  //TÄMÄ OLI KÄYTÖSSÄ private static String CUSTOMER_ENDPOINT = "http://localhost:8180/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";
  private static String CUSTOMER_ENDPOINT;
  //private static final String CUSTOMER_ENDPOINT = "http://localhost:11000/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";
  //private static final String CUSTOMER_ENDPOINT = "http://localhost:13131/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";
  
  private static String KAHVA_ENDPOINT;
  //TÄMÄ OLI KÄYTÖSSÄ private static String KAHVA_ENDPOINT = "http://localhost:8180/kahvaservice-mock-ear-0.0.1-SNAPSHOT-kahvaservice-mock-0.0.3-SNAPSHOT/KahvaServiceEndpointBean";
  //private static final String KAHVA_ENDPOINT = "http://localhost:10000/tampere-services/ldapService";

  private LdapService ldapService;
  private CustomerServicePortType customerService;
  private CommunityServicePortType communityService;

  private Properties properties;
  
  public WSCaller() throws IOException {
	// Read properties file.
	  properties = new Properties();
	  try {
		  //the base folder is ./, the root of the main.properties file
		  properties.load(new FileInputStream("./customer.properties"));
		  System.out.println("customer.properties-file loaded successfully.");
	  } catch (IOException e) {
		  System.err.println("Could not open customer.properties -file.");
	      e.printStackTrace();
	  }
	// get customer endpoint from customer.properties-file
  	CUSTOMER_ENDPOINT = properties.getProperty("CUSTOMER_ENDPOINT");
   // get customer endpoint from customer.properties-file
	KAHVA_ENDPOINT = properties.getProperty("KAHVA_ENDPOINT");
	if (CUSTOMER_ENDPOINT==null | KAHVA_ENDPOINT==null )
	{
		throw new IOException("\nCUSTOMER_ENDPOINT or KAHVA_ENDPOINT -properties not defined.");
	}	
  }
  
  public User getUserById(String userID) throws Exception {
    try {
      return getLdapService().getUserById(userID);
    } catch (Exception e) {
      System.err.println("Could not fetch user by ID: " + userID);
      e.printStackTrace();
      return null;
    }
  }

  public CustomerType getCustomerByPic(String pic) {
    try {
      return getCustomerService().opGetCustomer(pic, getCustomerAuditHeader());
    } catch (Exception e) {
      return null;
    }
  }

  public String addCustomer(String pic, String syntymaaika, String lastName, String firstname, String firstnames,
      String kansalaisuuskoodi, String kuntakoodi, String kielikoodi, String katunimi, String postiToimipaikka,
      String postinumero, String puhelinnumero, String matkapuhelin, String email, String email2) throws Exception {

    CustomerType customer = new CustomerType();
    customer.setStatus(ELOSSA);
    customer.setStatusDate(CalendarUtil.getXmlDate(new Date()));
    customer.setHenkiloTunnus(pic);
    customer.setSyntymaPvm(parseBirthDate(syntymaaika, pic));
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

    return getCustomerService().opAddCustomer(customer, getCustomerAuditHeader());
    // getCustomerService().opGetCustomer("444444-4444",
  }

  public CommunityType getCommunity(String guardianPic) throws Exception {
    MemberPicsType pics = new MemberPicsType();
    pics.getMemberPic().add(guardianPic);

    CommunityQueryCriteriaType criteria = new CommunityQueryCriteriaType();
    criteria.setMemberPics(pics);
    criteria.setCommunityType(GUARDIAN_COMMUNITY);

    CommunitiesType comms = getCommunityService().opQueryCommunities(criteria, getCommunityAuditHeader());

    if (comms.getCommunity().size() > 1) {  
      System.out.println("the guardian with pic:" + guardianPic + " has too many guardian communities, selecting first");
    }

    if (comms.getCommunity().isEmpty()) {
      return null;
    }

    return comms.getCommunity().get(0);
  }

  public String createCommunity(String communityName, String guardianPic) throws Exception {
    CommunityType community = new CommunityType();
    community.setType(GUARDIAN_COMMUNITY);
    community.setName(communityName);

    MembersType members = new MembersType();
    MemberType guardian = new MemberType();
    guardian.setRole(GUARDIAN);
    guardian.setPic(guardianPic);

    members.getMember().add(guardian);
    community.setMembers(members);

    return getCommunityService().opAddCommunity(community, getCommunityAuditHeader());
  }

  public void updateCommunity(String communityID, String dependantPic) throws Exception {
    CommunityType community = getCommunityService().opGetCommunity(communityID, getCommunityAuditHeader());   
    MembersType members = community.getMembers();
    
    // do not add duplicates
    if (!members.getMember().contains(dependantPic)) {
      MemberType member = new MemberType();
      member.setRole(DEPENDANT);
      member.setPic(dependantPic);
      members.getMember().add(member);

      getCommunityService().opUpdateCommunity(community, getCommunityAuditHeader());
    }
  }

  private fi.koku.services.entity.community.v1.AuditInfoType getCommunityAuditHeader() {
    fi.koku.services.entity.community.v1.AuditInfoType auditInfo = new fi.koku.services.entity.community.v1.AuditInfoType();
    auditInfo.setComponent("koku-dataimport");
    // TODO real user id?
    auditInfo.setUserId("444444-4444");
    return auditInfo;
  }

  private AuditInfoType getCustomerAuditHeader() {
    AuditInfoType auditinfo = new AuditInfoType();
    auditinfo.setComponent("koku-dataimport");
    // TODO real user id?
    auditinfo.setUserId("444444-4444");
    return auditinfo;
  }

  private void addPhoneNumber(String puhelinnumero, PhoneNumbersType numbersType) {
    PhoneNumberType phoneNumberType = new PhoneNumberType();
    // TODO set real data
    phoneNumberType.setNumberClass(GSM);
    // TODO set real data
    phoneNumberType.setNumberType(KOTIPUHELIN);
    phoneNumberType.setPuhelinnumeroTeksti(puhelinnumero);
    numbersType.getPhone().add(phoneNumberType);
  }

  private void addEmail(String email, ElectronicContactInfosType eContactInfos) {
    ElectronicContactInfoType eContact = new ElectronicContactInfoType();
    eContact.setContactInfoType(EMAIL);
    eContact.setContactInfo(email);
    eContactInfos.getEContactInfo().add(eContact);
  }

  private XMLGregorianCalendar parseBirthDate(String syntymaaika, String pic) throws Exception {    
    String source = null;    
    if (syntymaaika == null || syntymaaika.length() == 6) {
      source = pic;
    } else {
      source = syntymaaika;
    }

    XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
    cal.setDay(Integer.parseInt(source.substring(0, 2)));
    cal.setMonth(Integer.parseInt(source.substring(2, 4)));
    int year = Integer.parseInt(source.substring(4, 6));

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

  private LdapService getLdapService() throws Exception {
    if (ldapService == null) {
      LdapServiceFactory f = new LdapServiceFactory(KAHVA_ENDPOINT);
      ldapService = f.getOrganizationService();
    }
    return ldapService;
  }

  private CustomerServicePortType getCustomerService() throws Exception {
    if (customerService == null) {
      CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(CUSTOMER_SERVICE_USER_ID,
          CUSTOMER_SERVICE_PASSWORD, CUSTOMER_ENDPOINT);
      customerService = customerServiceFactory.getCustomerService();
    }
    return customerService;
  }

  private CommunityServicePortType getCommunityService() throws Exception {
    if (communityService == null) {
      CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory(CUSTOMER_SERVICE_USER_ID,
          CUSTOMER_SERVICE_PASSWORD, CUSTOMER_ENDPOINT);
      communityService = communityServiceFactory.getCommunityService();
    }
    return communityService;
  }
}