package fi.koku.tools.dataimport;

import fi.arcusys.tampere.hrsoa.entity.User;
import fi.arcusys.tampere.hrsoa.ws.ldap.LdapService;
import fi.koku.services.common.kahva.LdapServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.customer.v1.AuditInfoType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;

public class WSCaller {

  private static final String GUARDIAN_COMMUNITY = "guardian_community";
  private static final String CUSTOMER_SERVICE_USER_ID = "marko";
  private static final String CUSTOMER_SERVICE_PASSWORD = "marko";
  private static final String CUSTOMER_ENDPOINT = "http://localhost:8180/customer-service-ear-0.0.1-SNAPSHOT-customer-service-0.0.1-SNAPSHOT";  
  private static final String KAHVA_ENDPOINT = "http://localhost:8180/kahvaservice-mock-ear-0.0.1-SNAPSHOT-kahvaservice-mock-0.0.3-SNAPSHOT/KahvaServiceEndpointBean";
  
  private LdapService ldapService;
  private CustomerServicePortType customerService;
  private CommunityServicePortType communityService;
  
  public User getUserById(String userID) throws Exception {
    return getLdapService().getUserById(userID);
  }

  public void addCustomer() throws Exception{   
    AuditInfoType auditinfo = new AuditInfoType();
    auditinfo.setComponent("koku-dataimport");  
    // TODO tarkasta user id
    auditinfo.setUserId("444444-4444");
          
    CustomerType customer = new CustomerType();
    //customer.set
    //customer.set
    //customer.set
    getCustomerService().opAddCustomer(customer, auditinfo);
  }
  
  public void addToGuardianCommunity(String role, String pic) throws Exception {
    fi.koku.services.entity.community.v1.AuditInfoType auditinfo = new fi.koku.services.entity.community.v1.AuditInfoType();
    auditinfo.setComponent("koku-dataimport");
    // TODO tarkasta user id
    auditinfo.setUserId("444444-4444");

    CommunityType community = getCommunityService().opGetCommunity(GUARDIAN_COMMUNITY, auditinfo);
    MemberType member = new MemberType();
    member.setRole(role);
    member.setPic(pic);
    community.getMembers().getMember().add(member);
    getCommunityService().opUpdateCommunity(community, auditinfo);
  }
  
  private LdapService getLdapService() throws Exception {
    if (ldapService == null) {
      LdapServiceFactory f = new LdapServiceFactory(KAHVA_ENDPOINT);
      ldapService = f.getOrganizationService();
    }
    return ldapService;
  }
  
  private CustomerServicePortType getCustomerService() throws Exception {
    if (customerService != null) {
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
  
  
  /*   
  Customer(kuntalainen) kentät lapselle
  "status",                 -
  "statusDate",             -
  "henkiloTunnus",          Lapsen henkilötunnus
  "syntymaPvm",             Lapsen syntymäaika
  "sukuNimi",               Lpasen nimi (parsitaanko pilkulla erotettuna?)
  "etuNimi",                Lpasen nimi (parsitaanko pilkulla erotettuna?)
  "etunimetNimi",           -
  "kansalaisuusKoodi",      -
  "kuntaKoodi",             Kotikunta (ei ole koodi vaan teksti esim. "Tampere")
  "kieliKoodi",             -
  "turvakieltoKytkin",      -
  "addresses",              Lapsen osoite + Lapsen postinumero + Lapsen postitoimipaikka
  "phoneNumbers",           -
  "electronicContactInfos"  -
  
  Customer(kuntalainen) kentät päämiehelle
  "status",                 -
  "statusDate",             -
  "henkiloTunnus",          -
  "syntymaPvm",             -
  "sukuNimi",               Päämiehen nimi (parsitaanko pilkulla erotettuna?)
  "etuNimi",                Päämiehen nimi (parsitaanko pilkulla erotettuna?)
  "etunimetNimi",           -
  "kansalaisuusKoodi",      -
  "kuntaKoodi",             -
  "kieliKoodi",             -
  "turvakieltoKytkin",      -
  "addresses",              Päämiehen osoite + Päämiehen postinumero + Päämiehen postitoimipaikka
  "phoneNumbers",           Päämiehen Puhelin
  "electronicContactInfos"  Päämiehen Sähköposti
  
  Customer(kuntalainen) kentät puolisolle
  "status",                 -
  "statusDate",             -
  "henkiloTunnus",          -
  "syntymaPvm",             -
  "sukuNimi",               Puolison nimi (parsitaanko pilkulla erotettuna?)
  "etuNimi",                Puolison nimi (parsitaanko pilkulla erotettuna?)
  "etunimetNimi",           -
  "kansalaisuusKoodi",      -
  "kuntaKoodi",             -
  "kieliKoodi",             -
  "turvakieltoKytkin",      -
  "addresses",              -
  "phoneNumbers",           Puolison matkapuhelin
  "electronicContactInfos"  Puolison sähköposti


  LDIF Lapselle
    # kalle.kuntalainen, People, koku, example.org
    dn: cn=${portal.user.name},ou=People,o=koku,dc=example,dc=org 
    cn: ${portal.user.name}         -
    givenName: ${user.firstname}    Lpasen nimi (parsitaanko pilkulla erotettuna?)
    objectClass: inetOrgPerson
    objectClass: top
    sn: ${user.lasname}             Lpasen nimi (parsitaanko pilkulla erotettuna?)
    userPassword: test
    mail: ${user.email}             -
    uid: ${user.pic}                Lapsen henkilötunnus

 LDIF päämiehelle
    # kalle.kuntalainen, People, koku, example.org
    dn: cn=${portal.user.name},ou=People,o=koku,dc=example,dc=org 
    cn: ${portal.user.name}         -
    givenName: ${user.firstname}    Päämiehen nimi (parsitaanko pilkulla erotettuna?)
    objectClass: inetOrgPerson
    objectClass: top
    sn: ${user.lasname}             Päämiehen nimi (parsitaanko pilkulla erotettuna?)
    userPassword: test
    mail: ${user.email}             Päämiehen Sähköposti
    uid: ${user.pic}                -
    
 LDIF puolisolle
    # kalle.kuntalainen, People, koku, example.org
    dn: cn=${portal.user.name},ou=People,o=koku,dc=example,dc=org 
    cn: ${portal.user.name}         -
    givenName: ${user.firstname}    Puolison nimi (parsitaanko pilkulla erotettuna?)
    objectClass: inetOrgPerson
    objectClass: top
    sn: ${user.lasname}             Puolison nimi (parsitaanko pilkulla erotettuna?)
    userPassword: test
    mail: ${user.email}             Puolison sähköposti
    uid: ${user.pic}                -
    

   */
  
  
}