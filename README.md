## frontend

Sample frontend

### Build

Run mvn clean verify

### Install

1. Insall Java from oracle http://download.oracle.com/otn-pub/java/jdk/8u161-b12/2f38c3b165be4555a1fa6e98c45e0808/jre-8u161-windows-x64.tar.gz

Other versions: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

2. Install Tomcat http://www-us.apache.org/dist/tomcat/tomcat-8/v8.5.30/bin/apache-tomcat-8.5.30.exe

Other Versions: https://tomcat.apache.org/download-80.cgi

3.  Build and copy the frontend/target/BeesnDrawTrading.war over to $CATALINA_BASE/webapps/

### Update

4. Build and copy the latest war over to $CATALINA_BASE/webapps/ (If the tomcat server is up and runnig it will take affect in few seconds)
