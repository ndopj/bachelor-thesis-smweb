# bachelors-thesis-smweb

This repository contains all work related to my bachelors thesis (modernization of JavaEE application SMWeb) for which more informations can be found here: https://is.muni.cz/th/suevf/ (written in Slovak, abstract English). Projects contained inside this repository are demo applications and new generation project for JavaEE system called SMWeb.
Purpose of these demos is to choose correct framework for modernization of SMWEB so
not all of them contain full spec documentation.
All projects here use Maven tool, so they can be run in most IDEs or directly from command line.


### smweb-prototype-spring-boot-jsf  
Demo using Spring Boot web application with jsf pages on frontend.  
There is live instance of this demo in case of compilation problems at this address:  
http://gandalf.servermechanics.cz/smweb-prototype-spring-boot-jsf/  
There is no guarantee that this demo will be live at time of reading this.  
Compilation:  
<pre>
    1. Maven         - navigate to root directory. Run mvn clean install. This will generate .war file which can be uploaded on any  
                       Tomcat 9 server.  
    2. Intellij IDEA - open root directory as project. Add new run configuration. Choose spring boot and as main class choose  
                       SpringBootWithJSFApplication.java then hit run and web application should start on localhost:8080.
</pre>

### smweb-prototype-javaee-jsf
Demo using JavaEE 8 specificated web application with jsf pages on frontend.  
There is live instance of this demo in case of compilation problems at this address:  
http://gandalf.servermechanics.cz/smweb-prototype-spring-boot-jsf/  
There is no guarantee that this demo will be live at time of reading this.
Compilation:  
<pre>
    1. Maven         - navigate to root directory. Run mvn clean install. This will generate .war file which can be uploaded on any  
                       WildFly:16.x server.  
    2. Intellij IDEA - open root directory as project. Add new run configuration. Choose Jboss/local template and select some Jboss  
                       16.x implementation(demo was developed and tested on wildfly-17.0.1 but should be able to run on any  
                       application server supporting full JavaEE 8 specification). In configuration go to deployments and as deploy  
                       artifact choose smweb-prototype-javaee-jsf:war then hit run and application server should start on  
                       localhost:8080.
</pre>

### smweb-new-generation
This project is direct implementation of bachelors thesis for which this repository was created.
It can be deployed on: Tomcat 9.0.10, Jetty 9.4.22 and Wildfly 17.0.0
Full functionallity of this project can be found at:
https://is.muni.cz/auth/th/suevf/?fakulta=1433;obdobi=7643;sorter=vedouci;balik=1275

Compilation:
<pre>
    1. Maven         - navigate to root directory. Run mvn clean install. This will generate .war file which can be uploaded on any  
                       mentioned servers.  
    2. Intellij IDEA - open root directory as project. Add new run configuration for any mentioned server.
</pre>

