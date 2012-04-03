KoKu project code base consists of the following Git repositories

koku-esb          KoKu ESB layer artifacts (e.g. customized service proxies)
koku-extras       KoKu extra modules (e.g. optional authentication modules)
koku-main         KoKu common libraries, test automation, setup files and tools etc.
koku-processes    KoKu BPM process definitions
koku-service-api  KoKu SOA service layer API artifacts (e.g. service interface descriptions and service client code)
koku-services     KoKu SOA service layer implementation
koku-ui           KoKu user interface layer implementation (e.g. portlet projects)


============================================

koku-main repository directory structure:

common/		Artifacts that are shared across layers.
demo/     demo and prototype code
docs/		  Contains all the documentation for a project, including HTML files, installation and setup files, etc.
koku-ta/  Testautomation
setup/		Contains files that are relevant to the environment for KoKu. This directory may contain database setup files or any other files that are used to configure application servers.
tools/    Different development process and administration tools
