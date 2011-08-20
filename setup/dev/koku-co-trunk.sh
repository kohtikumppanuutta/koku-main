#!/bin/sh

mkdir services service-api ui processes
cd services
svn co https://svn.mermit.fi/projects/kohtikumppanuutta/services/trunk
cd ../service-api
svn co https://svn.mermit.fi/projects/kohtikumppanuutta/service-api/trunk
cd ../ui
svn co https://svn.mermit.fi/projects/kohtikumppanuutta/ui/trunk
cd ../processes
svn co https://svn.mermit.fi/projects/kohtikumppanuutta/processes/trunk

