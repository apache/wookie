**** These instructions are now out of date - need to update them 14/12/2007

Setting up this project
-----------------------

Deployment to JBoss 4.0.4 - ccrt version of JBoss
--------------------------------------------------
Java 1.5 extension do not work in this version of JBoss
Fix: go to   "ccrt\jboss-4.0.4.GA\server\default\deploy\jbossweb-tomcat55.sar\conf"
and edit "web.xml"
find the following...

<!--Uncomment to use jdk1.5 features in jsp pages

      <init-param>
         <param-name>compilerSourceVM</param-name>
         <param-value>1.5</param-value>
      </init-param>
-->

Uncomment it.

See http://jira.jboss.com/jira/browse/JBWEB-87
----------------------------------------------------------------

You will need mysql database v 5+
* In the scripts folder there is an sql script file which will create the DB (create_database.sql)
* In the scripts folder there is also sql script file which should allow you to populate the DB.(db_dump.sql) 		
* The db user should have the credentials "root", "admin"
* The coppercore runtime package (CCRT), which can be found here (http://coppercore.org)

This project is setup as a web project within eclipse. (it can be launched via eclipse).
To avoid port problems, it is best to change your tomcat instance to run on port 8081 so 
that it does not interfere with the jboss instance running coppercore. (which will be on 8080)
At some point in the near future this web app will be configured to also run under the same jboss instance.
(but its easier under development to do this from eclipse using tomcat)

Once you have this eclipse project setup (and you can run it under tomcat from eclipse on port 8081), you must also
take some additional steps before you can use it.

1. Update Coppercore using the .war files found under this project under the "setup_resources" folder 

	Copy across the modified ccsi.war and sled3.war file to your coppercore RT instance, under the folder

	*your_path*\ccrt\jboss-4.0.4.GA\server\default\deploy

	(TODO: the modifed src of the wars will be included in this project)

2. Import the "guitar_types_toolsv3.zip" into coppercore and create user/s to run the UOL.
	This is a test package containing some of the widgets.


Running the whole system
------------------------

* start the mysql database
* next you should start the widgetservice from eclipse (on port 8081)
* next you should start CCRT - using the startup script found at the root of the CCRT zip


Issues
------
At present the macOs widgets don't behave correctly in IE. (wikipedia & youtube). For now it is advised to 
use Mozilla firefox to view the LD player.
  
Problems - email P.Sharples@bolton.ac.uk
