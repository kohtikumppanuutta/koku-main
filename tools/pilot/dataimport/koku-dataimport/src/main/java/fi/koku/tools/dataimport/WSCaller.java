package fi.koku.tools.dataimport;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;
import fi.arcusys.tampere.hrsoa.ws.ldap.LdapService;
import fi.arcusys.tampere.hrsoa.ws.ldap.LdapService_Service;
import fi.koku.services.common.kahva.LdapServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.customer.v1.AuditInfoType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;

public class WSCaller {

  private static final String CUSTOMER_SERVICE_USER_ID = "marko";
  private static final String CUSTOMER_SERVICE_PASSWORD = "marko";
  private static final String CUSTOMER_ENDPOINT = "http://localhost:8180/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";
  
  private static final String KAHVA_ENDPOINT = "http://localhost:8180/kahvaservice-mock-ear-0.0.1-SNAPSHOT-kahvaservice-mock-0.0.3-SNAPSHOT/KahvaServiceEndpointBean";
  
  public void addCustomers(CSVReader reader) throws Exception{
    
  }
    
  public void customerService() throws Exception {
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(CUSTOMER_SERVICE_USER_ID,
        CUSTOMER_SERVICE_PASSWORD, CUSTOMER_ENDPOINT);
    CustomerServicePortType service = customerServiceFactory.getCustomerService();

    AuditInfoType type = new AuditInfoType();
    type.setComponent("moro");
    type.setUserId("444444-4444");

    CustomerType cust = service.opGetCustomer("444444-4444", type);
    System.out.println(cust.getEtunimetNimi());
  }
  
  public void communityService() throws Exception {
    CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory(CUSTOMER_SERVICE_USER_ID,
        CUSTOMER_SERVICE_PASSWORD, CUSTOMER_ENDPOINT);
    CommunityServicePortType service = communityServiceFactory.getCommunityService();

    fi.koku.services.entity.community.v1.AuditInfoType type = new fi.koku.services.entity.community.v1.AuditInfoType();
    type.setComponent("moro");
    type.setUserId("444444-4444");

    CommunityType comm = service.opGetCommunity("1", type);
    System.out.println(comm.getName());
  }
  
  public void kahvaService() throws Exception{      
    URL wsdlLocation = new URL(KAHVA_ENDPOINT+"?wsdl");
    
    LdapService_Service service = new LdapService_Service(wsdlLocation, new QName(
        "http://www.arcusys.fi/tampere/hrsoa/ws/ldap/", "ldapService"));
    LdapService port = service.getLdapServiceSOAP();
    ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, KAHVA_ENDPOINT);
    
    User user = port.getUserById("nelli.neuvola");    
    System.out.println(user);
  }
}