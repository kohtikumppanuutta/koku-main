This is a data importer tool for Kohti kumppanuutta -project. The aimed use of this tool
is to read predefined CSV format data and create employees and customers from that data.

NOTE!
Webservice addresses can be configured in WSCaller.java.

The data formats currently supported are named Employee, Effica Customer and Helmi Customer.

Employee:
- Selecting Employee functionality from the UI creates the files for updating the LDAP.
  
- Selecting Employee PICs functionality from the UI generates list of employee PICs so
  they can be checked against existing data.

LDIF
- columns: userid, group
- user id is used to get the user information from LDAP service using a web service.
- group is used to recognize the users that belong to select groups. Currently following
  groups are checked and employees are added to corresponding groups:
  - Kouluterveydenhuollon työntekijä
  - Päiväkodin työntekijä
  - Neuvolan työntekijä
- if different groups are needed the method LDIFWriter.writeEmployeeLDIF() needs to be modified.
- all employees are added to group "virkailija" in LDAP
- as the result, following files are created:
  - Employee.ldif, contains creation clauses just for employees.  
  - Employee_structure.ldif, contains the structure of the LDAP needed to add the information.
  - EmployeeGroup.ldif, contains "virkailija" group update clause.
  - employeeRegistry_Kouluterveydenhuollon työntekijä.ldif
  - employeeRegistry_Päiväkodin työntekijä.ldif
  - employeeRegistry_Neuvolan työntekijä.ldif
  - employeeOrgUnit_Kouluterveydenhuollon työntekijä.ldif
  - employeeOrgUnit_Päiväkodin työntekijä.ldif
  - employeeOrgUnit_Neuvolan työntekijä.ldif
  - Employee_all.ldif, contains all previous files contents, excluding the structure creation
  - Optionally the file NotFoundEmployeeIDS.txt is written if there are any employees whose
    information is not found from the webservice. The file contains the employee userid:s which
    were not found.

Effica Customer
- Selecting Effica Customer functionality from the UI adds the customers to customer service and 
  creates the files for updating the LDAP.
  
- Selecting Effica Customer PICs functionality from the UI generates list of child and guardian
  PICs so they can be checked against existing data.

LDIF
- columns: NOT_USED, NOT_USED, child unit, child group, child pic, NOT_USED, child lastname, 
  child firstnames,NOT_USED, NOT_USED, NOT_USED, NOT_USED, NOT_USED, NOT_USED, NOT_USED, child phone
  guardian PIC, guardian lastname, guardian firstnames, NOT_USED, NOT_USED, NOT_USED, guardian email,
  NOT_USED, guardian phone, guardian GSM
- all customers are added to group "kuntalainen" in LDAP
- all children are additionally added to groups in LDAP. The group name is created by concatenating
  child unit and child group and removing all ',' and '/' characters.
- as a result following files for LDAP are created
  - Effica_customer_structure.ldif, contains the structure of the LDAP needed to add the information
  - EfficaCustomer.ldif, contains creation clauses just for customers.  
  - EfficaCustomerGroup.ldif, contains "kuntalainen" group update clause.
  - Child group files named after groupname + .ldif
  - Effica_All.ldif, contains all previous files contents, excluding the structure creation
  
WebService
- columns: NOT_USED, NOT_USED, NOT_USED, NOT_USED, child pic, child birth date, child lastname, 
  child firstnames, child kuntakoodi, child language, child nationality, NOT_USED, child address,
  child post code, child post office, child phone, guardian PIC, guardian lastname, guardian firstnames,
  guardian address, guardian post code, guardian post office, guardian email, guardian email 2,
  guardian phone, guardian GSM
- uses a webservice to create customers to customerservice
- uses community service to create communities to guardians and then add children to those communities
  as dependants.
- writes 2 files:
  - effica_added_communityIDs.txt, contains added community id:s
  - effica_addedCustomerIDs.txt, contains added customer id:s

Helmi Customer
- Selecting Helmi Customer functionality from the UI adds the customers to customer service and 
  creates the files for updating the LDAP.
  
- Selecting Helmi Customer PICs functionality from the UI generates list of child and guardian
  PICs so they can be checked against existing data.

LDIF
- columns: NOT_USED, NOT_USED, NOT_USED, child pic postfix, child birthdate, NOT_USED, 
  child firstnames, child lastname, NOT_USED, NOT_USED, NOT_USED, NOT_USED, NOT_USED, guardian phones,
  guardian emails, guardians, guardian 1 PIC, guardian 2 PIC, child unit, child group
- all customers are added to group "kuntalainen" in LDAP
- all children are additionally added to groups in LDAP. The group name is created by concatenating
  child unit and child group and removing all ',' and '/' characters.
- as a result following files for LDAP are created  
  - Helmi_customer_structure.ldif, contains the structure of the LDAP needed to add the information
  - HelmiCustomer.ldif, contains creation clauses just for customers
  - HelmiCustomerGroup.ldif, contains "kuntalainen" group update clause
  - Child group files named after groupname + .ldif
  - Helmi_All.ldif, contains all previous files contents, excluding the structure creation

WebService
- columns: NOT_USED, NOT_USED, NOT_USED, child pic postfix, child birthdate, NOT_USED, 
  child firstnames, child lastname, child nationality, child kunta, child language, NOT_USED, address,
  guardian phones, guardian emails, guardians, guardian 1 PIC, guardian 2 PIC, child unit, child group
- uses a webservice to create customers to customerservice
- uses community service to create communities to guardians and then add children to those communities
  as dependants.
- writes 2 files:  
  helmi_added_communityIDs.txt, contains added community id:s
  helmi_addedCustomerIDs.txt, contains added customer id:s
  
**************************************************************************************************************
IMPROVEMENT NEEDS!!!
**************************************************************************************************************

Effica customers WS
- child kuntakoodi, kielikoodi are directly from excel, allthough they should be codes
- Guardian has hardcoded kuntakoodi, kielikoodi = "011", "FI"
- Second guardian is not used at all
- status = elossa
- kansalaisuusKoodi = FI
- turvakieltoKytkin = false
- in kuntakoodi and kielikoodi only first 10  characters are used
- second email or phone number is not used even if they are provided

WS Osoite field:
- addressType = virallinen
- postilokeroTeksti = ""
- maatunnusKoodi = ""

WS Community:
- community type = guardian_community
- guardian role = guardian
- child role = dependant

WS Phone number:
- phone number class = gsm
- phone number type = kotipuhelin

Effica customer LDIF
- All customers are missing user.id from LDIF, PIC is used instead.
- second email or phone number is not used even if they are provided
- Second guardian is not used at all

Helmi customer WS
- child kuntakoodi, kielikoodi are directly from excel, allthough they should be codes
- data is missing puuttuu post office and post code
- Guardians have hardcoded kuntakoodi, kielikoodi = "011", "FI"
- Guardians and children use the same address
- status = elossa
- kansalaisuusKoodi = FI
- turvakieltoKytkin = false
- in kuntakoodi and kielikoodi only first 10  characters are used

WS Osoite field:
- addressType = virallinen
- postilokeroTeksti = ""
- maatunnusKoodi = ""

WS Community:
- community type = guardian_community
- guardian role = guardian
- child role = dependant

WS Phone number:
- phone number class = gsm
- phone number type = kotipuhelin

Helmi customer LDIF
- All customers are missing user.id from LDIF, PIC is used instead.