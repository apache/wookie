Prerequisites
(a) java v1.5.* or later installed
(b) tomcat v5.5.* or v6.0.* installed (See below for special instructions for v6.*)
(c) Mysql v5.0.* installed
 
Setup 
(1) Place the "wookie" folder found in this archive, under your "webapps" folder in tomcat
	- usually found under tomcat/webapps	
(2) You will need to alter the "tomcat-users.xml" file under your tomcat instance
	- usually found under tomcat/conf

	You will need to add two entries to enable the admin username/password...
	
	<role rolename="widgetadmin"/>
	<user password="java" roles="widgetadmin" username="java"/>
	
	...otherwise you will not be able to login to the admin section of the widget server 
	(unless you remove the authentication defined in the web.xml file)
(3) Login to mysql and create a new user for the Wookie application
(4) Run the "widgetdb_mysql.sql" script found in this archive
(5) Grant the Wookie user access rights to the "widgetdb" schema in mysqladmin
(6) Edit WEB-INF\classes\hibernate.cfg to include the database username and password
(7) Start tomcat and navigate to http://localhost:8080/wookie in your browser

Tomcat 6.0.* Users Only
=====================
Tomcat 6.0.* includes a version of Expression Library (el.jar) that conflicts with the bundled juel.jar in Wookie. 

To resolve this issue, replace

juel.2.1.0.jar 

with: 

juel-impl-2.1.2.jar 

Which can be downloaded from: 

http://juel.sourceforge.net/ 

More background on the workaround to this problem can be found here: https://issues.apache.org/jira/browse/WOOKIE-14